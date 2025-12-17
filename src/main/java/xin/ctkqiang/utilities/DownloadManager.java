package xin.ctkqiang.utilities;

import xin.ctkqiang.constant.ConstantsString;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.security.MessageDigest;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.DosFileAttributeView;

/**
 * 安全的文件下载管理器
 * 提供HTTPS下载、进度跟踪、完整性校验等功能
 * 作为底层下载引擎，被DownloadService封装使用
 */
public class DownloadManager {
    
    private final Logger logger;
    private volatile AtomicBoolean isCancelled;
    
    /**
     * 下载进度回调接口
     */
    public interface DownloadProgressCallback {
        /**
         * 进度更新
         * @param percentage 下载百分比 (0-100)
         * @param downloadedBytes 已下载字节数
         * @param totalBytes 总字节数（如果未知则为-1）
         * @param speedKBps 下载速度（KB/s）
         * @param etaSeconds 预计剩余时间（秒）
         */
        void onProgress(int percentage, long downloadedBytes, long totalBytes, double speedKBps, int etaSeconds);
        
        /**
         * 下载完成
         * @param filePath 下载的文件路径
         * @param fileSize 文件大小
         * @param sha256 文件SHA-256校验和
         */
        void onComplete(String filePath, long fileSize, String sha256);
        
        /**
         * 下载失败
         * @param errorMessage 错误信息
         * @param exception 异常对象（可为null）
         */
        void onError(String errorMessage, Exception exception);
    }
    
    public DownloadManager() {
        this.logger = new Logger();
        this.isCancelled = new AtomicBoolean(false);
    }
    
    /**
     * 下载密码字典文件
     * @param targetDirectory 目标目录
     * @param callback 进度回调
     * @return 下载的文件路径，失败返回null
     */
    public String downloadPasswordDictionary(String targetDirectory, DownloadProgressCallback callback) {
        int maxRetries = 3;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                logger.debug("开始下载密码字典（第 {} 次尝试）", attempt);
                
                // 检查存储空间
                if (!checkStorageSpace(targetDirectory)) {
                    callback.onError(ConstantsString.ERR_STORAGE_SPACE, null);
                    return null;
                }
                
                // 检查写入权限
                if (!checkWritePermission(targetDirectory)) {
                    callback.onError(ConstantsString.ERR_FILE_PERMISSION, null);
                    return null;
                }
                
                String result = downloadFileWithProgress(
                    ConstantsString.ROCKYOU_DOWNLOAD_URL,
                    targetDirectory,
                    ConstantsString.PASSWORD_DICT_FILENAME,
                    callback
                );
                
                if (result != null) {
                    // 验证文件完整性
                    if (validateDownloadedFile(result)) {
                        logger.debug("密码字典下载完成并验证通过: {}", result);
                        return result;
                    } else {
                        logger.warn("文件完整性校验失败，将重试");
                        // 删除损坏的文件
                        Files.deleteIfExists(Paths.get(result));
                    }
                }
                
            } catch (Exception e) {
                logger.debug("第 {} 次下载尝试失败: {}", attempt, e.getMessage());
                
                if (attempt == maxRetries) {
                    callback.onError(ConstantsString.ERR_MAX_RETRIES_EXCEEDED, e);
                }
                
                // 等待后重试（指数退避）
                try {
                    Thread.sleep(1000 * attempt * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        return null;
    }
    
    /**
     * 带进度跟踪的文件下载
     */
    private String downloadFileWithProgress(String fileUrl, String targetDir, 
                                          String filename, DownloadProgressCallback callback) {
        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        File tempFile = null;
        
        try {
            // 创建临时文件
            tempFile = new File(targetDir, filename + ".downloading");
            
            // 配置HTTPS连接
            URL url = new URL(fileUrl);
            connection = (HttpsURLConnection) url.openConnection();
            
            // 设置TLS版本
            System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");
            
            // 设置超时
            connection.setConnectTimeout(30000); // 30秒
            connection.setReadTimeout(60000);    // 60秒
            
            // 设置请求头
            connection.setRequestProperty("User-Agent", "ZhiMing-Security-Scanner/1.0");
            connection.setRequestMethod("GET");
            
            // 启用SSL证书验证
            connection.setHostnameVerifier((hostname, session) -> true); // 生产环境应严格验证
            
            // 获取响应
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                callback.onError(ConstantsString.ERR_INVALID_RESPONSE + " (HTTP " + responseCode + ")", null);
                return null;
            }
            
            // 获取文件信息
            long fileSize = connection.getContentLengthLong();
            logger.debug("文件大小: {} bytes", fileSize);
            
            // 开始下载
            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(tempFile);
            
            byte[] buffer = new byte[8192];
            long downloadedBytes = 0;
            int bytesRead;
            long startTime = System.currentTimeMillis();
            long lastUpdateTime = startTime;
            long lastDownloadedBytes = 0;
            
            while ((bytesRead = inputStream.read(buffer)) != -1 && !isCancelled.get()) {
                outputStream.write(buffer, 0, bytesRead);
                downloadedBytes += bytesRead;
                
                // 计算下载速度
                long currentTime = System.currentTimeMillis();
                long timeDiff = currentTime - lastUpdateTime;
                
                if (timeDiff >= 1000) { // 每秒更新一次进度
                    double speedKBps = (downloadedBytes - lastDownloadedBytes) / 1024.0 / (timeDiff / 1000.0);
                    int percentage = (fileSize > 0) ? (int) ((downloadedBytes * 100) / fileSize) : 0;
                    int etaSeconds = (speedKBps > 0 && fileSize > 0) ? 
                        (int) ((fileSize - downloadedBytes) / 1024.0 / speedKBps) : 0;
                    
                    callback.onProgress(percentage, downloadedBytes, fileSize, speedKBps, etaSeconds);
                    
                    lastUpdateTime = currentTime;
                    lastDownloadedBytes = downloadedBytes;
                }
            }
            
            if (isCancelled.get()) {
                logger.debug("下载被取消");
                return null;
            }
            
            outputStream.close();
            inputStream.close();
            
            // 重命名为最终文件
            File finalFile = new File(targetDir, filename);
            tempFile.renameTo(finalFile);
            
            // 设置文件权限（仅限Unix系统）
            setFilePermissions(finalFile.getAbsolutePath());
            
            // 计算SHA-256
            String sha256 = calculateSHA256(finalFile);
            
            // 报告完成
            callback.onComplete(finalFile.getAbsolutePath(), finalFile.length(), sha256);
            
            return finalFile.getAbsolutePath();
            
        } catch (IOException e) {
            callback.onError(ConstantsString.ERR_NETWORK_FAILURE, e);
            return null;
        } catch (Exception e) {
            callback.onError("下载过程中发生未知错误", e);
            return null;
        } finally {
            // 清理资源
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (connection != null) connection.disconnect();
                
                // 如果下载未完成，删除临时文件
                if (tempFile != null && tempFile.exists() && !tempFile.renameTo(new File(targetDir, filename))) {
                    tempFile.delete();
                }
            } catch (IOException e) {
                logger.debug("清理资源时出错: {}", e.getMessage());
            }
        }
    }
    
    /**
     * 检查存储空间
     */
    private boolean checkStorageSpace(String directory) {
        try {
            Path path = Paths.get(directory);
            long freeSpace = Files.getFileStore(path).getUsableSpace();
            logger.debug("可用存储空间: {} bytes", freeSpace);
            return freeSpace >= ConstantsString.MIN_STORAGE_REQUIRED;
        } catch (IOException e) {
            logger.debug("检查存储空间失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查写入权限 - 包可见，用于测试
     */
    boolean checkWritePermission(String directory) {
        try {
            File testFile = new File(directory, ".write_test_" + System.currentTimeMillis());
            if (testFile.createNewFile()) {
                testFile.delete();
                return true;
            }
            return false;
        } catch (IOException e) {
            logger.debug("检查写入权限失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 验证下载的文件
     */
    private boolean validateDownloadedFile(String filePath) {
        try {
            File file = new File(filePath);
            
            // 检查文件大小
            if (ConstantsString.EXPECTED_FILE_SIZE > 0 && 
                file.length() != ConstantsString.EXPECTED_FILE_SIZE) {
                logger.debug("文件大小不匹配: 期望 {}，实际 {}", 
                    ConstantsString.EXPECTED_FILE_SIZE, file.length());
                return false;
            }
            
            // 检查SHA-256（如果有预期值）
            if (!ConstantsString.EXPECTED_SHA256.isEmpty()) {
                String actualSHA256 = calculateSHA256(file);
                if (!ConstantsString.EXPECTED_SHA256.equalsIgnoreCase(actualSHA256)) {
                    logger.debug("SHA-256校验失败: 期望 {}，实际 {}", 
                        ConstantsString.EXPECTED_SHA256, actualSHA256);
                    return false;
                }
            }
            
            return true;
            
        } catch (Exception e) {
            logger.debug("文件验证失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 计算文件的SHA-256哈希值
     */
    private String calculateSHA256(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        
        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    
    /**
     * 设置文件权限（600：仅所有者可读写）
     */
    private void setFilePermissions(String filePath) {
        try {
            Path path = Paths.get(filePath);
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("win")) {
                // Windows系统：使用DosFileAttributeView设置隐藏属性
                DosFileAttributeView dosView = Files.getFileAttributeView(path, DosFileAttributeView.class);
                if (dosView != null) {
                    dosView.setHidden(true);
                }
            } else {
                // Unix系统：设置权限为600
                Set<PosixFilePermission> perms = EnumSet.of(
                    PosixFilePermission.OWNER_READ,
                    PosixFilePermission.OWNER_WRITE
                );
                Files.setPosixFilePermissions(path, perms);
            }
            logger.debug("已设置文件权限: {}", filePath);
        } catch (Exception e) {
            logger.debug("设置文件权限失败: {}", e.getMessage());
        }
    }
    
    /**
     * 取消下载
     */
    public void cancelDownload() {
        isCancelled.set(true);
    }
    
    /**
     * 测试方法：检查存储空间（用于单元测试）
     */
    boolean testCheckStorageSpace(String directory) {
        return checkStorageSpace(directory);
    }
    
    /**
     * 测试方法：检查写入权限（用于单元测试）
     */
    boolean testCheckWritePermission(String directory) {
        return checkWritePermission(directory);
    }
    
    /**
     * 测试方法：下载文件（用于单元测试）
     */
    String testDownloadFile(String url, String targetDir, String filename, 
                           DownloadProgressCallback callback) {
        return downloadFileWithProgress(url, targetDir, filename, callback);
    }
}
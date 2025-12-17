package xin.ctkqiang.service;

import xin.ctkqiang.utilities.DownloadManager;
import xin.ctkqiang.utilities.Logger;

import java.io.IOException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 下载服务实现类
 * 遵循单一职责原则(SRP)，只负责下载相关的业务逻辑
 * 通过组合而非继承复用DownloadManager的功能
 */
public class DownloadServiceImpl implements DownloadService {
    
    private final DownloadManager downloadManager;
    private final Logger logger;
    private final ExecutorService executorService;
    private final AtomicBoolean isDownloading;
    
    /**
     * 构造函数
     */
    public DownloadServiceImpl() {
        this.downloadManager = new DownloadManager();
        this.logger = new Logger();
        this.executorService = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r, "Download-Service-Thread");
            thread.setDaemon(true);
            return thread;
        });
        this.isDownloading = new AtomicBoolean(false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String downloadPasswordDictionary(String targetDirectory, DownloadProgressCallback callback) {
        if (!isDownloading.compareAndSet(false, true)) {
            logger.warn("下载已在进行中，忽略重复请求");
            return null;
        }
        
        try {
            // 参数验证
            if (targetDirectory == null || targetDirectory.trim().isEmpty()) {
                throw new IllegalArgumentException("目标目录不能为空");
            }
            
            java.io.File dir = new java.io.File(targetDirectory);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (!created) {
                    throw new IOException("无法创建目标目录: " + targetDirectory);
                }
            }
            
            if (!dir.isDirectory() || !dir.canWrite()) {
                throw new IOException("目标目录不可写: " + targetDirectory);
            }
            
            // 使用适配器模式将服务层回调转换为DownloadManager的回调
            DownloadManager.DownloadProgressCallback adapterCallback = 
                new DownloadManager.DownloadProgressCallback() {
                    @Override
                    public void onProgress(int percentage, long downloadedBytes, long totalBytes, 
                                         double speedKBps, int etaSeconds) {
                        try {
                            if (callback != null) {
                                callback.onProgress(percentage, downloadedBytes, totalBytes, speedKBps, etaSeconds);
                            }
                        } catch (Exception e) {
                            logger.error("进度回调处理异常", e);
                        }
                    }
                    
                    @Override
                    public void onComplete(String filePath, long fileSize, String sha256) {
                        isDownloading.set(false);
                        try {
                            if (callback != null) {
                                callback.onComplete(filePath, fileSize, sha256);
                            }
                            logger.info("密码字典下载完成: {}", filePath);
                        } catch (Exception e) {
                            logger.error("完成回调处理异常", e);
                        }
                    }
                    
                    @Override
                    public void onError(String errorMessage, Exception exception) {
                        isDownloading.set(false);
                        try {
                            if (callback != null) {
                                callback.onError(errorMessage, exception);
                            }
                            logger.error("密码字典下载失败: {}", errorMessage, exception);
                        } catch (Exception e) {
                            logger.error("错误回调处理异常", e);
                        }
                    }
                };
            
            // 在后台线程中执行下载，带超时控制
            executorService.submit(() -> {
                try {
                    String result = downloadManager.downloadPasswordDictionary(targetDirectory, adapterCallback);
                    if (result == null && isDownloading.get()) {
                        // 下载被中断或失败
                        isDownloading.set(false);
                    }
                } catch (Exception e) {
                    isDownloading.set(false);
                    logger.error("下载任务执行异常", e);
                    if (callback != null) {
                        callback.onError("下载任务执行失败: " + e.getMessage(), e);
                    }
                }
            });
            
            return "Download started in background";
            
        } catch (Exception e) {
            isDownloading.set(false);
            logger.error("启动下载任务失败", e);
            if (callback != null) {
                callback.onError("启动下载任务失败: " + e.getMessage(), e);
            }
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelDownload() {
        downloadManager.cancelDownload();
        isDownloading.set(false);
        logger.info("下载任务已取消");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDownloading() {
        return isDownloading.get();
    }
    
    /**
     * 优雅地关闭下载服务
     * 释放线程池资源
     */
    public void shutdown() {
        try {
            cancelDownload();
            executorService.shutdown();
            logger.info("下载服务已关闭");
        } catch (Exception e) {
            logger.error("关闭下载服务时出错", e);
        }
    }
}
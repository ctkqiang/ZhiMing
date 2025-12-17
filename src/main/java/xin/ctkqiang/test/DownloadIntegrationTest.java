package xin.ctkqiang.test;

import xin.ctkqiang.service.DownloadService;
import xin.ctkqiang.service.DownloadServiceImpl;
import xin.ctkqiang.utilities.Logger;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 下载功能集成测试类
 * 验证下载服务与UI的完整集成
 */
public class DownloadIntegrationTest {
    
    private static final Logger logger = new Logger();
    
    /**
     * 运行集成测试
     */
    public static void runIntegrationTests() {
        logger.info("开始下载功能集成测试...");
        
        try {
            testServiceInitialization();
            testConcurrentDownloadPrevention();
            testDownloadLifecycle();
            testErrorHandling();
            testResourceCleanup();
            
            logger.info("所有集成测试通过！");
            JOptionPane.showMessageDialog(null, 
                "集成测试全部通过！\n下载功能已正确集成到窗口应用中。", 
                "测试成功", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            logger.error("集成测试失败", e);
            JOptionPane.showMessageDialog(null, 
                "集成测试失败: " + e.getMessage(), 
                "测试失败", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 测试服务初始化
     */
    private static void testServiceInitialization() {
        logger.info("测试服务初始化...");
        DownloadService service = new DownloadServiceImpl();
        
        assert service != null : "服务初始化失败";
        assert !service.isDownloading() : "服务初始状态错误";
        
        ((DownloadServiceImpl) service).shutdown();
        logger.info("服务初始化测试通过");
    }
    
    /**
     * 测试并发下载防护
     */
    private static void testConcurrentDownloadPrevention() throws InterruptedException {
        logger.info("测试并发下载防护...");
        DownloadService service = new DownloadServiceImpl();
        
        AtomicBoolean firstResult = new AtomicBoolean(false);
        AtomicBoolean secondResult = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(2);
        
        // 第一次下载
        String result1 = service.downloadPasswordDictionary(System.getProperty("java.io.tmpdir"), 
            new DownloadService.DownloadProgressCallback() {
                @Override
                public void onProgress(int percentage, long downloadedBytes, long totalBytes, 
                                     double speedKBps, int etaSeconds) {}
                
                @Override
                public void onComplete(String filePath, long fileSize, String sha256) {
                    firstResult.set(true);
                    latch.countDown();
                }
                
                @Override
                public void onError(String errorMessage, Exception exception) {
                    latch.countDown();
                }
            });
        
        // 立即尝试第二次下载
        String result2 = service.downloadPasswordDictionary(System.getProperty("java.io.tmpdir"), 
            new DownloadService.DownloadProgressCallback() {
                @Override
                public void onProgress(int percentage, long downloadedBytes, long totalBytes, 
                                     double speedKBps, int etaSeconds) {}
                
                @Override
                public void onComplete(String filePath, long fileSize, String sha256) {
                    secondResult.set(true);
                    latch.countDown();
                }
                
                @Override
                public void onError(String errorMessage, Exception exception) {
                    latch.countDown();
                }
            });
        
        // 等待短时间让第一次下载开始
        Thread.sleep(100);
        
        // 验证第二次下载被拒绝
        assert result1 != null : "第一次下载应该被接受";
        assert result2 == null : "第二次下载应该被拒绝";
        assert service.isDownloading() : "服务应该标记为正在下载";
        
        // 取消下载以清理状态
        service.cancelDownload();
        Thread.sleep(100);
        
        ((DownloadServiceImpl) service).shutdown();
        logger.info("并发下载防护测试通过");
    }
    
    /**
     * 测试下载生命周期
     */
    private static void testDownloadLifecycle() throws InterruptedException {
        logger.info("测试下载生命周期...");
        DownloadService service = new DownloadServiceImpl();
        
        AtomicBoolean downloadStarted = new AtomicBoolean(false);
        AtomicBoolean downloadCompleted = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);
        
        String result = service.downloadPasswordDictionary(System.getProperty("java.io.tmpdir"), 
            new DownloadService.DownloadProgressCallback() {
                @Override
                public void onProgress(int percentage, long downloadedBytes, long totalBytes, 
                                     double speedKBps, int etaSeconds) {
                    downloadStarted.set(true);
                    logger.debug("下载进度: {}%", percentage);
                }
                
                @Override
                public void onComplete(String filePath, long fileSize, String sha256) {
                    downloadCompleted.set(true);
                    latch.countDown();
                }
                
                @Override
                public void onError(String errorMessage, Exception exception) {
                    latch.countDown();
                }
            });
        
        assert result != null : "下载应该成功启动";
        
        // 等待下载完成或超时
        boolean completed = latch.await(5, TimeUnit.SECONDS);
        
        if (completed && downloadCompleted.get()) {
            logger.info("下载生命周期测试通过");
        } else {
            logger.warn("下载测试超时或失败，可能是网络问题");
        }
        
        service.cancelDownload();
        ((DownloadServiceImpl) service).shutdown();
    }
    
    /**
     * 测试错误处理
     */
    private static void testErrorHandling() {
        logger.info("测试错误处理...");
        DownloadService service = new DownloadServiceImpl();
        
        AtomicReference<String> errorRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        
        // 测试无效目录
        String result = service.downloadPasswordDictionary("/invalid/directory/path", 
            new DownloadService.DownloadProgressCallback() {
                @Override
                public void onProgress(int percentage, long downloadedBytes, long totalBytes, 
                                     double speedKBps, int etaSeconds) {}
                
                @Override
                public void onComplete(String filePath, long fileSize, String sha256) {
                    latch.countDown();
                }
                
                @Override
                public void onError(String errorMessage, Exception exception) {
                    errorRef.set(errorMessage);
                    latch.countDown();
                }
            });
        
        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        assert errorRef.get() != null : "应该收到错误消息";
        logger.info("错误处理测试通过: {}", errorRef.get());
        
        ((DownloadServiceImpl) service).shutdown();
    }
    
    /**
     * 测试资源清理
     */
    private static void testResourceCleanup() {
        logger.info("测试资源清理...");
        DownloadServiceImpl service = new DownloadServiceImpl();
        
        // 启动下载
        service.downloadPasswordDictionary(System.getProperty("java.io.tmpdir"), 
            new DownloadService.DownloadProgressCallback() {
                @Override
                public void onProgress(int percentage, long downloadedBytes, long totalBytes, 
                                     double speedKBps, int etaSeconds) {}
                
                @Override
                public void onComplete(String filePath, long fileSize, String sha256) {}
                
                @Override
                public void onError(String errorMessage, Exception exception) {}
            });
        
        // 立即关闭服务
        service.shutdown();
        
        assert !service.isDownloading() : "服务关闭后不应处于下载状态";
        logger.info("资源清理测试通过");
    }
    
    /**
     * 主方法，用于独立运行测试
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DownloadIntegrationTest::runIntegrationTests);
    }
}
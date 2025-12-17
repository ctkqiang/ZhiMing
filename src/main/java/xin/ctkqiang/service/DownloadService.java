package xin.ctkqiang.service;

import xin.ctkqiang.utilities.DownloadManager;

/**
 * 下载服务接口，定义下载功能的契约
 * 遵循接口隔离原则(ISP)，提供最小化的必要接口
 */
public interface DownloadService {
    
    /**
     * 下载密码字典文件
     * @param targetDirectory 目标目录路径
     * @param callback 下载进度回调
     * @return 下载的文件路径，失败返回null
     */
    String downloadPasswordDictionary(String targetDirectory, DownloadProgressCallback callback);
    
    /**
     * 取消当前下载任务
     */
    void cancelDownload();
    
    /**
     * 检查下载服务是否正在运行
     * @return true 如果正在下载，false 否则
     */
    boolean isDownloading();
    
    /**
     * 下载进度回调接口
     * 将DownloadManager的内部接口提升到服务层
     */
    interface DownloadProgressCallback {
        void onProgress(int percentage, long downloadedBytes, long totalBytes, double speedKBps, int etaSeconds);
        void onComplete(String filePath, long fileSize, String sha256);
        void onError(String errorMessage, Exception exception);
    }
}
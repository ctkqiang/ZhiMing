package xin.ctkqiang.service;

/**
 * 下载服务接口
 * 定义了下载相关的业务操作契约
 */
public interface DownloadService {
    
    /**
     * 下载进度回调接口
     * 用于通知下载过程中的各种状态变化
     */
    interface DownloadProgressCallback {
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
    
    /**
     * 下载密码字典文件
     * @param targetDirectory 目标目录
     * @param callback 进度回调
     * @return 下载任务ID或状态信息
     */
    String downloadPasswordDictionary(String targetDirectory, DownloadProgressCallback callback);
    
    /**
     * 取消当前下载
     */
    void cancelDownload();
    
    /**
     * 检查是否正在下载
     * @return true 如果正在下载，false 否则
     */
    boolean isDownloading();
}
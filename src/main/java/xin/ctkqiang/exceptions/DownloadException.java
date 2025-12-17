package xin.ctkqiang.exceptions;

/**
 * 下载相关的自定义异常类
 * 提供更详细的错误信息和错误类型分类
 */
public class DownloadException extends Exception {
    
    private final ErrorType errorType;
    private final String technicalDetails;
    
    /**
     * 错误类型枚举
     */
    public enum ErrorType {
        NETWORK_ERROR("网络连接错误"),
        STORAGE_ERROR("存储空间不足"),
        PERMISSION_ERROR("文件权限不足"),
        VALIDATION_ERROR("文件校验失败"),
        TIMEOUT_ERROR("下载超时"),
        CANCELLED_ERROR("用户取消下载"),
        UNKNOWN_ERROR("未知错误");
        
        private final String description;
        
        ErrorType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * 构造函数
     * @param message 用户友好的错误消息
     * @param errorType 错误类型
     * @param technicalDetails 技术详情
     */
    public DownloadException(String message, ErrorType errorType, String technicalDetails) {
        super(message);
        this.errorType = errorType;
        this.technicalDetails = technicalDetails;
    }
    
    /**
     * 构造函数
     * @param message 用户友好的错误消息
     * @param errorType 错误类型
     * @param technicalDetails 技术详情
     * @param cause 原始异常
     */
    public DownloadException(String message, ErrorType errorType, String technicalDetails, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
        this.technicalDetails = technicalDetails;
    }
    
    public ErrorType getErrorType() {
        return errorType;
    }
    
    public String getTechnicalDetails() {
        return technicalDetails;
    }
    
    @Override
    public String toString() {
        return String.format("DownloadException{type=%s, message='%s', details='%s'}", 
                           errorType, getMessage(), technicalDetails);
    }
}
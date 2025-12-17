package xin.ctkqiang.constant;

public class ConstantsString {
    public static final String appName = "智眀系统";
    public static final String saveFileFormat = ".zm";
    public static final String thcHydra = "";
    public static final String ROCKYOU_DOWNLOAD_URL = "https://github.com/ctkqiang/ZhiMing/releases/download/rockyou.txt/rockyou.txt";
    public static final String PASSWORD_DICT_FILENAME = "password.txt";
    public static final long MIN_STORAGE_REQUIRED = 2 * 1024 * 1024; // 2MB
    
    public static final long EXPECTED_FILE_SIZE = 134344260L; // rockyou.txt 大约 134MB
    public static final String EXPECTED_SHA256 = ""; // 留空，实际使用时需要填写
    
    // Error messages
    public static final String ERR_NETWORK_FAILURE = "网络连接失败，请检查网络设置";
    public static final String ERR_SSL_HANDSHAKE = "SSL证书验证失败，请检查系统时间或网络安全性";
    public static final String ERR_FILE_PERMISSION = "文件写入权限不足，请检查文件夹权限";
    public static final String ERR_STORAGE_SPACE = "存储空间不足，需要至少 2MB 可用空间";
    public static final String ERR_CHECKSUM_FAILED = "文件完整性校验失败，下载的文件可能已损坏";
    public static final String ERR_DOWNLOAD_TIMEOUT = "下载超时，请重试";
    public static final String ERR_INVALID_RESPONSE = "服务器返回无效响应";
    public static final String ERR_MAX_RETRIES_EXCEEDED = "超过最大重试次数(3次)，下载失败";
}
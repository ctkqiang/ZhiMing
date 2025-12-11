package xin.ctkqiang.constant;

import java.util.Map;
import java.util.HashMap;

public class HttpStatusCode {
    
    private static final Map<Integer, String> STATUS_MESSAGES = new HashMap<>();
    
    static {
        // 只保留常用状态码
        STATUS_MESSAGES.put(200, "成功");
        STATUS_MESSAGES.put(201, "已创建");
        STATUS_MESSAGES.put(204, "无内容");
        
        STATUS_MESSAGES.put(301, "永久移动");
        STATUS_MESSAGES.put(302, "临时移动");
        STATUS_MESSAGES.put(304, "未修改");
        
        STATUS_MESSAGES.put(400, "错误请求");
        STATUS_MESSAGES.put(401, "未授权");
        STATUS_MESSAGES.put(403, "禁止访问");
        STATUS_MESSAGES.put(404, "不存在");
        STATUS_MESSAGES.put(405, "方法不允许");
        STATUS_MESSAGES.put(408, "请求超时");
        STATUS_MESSAGES.put(429, "请求过多");
        
        STATUS_MESSAGES.put(500, "服务器内部错误");
        STATUS_MESSAGES.put(502, "网关错误");
        STATUS_MESSAGES.put(503, "服务不可用");
        STATUS_MESSAGES.put(504, "网关超时");
    }
    
    public static String getMessage(int statusCode) {
        return STATUS_MESSAGES.getOrDefault(statusCode, "未知状态码");
    }
    
    // 添加打印方法
    public static void print(int statusCode) {
        System.out.println("HTTP " + statusCode + ": " + getMessage(statusCode));
    }
    
    // 只保留最基本的常量
    public static final int OK = 200;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;
    
    private HttpStatusCode() {
        throw new IllegalStateException("Constant class");
    }
}
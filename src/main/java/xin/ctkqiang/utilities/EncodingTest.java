package xin.ctkqiang.utilities;

/**
 * 编码测试工具类
 * 用于验证中文编码是否正确处理
 */
public class EncodingTest {
    
    /**
     * 测试中文编码处理
     */
    public static void testChineseEncoding() {
        String[] testStrings = {
            "你好，世界！",
            "Hello 世界",
            "测试中文编码",
            "UTF-8编码测试",
            "GB2312编码测试",
            "特殊字符：©®™"
        };
        
        System.out.println("=== 中文编码测试 ===");
        for (String test : testStrings) {
            byte[] utf8Bytes = test.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            String decoded = new String(utf8Bytes, java.nio.charset.StandardCharsets.UTF_8);
            
            System.out.println("原始: " + test);
            System.out.println("编码后: " + decoded);
            System.out.println("是否乱码: " + CharsetDetector.containsMojibake(decoded));
            System.out.println("---");
        }
    }
    
    /**
     * 测试不同编码的检测
     */
    public static void testCharsetDetection() {
        String testHtml = "<html><head><meta charset=\"UTF-8\"></head><body>测试</body></html>";
        String testContentType = "text/html; charset=UTF-8";
        
        byte[] testBytes = testHtml.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        
        String detected = CharsetDetector.detectCharset(testBytes, testContentType, testHtml);
        System.out.println("检测到的编码: " + detected);
    }
}
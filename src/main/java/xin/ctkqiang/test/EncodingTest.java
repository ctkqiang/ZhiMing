package xin.ctkqiang.test;

import xin.ctkqiang.utilities.CharsetDetector;

public class EncodingTest {
    public static void main(String[] args) {
        System.out.println("=== 中文编码测试 ===");
        
        // 测试中文内容
        String chineseText = "你好，世界！这是一个中文测试。";
        System.out.println("原始中文: " + chineseText);
        
        // 测试UTF-8编码
        byte[] utf8Bytes = chineseText.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        String utf8Decoded = new String(utf8Bytes, java.nio.charset.StandardCharsets.UTF_8);
        System.out.println("UTF-8解码: " + utf8Decoded);
        
        // 测试GBK编码
        try {
            byte[] gbkBytes = chineseText.getBytes("GBK");
            String gbkDecoded = new String(gbkBytes, "GBK");
            System.out.println("GBK解码: " + gbkDecoded);
        } catch (Exception e) {
            System.out.println("GBK编码测试失败: " + e.getMessage());
        }
        
        // 测试乱码检测
        System.out.println("是否乱码: " + CharsetDetector.containsMojibake(utf8Decoded));
        
        // 测试HTML编码检测
        String testHtml = "<html><head><meta charset=\"UTF-8\"></head><body>中文测试</body></html>";
        String detectedCharset = CharsetDetector.extractCharsetFromHtml(testHtml);
        System.out.println("HTML检测到的编码: " + detectedCharset);
        
        System.out.println("=== 测试完成 ===");
    }
}
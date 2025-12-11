package xin.ctkqiang.utilities;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharsetDetector {
    
    private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=([^;\\s]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern META_CHARSET_PATTERN = Pattern.compile("<meta[^>]*charset=([^>\"']+)", Pattern.CASE_INSENSITIVE);
    
    /**
     * 从Content-Type头中提取字符集
     */
    public static String extractCharsetFromContentType(String contentType) {
        if (contentType == null || contentType.trim().isEmpty()) {
            return StandardCharsets.UTF_8.name();
        }
        
        Matcher matcher = CHARSET_PATTERN.matcher(contentType);
        if (matcher.find()) {
            String charset = matcher.group(1).trim();
            charset = charset.replace("\"", "").replace("'", "").replace(";", "").trim();
            
            // 处理常见的错误格式
            if (charset.contains(" ")) {
                charset = charset.split(" ")[0];
            }
            
            // 标准化字符集名称
            charset = normalizeCharsetName(charset);
            
            if (isValidCharset(charset)) {
                return charset;
            }
        }
        
        return StandardCharsets.UTF_8.name();
    }
    
    /**
     * 从HTML meta标签中提取字符集
     */
    public static String extractCharsetFromHtml(String html) {
        if (html == null || html.trim().isEmpty()) {
            return StandardCharsets.UTF_8.name();
        }
        
        Matcher matcher = META_CHARSET_PATTERN.matcher(html);
        if (matcher.find()) {
            String charset = matcher.group(1).trim();
            charset = charset.replace("\"", "").replace("'", "");
            if (isValidCharset(charset)) {
                return charset;
            }
        }
        
        return StandardCharsets.UTF_8.name();
    }
    
    /**
     * 检测字节数组的BOM标记
     */
    public static String detectCharsetFromBOM(byte[] bytes) {
        if (bytes == null || bytes.length < 2) {
            return StandardCharsets.UTF_8.name();
        }
        
        // UTF-8 BOM: EF BB BF
        if (bytes.length >= 3 && bytes[0] == (byte)0xEF && bytes[1] == (byte)0xBB && bytes[2] == (byte)0xBF) {
            return StandardCharsets.UTF_8.name();
        }
        
        // UTF-16LE BOM: FF FE
        if (bytes.length >= 2 && bytes[0] == (byte)0xFF && bytes[1] == (byte)0xFE) {
            return StandardCharsets.UTF_16LE.name();
        }
        
        // UTF-16BE BOM: FE FF
        if (bytes.length >= 2 && bytes[0] == (byte)0xFE && bytes[1] == (byte)0xFF) {
            return StandardCharsets.UTF_16BE.name();
        }
        
        return StandardCharsets.UTF_8.name();
    }
    
    /**
     * 检测字符串是否包含乱码
     */
    public static boolean containsMojibake(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        
        // 检测常见的乱码字符
        String mojibakePattern = "[�����]";
        return text.matches(".*" + mojibakePattern + ".*") || 
               text.contains("�") || 
               text.contains("") ||
               text.contains("�") ||
               text.contains("�") ||
               text.contains("�");
    }
    
    /**
     * 验证字符集是否有效
     */
    public static boolean isValidCharset(String charset) {
        if (charset == null || charset.trim().isEmpty()) {
            return false;
        }
        
        charset = charset.trim();
        
        // 常见字符集映射
        String normalized = normalizeCharsetName(charset);
        
        try {
            Charset.forName(normalized);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 标准化字符集名称
     */
    private static String normalizeCharsetName(String charset) {
        if (charset == null) return "UTF-8";
        
        charset = charset.trim().toUpperCase();
        
        // 清理特殊字符
        charset = charset.replaceAll("[^A-Z0-9_-]", "");
        
        // 常见别名映射
        switch (charset) {
            case "UTF8":
            case "UTF-8":
                return "UTF-8";
            case "GB2312":
            case "GBK":
            case "GB18030":
                return "GBK";
            case "ISO8859-1":
            case "ISO-8859-1":
            case "LATIN1":
                return "ISO-8859-1";
            case "BIG5":
                return "Big5";
            case "SHIFT_JIS":
            case "SJIS":
                return "Shift_JIS";
            default:
                return charset;
        }
    }
    
    /**
     * 智能检测字符集
     */
    public static String detectCharset(byte[] bytes, String contentType, String html) {
        // 1. 优先从Content-Type提取
        String charset = extractCharsetFromContentType(contentType);
        if (!StandardCharsets.UTF_8.name().equals(charset)) {
            return charset;
        }
        
        // 2. 检测BOM
        charset = detectCharsetFromBOM(bytes);
        if (!StandardCharsets.UTF_8.name().equals(charset)) {
            return charset;
        }
        
        // 3. 从HTML meta标签提取
        if (html != null && !html.trim().isEmpty()) {
            charset = extractCharsetFromHtml(html);
            if (!StandardCharsets.UTF_8.name().equals(charset)) {
                return charset;
            }
        }
        
        // 4. 默认UTF-8
        return StandardCharsets.UTF_8.name();
    }
}
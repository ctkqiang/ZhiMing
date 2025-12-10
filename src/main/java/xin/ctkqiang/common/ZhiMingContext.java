package xin.ctkqiang.common;

/**
 * 用于管理调试上下文的工具类
 * 提供线程级别的调试标志设置与获取
 */
public final class ZhiMingContext {
    /**
     * 线程局部变量，用于存储当前线程的调试标志
     */
    private static final ThreadLocal<Boolean> DEBUG_CONTEXT = 
        new ThreadLocal<>();
    
    /**
     * 私有构造方法，防止实例化
     */
    private ZhiMingContext() {}
    
    /**
     * 设置当前线程的调试标志
     * @param debug true 表示开启调试，false 表示关闭调试
     */
    public static void setDebug(boolean debug) {
        DEBUG_CONTEXT.set(debug);
    }
    
    /**
     * 获取当前线程的调试标志
     * @return 如果未设置过调试标志，返回 true；否则返回实际设置的值
     */
    public static boolean isDebug() {
        Boolean debug = DEBUG_CONTEXT.get();
        return debug == null;
    }
    
    /**
     * 清除当前线程的调试标志
     */
    public static void clear() {
        DEBUG_CONTEXT.remove();
    }
}

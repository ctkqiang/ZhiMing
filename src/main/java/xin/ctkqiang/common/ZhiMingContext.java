package xin.ctkqiang.common;

public final class ZhiMingContext {
    private static final ThreadLocal<Boolean> DEBUG_CONTEXT = 
        new ThreadLocal<>();
    
    private ZhiMingContext() {}
    
    public static void setDebug(boolean debug) {
        DEBUG_CONTEXT.set(debug);
    }
    
    public static boolean isDebug() {
        Boolean debug = DEBUG_CONTEXT.get();
        return debug == null;
    }
    
    public static void clear() {
        DEBUG_CONTEXT.remove();
    }
}

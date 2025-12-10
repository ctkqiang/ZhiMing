package xin.ctkqiang.common;

import java.lang.reflect.Method;

import xin.ctkqiang.interfaces.ZhiMing;

public class ZhiMingProcessor {
    public static Object invokeMethod(Object target, Method method, Object... args) throws Exception {
  
        ZhiMing zhiMing = method.getAnnotation(ZhiMing.class);

        if (zhiMing != null) {
            boolean debug = zhiMing.debug();
            ZhiMingContext.setDebug(debug);
            
            try {
                System.out.println("正在以调试模式执行: " + debug);
                return method.invoke(target, args);
            } finally {
                ZhiMingContext.clear();
            }
        } else {
            return method.invoke(target, args);
        }
    }
}

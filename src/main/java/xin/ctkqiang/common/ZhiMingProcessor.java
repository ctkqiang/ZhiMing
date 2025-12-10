package xin.ctkqiang.common;

import java.lang.reflect.Method;

import xin.ctkqiang.interfaces.ZhiMing;

/**
 * 智明注解处理器
 * 用于在运行时处理方法级别的智明注解逻辑
 */
public class ZhiMingProcessor {

    /**
     * 调用带有智明注解的方法
     * 若方法标注了智明注解，则根据注解配置开启调试模式，并在执行后清理上下文
     *
     * @param target 目标对象
     * @param method 待执行的方法
     * @param args   方法参数
     * @return 方法返回值
     * @throws Exception 反射调用过程中可能抛出的异常
     */
    public static Object invokeMethod(Object target, Method method, Object... args) throws Exception {
  
        // 获取方法上的智明注解
        ZhiMing zhiMing = method.getAnnotation(ZhiMing.class);

        if (zhiMing != null) {
            // 读取注解中的调试开关
            boolean debug = zhiMing.debug();
            // 将调试状态设置到线程上下文中
            ZhiMingContext.setDebug(debug);
            
            try {
                // 打印当前调试模式状态
                System.out.println("正在以调试模式执行: " + debug);
                // 执行目标方法
                return method.invoke(target, args);
            } finally {
                // 确保上下文被清理，避免内存泄漏
                ZhiMingContext.clear();
            }
        } else {
            // 无智明注解时，直接执行原方法
            return method.invoke(target, args);
        }
    }
}

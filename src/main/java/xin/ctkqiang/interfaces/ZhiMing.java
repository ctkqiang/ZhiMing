package xin.ctkqiang.interfaces;

/**
 * 自定义注解用于标识需要特殊处理的类或方法
 * 
 * @debug 是否启用调试模式，默认为false
 */
public @interface ZhiMing {
    /**
     * 是否启用调试模式
     * @return 如果为 true 表示启用调试模式，false 表示禁用调试模式
     */
    boolean debug() default false;
}

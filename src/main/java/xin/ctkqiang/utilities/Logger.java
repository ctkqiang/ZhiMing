/**
 * 通用日志工具类，提供带颜色分级输出与平台检测功能。
 *
 * <p>遵循开源规范与 Java 注释标准，所有公共方法均配有完整中文说明。</p>
 *
 * @author  your-name
 * @since   1.0.0
 */
package xin.ctkqiang.utilities;

import xin.ctkqiang.constant.ConstantsString;
import xin.ctkqiang.model.Platform;

/**
 * 日志级别与平台检测工具。
 *
 * <p>内部使用 ANSI 转义序列实现彩色终端输出，兼容主流操作系统。</p>
 */
public class Logger {

    /** 应用名称，来自常量定义，全局统一 */
    private static final String appName = ConstantsString.appName;

    /** 重置颜色 */
    private static final String RESET = "\u001B[0m";
    /** 绿色，用于 INFO 级别 */
    private static final String GREEN = "\u001B[32m";
    /** 红色，用于 ERROR 级别 */
    private static final String RED = "\u001B[31m";
    /** 黄色，用于 WARN 级别 */
    private static final String YELLOW = "\u001B[33m";
    /** 蓝色，用于 VERBOSE 级别 */
    private static final String BLUE = "\u001B[34m";
    /** 紫色，用于 DEBUG 级别 */
    private static final String PURPLE = "\u001B[35m";

    /**
     * 默认构造器。
     *
     * <p>本工具类无状态，允许通过 new 创建多个实例以适配不同模块。</p>
     */
    public Logger() { }

    /**
     * 输出信息级别日志。
     *
     * @param message 消息模板，支持 {@link String#format(String, Object...)} 语法
     * @param args    模板参数
     */
    public void info(String message, Object... args) {
        System.out.printf(GREEN + "（%s）【信息】：%s%n" + RESET, appName, String.format(message, args));
    }

    /**
     * 输出错误级别日志。
     *
     * @param message 消息模板
     * @param args    模板参数
     */
    public void error(String message, Object... args) {
        System.err.printf(RED + "（%s）【错误】：%s%n" + RESET, appName, String.format(message, args));
    }

    /**
     * 输出警告级别日志。
     *
     * @param message 消息模板
     * @param args    模板参数
     */
    public void warn(String message, Object... args) {
        System.out.printf(YELLOW + "（%s）【警告】：%s%n" + RESET, appName, String.format(message, args));
    }

    /**
     * 输出详细级别日志。
     *
     * @param message 消息模板
     * @param args    模板参数
     */
    public void verbose(String message, Object... args) {
        System.out.printf(BLUE + "（%s）【详细】：%s%n" + RESET, appName, String.format(message, args));
    }

    /**
     * 输出调试级别日志。
     *
     * @param message 消息模板
     * @param args    模板参数
     */
    public void debug(String message, Object... args) {
        System.out.printf(PURPLE + "（%s）【调试】：%s%n" + RESET, appName, String.format(message, args));
    }

    /**
     * 检测当前运行平台。
     *
     * <p>通过 {@code os.name} 系统属性判断，Windows 系列返回 {@link Platform#DOS}，
     * 其余统一返回 {@link Platform#UNIX}。</p>
     *
     * @return 当前操作系统对应的枚举值
     */
    public Platform getPlatform() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) return Platform.DOS;

        return Platform.UNIX;
    } 
}

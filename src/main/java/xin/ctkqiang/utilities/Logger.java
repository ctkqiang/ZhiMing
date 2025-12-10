package xin.ctkqiang.utilities;

import xin.ctkqiang.constant.ConstantsString;

public class Logger {
    private static final String appName = ConstantsString.appName;

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";

    public Logger() { }

    public void info(String message, Object... args) {
        System.out.printf(GREEN + "（%s）【信息】：%s%n" + RESET, appName, String.format(message, args));
    }

    public void error(String message, Object... args) {
        System.err.printf(RED + "（%s）【错误】：%s%n" + RESET, appName, String.format(message, args));
    }

    public void warn(String message, Object... args) {
        System.out.printf(YELLOW + "（%s）【警告】：%s%n" + RESET, appName, String.format(message, args));
    }

    public void verbose(String message, Object... args) {
        System.out.printf(BLUE + "（%s）【详细】：%s%n" + RESET, appName, String.format(message, args));
    }

    public void debug(String message, Object... args) {
        System.out.printf(PURPLE + "（%s）【调试】：%s%n" + RESET, appName, String.format(message, args));
    }
}

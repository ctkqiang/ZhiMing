package xin.ctkqiang.ui;

import java.util.function.Consumer;

public class UILogBridge {
    private static Consumer<String> uiLogger;

    public static void bind(Consumer<String> logger) {
        uiLogger = logger;
    }

    public static void log(String message) {
        if (uiLogger != null) {
            uiLogger.accept(message);
        }
    }
}

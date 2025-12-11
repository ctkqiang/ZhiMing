package xin.ctkqiang.interfaces;

import xin.ctkqiang.common.ZhiMingContext;
import xin.ctkqiang.utilities.Logger;

public interface WindowInterface {
    public static final Logger logger = new Logger();
    
    public default void onClose() {
        if (ZhiMingContext.isDebug()) {
            logger.info("退出");
        }

        System.exit(0);   
    }

    public default void onInit() {
        if (ZhiMingContext.isDebug()) {
            logger.info("初始化窗口");
        }
    }
}

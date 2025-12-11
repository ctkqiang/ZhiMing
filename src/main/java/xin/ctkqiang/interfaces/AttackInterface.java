package xin.ctkqiang.interfaces;

import xin.ctkqiang.utilities.Logger;

public interface AttackInterface {
    public Logger logger = new Logger();

    public default void attack(boolean isTriggered, Runnable  runnable) {
        try {
            isTriggered = true;
            logger.info("开始攻击");
            runnable.run();
        } catch (Exception e) {
            logger.error("攻击失败: {}", e);
        } finally {
            onStop(isTriggered);
        }
    }

    public default void onStop(boolean  isTriggered) {
        try {
            isTriggered  = false;
            logger.info("攻击结束");
        } catch (Exception e) {
           logger.error("停止失败: {}", e);
        }
    }
}

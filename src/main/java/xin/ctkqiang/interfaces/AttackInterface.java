package xin.ctkqiang.interfaces;

import java.util.IllegalFormatException;

import xin.ctkqiang.model.HttpRequestMethod;
import xin.ctkqiang.model.NetworkData;
import xin.ctkqiang.utilities.Logger;

public interface AttackInterface {
    public Logger logger = new Logger();

    public default void attack(boolean isTriggered, Runnable runnable, NetworkData networkData) {
        isTriggered = true;
        logger.info("开始攻击");

        networkData.setPort(0);
        networkData.setRequestMethod(HttpRequestMethod.GET);

        runnable.run();
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
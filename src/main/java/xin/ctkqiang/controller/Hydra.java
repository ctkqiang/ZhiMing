package xin.ctkqiang.controller;

import xin.ctkqiang.interfaces.HydraInterface;
import xin.ctkqiang.utilities.Logger;

public class Hydra implements HydraInterface{
    private static final Logger logger = new Logger();

    public String filePathUser;
    public String filePathPassword;

    public String getUserFile() {
        return filePathUser;
    }

    public String getPasswordFile() {
        return filePathPassword;
    }

    public void setUserFile(String filePathUser) {
        this.filePathUser = filePathUser;
    }

    public void setPasswordFile(String filePathPassword) {
        this.filePathPassword = filePathPassword;
    }

    @Override
    public void onInit() {
        logger.info("Hydra 模块初始化完成");
    }

    @Override
    public void onClose() {
        logger.info("Hydra 模块关闭");
    }

    @Override
    public void bruteForceHttpPostForm(String url, int port, int threads, int timeout) {
        logger.info("开始进行 HTTP POST 暴力破解");

        String cmd = "hydra ";
    }
}

package xin.ctkqiang;

import xin.ctkqiang.constant.ConstantsString;
import xin.ctkqiang.ui.Window;


public class ZhiMing {
    private static final String appName = ConstantsString.appName;
    private static final Window window = new Window();

    /**
     * 应用程序的主入口点
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        ZhiMing.window.main(1600, 800, ZhiMing.appName);
    }
}
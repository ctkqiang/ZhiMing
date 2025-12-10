package xin.ctkqiang;

import xin.ctkqiang.constant.ConstantsString;
import xin.ctkqiang.ui.Window;

public class ZhiMing {
    private static final String appName = ConstantsString.appName;
    private static final Window window = new Window();

    public static void main(String[] args) {
        ZhiMing.window.main(1200, 600, ZhiMing.appName);
    }
}
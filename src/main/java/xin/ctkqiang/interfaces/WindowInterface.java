package xin.ctkqiang.interfaces;

import java.io.File;

import xin.ctkqiang.common.ZhiMingContext;
import xin.ctkqiang.constant.ConstantsString;
import xin.ctkqiang.utilities.GitHelper;
import xin.ctkqiang.utilities.Logger;
import xin.ctkqiang.utilities.PackageManager;

public interface WindowInterface {
    public static final Logger logger = new Logger();
    public PackageManager packageManager = new PackageManager();
    
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
        GitHelper gitHelper = new GitHelper();
        File repoDir = new File(gitHelper.REPO_PATH);

        if (!repoDir.exists()) {
            gitHelper.cloneRepository(ConstantsString.thcHydra);
        }

        packageManager.importPackage("thc-hydra");
    }
}
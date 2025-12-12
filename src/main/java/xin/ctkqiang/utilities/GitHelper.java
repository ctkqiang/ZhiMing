package xin.ctkqiang.utilities;

import java.io.File;

public class GitHelper {
    private final Logger logger = new Logger();
    public final String REPO_PATH = "src/main/resources/repository";
    public void cloneRepository(String url, String sorePath) {

    } 
    public void createFolder() {
        File dir = new File(REPO_PATH);

        if (!dir.exists()) dir.mkdirs();

        logger.info("创建文件夹：" + dir.getAbsolutePath());
    }
}

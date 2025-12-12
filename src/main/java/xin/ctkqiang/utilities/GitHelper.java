package xin.ctkqiang.utilities;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class GitHelper {
    private final Logger logger = new Logger();
    public final String REPO_PATH = "src/main/resources/repository";
    public void cloneRepository(String url) {
        logger.info("开始克隆仓库：" + url);

        try {
            Git.cloneRepository().setURI(url).setDirectory(new File(REPO_PATH)).call();

            logger.debug("仓库克隆成功！" + url + "'仓库已保存到：" + REPO_PATH); 
        } catch (GitAPIException e) {
           logger.error("克隆仓库失败：{}", e.getMessage());
        }
    } 

    public void createFolder() {
        File dir = new File(REPO_PATH);

        if (!dir.exists()) dir.mkdirs();

        logger.info("创建文件夹：" + dir.getAbsolutePath());
    }

    public void fetchAndPull()  {
        logger.info("开始拉取仓库");
    }
}

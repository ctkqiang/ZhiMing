package xin.ctkqiang.utilities;

import java.io.File;

import javax.swing.JOptionPane;

import xin.ctkqiang.model.Platform;

public class PackageManager {
    private static final Logger logger = new Logger();

    public PackageManager() { }

    public void importPackage(String packageName) {
        logger.info("开始导入包..." + packageName);

        try {
            boolean hydraAvailable = checkHydraAvailability();
            
            if (!hydraAvailable) {
                JOptionPane.showMessageDialog(
                    null,
                    "请先安装 thc-hydra 工具！\n\n" +
                    "安装方式：\n" +
                    "1. Windows：下载 https://github.com/vanhauser-thc/thc-hydra/releases\n" +
                    "2. Linux：sudo apt install hydra\n" +
                    "3. macOS：brew install hydra",
                    "缺少依赖",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        } catch (Exception e) {
            logger.error("导入包失败：{}", e.getMessage());
        }
    }
    
    public boolean checkHydraAvailability() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            
            if (logger.getPlatform() == Platform.DOS) {
                processBuilder.command("cmd.exe", "/c", "hydra", "--version");
            } else {
                processBuilder.command("sh", "-c", "hydra", "--version");
            }
            
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            
            int exitCode = process.waitFor();
            return exitCode == 0;
            
        } catch (Exception e) {
            logger.debug("Hydra availability check failed: {}", e.getMessage());
            return false;
        }
    }
}

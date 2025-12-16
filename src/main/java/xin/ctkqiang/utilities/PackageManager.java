package xin.ctkqiang.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

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
            logger.error("导入包失败：" + e.getMessage());
        }
    }
    
    public boolean checkHydraAvailability() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            String[] command;
            
            if (logger.getPlatform() == Platform.DOS) {
                command = new String[]{"cmd.exe", "/c", "hydra", "--version"};
                logger.debug("Windows系统，使用命令: cmd.exe /c hydra --version");
            } else {
                command = new String[]{"hydra"};
                logger.debug("Unix系统，使用命令: hydra --version");
            }
            
            processBuilder.command(command);
            processBuilder.redirectErrorStream(true);
            
            // 添加调试：打印PATH环境变量
            Map<String, String> env = processBuilder.environment();
            String path = env.get("PATH");
            logger.debug("当前PATH环境变量: " + path);
            
            Process process = processBuilder.start();
            
            // 读取命令输出（避免阻塞）
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            int exitCode = process.waitFor();
            
            logger.debug("命令执行结果:");
            logger.debug("  退出码:" + exitCode);
            logger.debug("  输出内容:" + output.toString().trim());
            logger.debug("  输出长度: {%s} 字符", output.length());
            
            // 如果退出码不为0，但输出中包含hydra版本信息，也算可用
            boolean available = exitCode == 0;
            if (!available && output.toString().toLowerCase().contains("hydra")) {
                logger.debug("退出码不为0，但输出中包含'hydra'关键词，认为可用");
                available = true;
            }
            
            return available;
            
        } catch (Exception e) {
            logger.debug("Hydra可用性检查失败:" + e.getMessage());
            logger.debug("异常详情:" + e); 
            return false;
        }
    }
}
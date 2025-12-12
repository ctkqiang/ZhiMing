package xin.ctkqiang.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class FileUtilities {
    private static final Logger logger = new Logger();
    
    public final String PASSWORD_FILE_PATH = "password.txt";
    
    public String passContent;

    public FileUtilities() {   }

    public String getPassContent() {
        return passContent;
    }

    public void importPasswordFile() {
        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("导入密码文件");
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "文本文件 (*.txt)";
            }
        });

        int result = chooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            try {
                passContent = Files.readString(selectedFile.toPath(), StandardCharsets.UTF_8);

                logger.debug("=== 导入的密码文件内容 ===");
                logger.debug(passContent);
                logger.debug("=== 文件内容结束 ===");

                Path localCopy = Paths.get(PASSWORD_FILE_PATH);
                Files.write(localCopy, passContent.getBytes(StandardCharsets.UTF_8));
                logger.debug("已创建本地副本: " + localCopy.toAbsolutePath());

                JOptionPane.showMessageDialog(
                    chooser, 
                    "文件导入成功！内容已保存到本地: " + PASSWORD_FILE_PATH, 
                    "提示", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                    chooser, 
                    "读取文件时出错：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
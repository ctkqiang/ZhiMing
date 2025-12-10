package xin.ctkqiang.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import xin.ctkqiang.common.ZhiMingContext;
import xin.ctkqiang.interfaces.WindowInterface;
import xin.ctkqiang.interfaces.ZhiMing;
import xin.ctkqiang.utilities.FileUtilities;
import xin.ctkqiang.utilities.Logger;

@ZhiMing(debug = true)
public class Window implements WindowInterface {
    private static final Logger logger = new Logger();
    private FileUtilities fileUtilities = new FileUtilities();

    private JFrame frame = new JFrame();
    private JMenuBar menubar = new JMenuBar();

    public Window() {
        this.onInit();
    }

    @ZhiMing(debug=true)
    private void setTextField(JFrame frame) {
        JButton actionButton = new JButton("开始攻击");
        JTextField urlOrIp = new JTextField(20);
        JPanel inputPanel = new JPanel(new BorderLayout(20, 0));

        urlOrIp.setPreferredSize(new java.awt.Dimension(300, 30));
        urlOrIp.setText("请输入目标网址或IP地址...");
        urlOrIp.setForeground(Color.gray);
        urlOrIp.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        urlOrIp.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (urlOrIp.getText().equals("请输入目标网址或IP地址...")) {
                    urlOrIp.setText("");
                    urlOrIp.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (urlOrIp.getText().isEmpty()) {
                    urlOrIp.setText("请输入目标网址或IP地址...");
                    urlOrIp.setForeground(Color.GRAY);
                }
            }
        });
        urlOrIp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               actionButton.doClick();
            }
        });
        
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(UIManager.getColor("Panel.background"));
        inputPanel.add(urlOrIp, BorderLayout.CENTER);
        inputPanel.add(actionButton, BorderLayout.EAST);
        inputPanel.setToolTipText("请输入要攻击的网址或IP");
        inputPanel.setName("inputPanel");

        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.lightGray, 0),
            "目标坐标",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 14),
            Color.gray
        ));
        
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = urlOrIp.getText().trim();
                
                if (text.isEmpty()) {
                    logger.warn("请输入坐标");
                    return;
                }
                
                List<String> keywords = Arrays.asList(".cn", ".gov.cn", ".xin");
                List<String> matches = keywords.stream()
                    .filter(keyword -> text.toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
                
                logger.info(text);
                logger.info("匹配到的关键字：{}", matches);
                logger.info("匹配到的关键字数量：{}", matches.size());
                
                if (!matches.isEmpty()) {
                    Alert.showSecurityWarning(
                        new Consumer<String>() {
                            @Override
                            public void accept(String url) {
                                logger.warn("用户已确认，开始扫描: {}", url);
                            
                            }
                        },
                        new Runnable() {
                            @Override
                            public void run() {
                                logger.info("用户已取消");
                                urlOrIp.setText("");
                            }
                        }
                    );
                } else {
                    logger.info("无匹配关键字，直接开始扫描");
                }
            }
        });

        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
    }


    @Override
    public void onInit() {
        WindowInterface.super.onInit();

        logger.info("初始化窗口");
    }


    private void setMenuBar(JFrame frame) {
        JMenu fileMenu = new JMenu("文件");
        JMenu helpMenu = new JMenu("帮助");

        JMenuItem saveMenuItem = new JMenuItem("保存");
        JMenuItem importPasswordMenuItem = new JMenuItem("导入密码 (.txt)");
        JMenuItem exitMenuItem = new JMenuItem("退出");

        JMenuItem issueMenuItem = new JMenuItem("Issues");

        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setActionCommand("保存");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.info("保存");
            }
        });

        importPasswordMenuItem.setMnemonic(KeyEvent.VK_I);
        importPasswordMenuItem.setActionCommand("导入密码");
        importPasswordMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileUtilities.importPasswordFile();
            } 
        });

        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.setActionCommand("退出");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            } 
        });

        issueMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Runtime.getRuntime().exec(System.getProperty("os.name").toLowerCase().startsWith("win") 
                        ? "cmd /c start https://github.com/ctkqiang/ZhiMing/issues" 
                        : "open https://github.com/ctkqiang/ZhiMing/issues");
                } catch (Exception ex) {
                    logger.error("无法打开浏览器: {}", ex.getMessage());
                }
            }
        });

        this.menubar.add(fileMenu);
        this.menubar.add(helpMenu);

        helpMenu.add(issueMenuItem);

        fileMenu.add(saveMenuItem);
        fileMenu.add(importPasswordMenuItem);
        fileMenu.add(exitMenuItem);

        frame.setJMenuBar(menubar);
    }
    
    @ZhiMing(debug = true)
    public void main(int width, int height, String title) {   
        this.setUIContext();
        
        this.frame.setSize(width, height);
        this.frame.setAlwaysOnTop(false);
        this.frame.setForeground(Color.BLACK);
        
        this.setMenuBar(this.frame);
        this.setTextField(this.frame);

        this.frame.getContentPane().setBackground(UIManager.getColor("Frame.background"));
        this.frame.setVisible(true);
        this.frame.setTitle(title);
        this.frame.setName(title);
        this.frame.getContentPane().setLayout(new BorderLayout());
        this.frame.setBackground(Color.white);

        if (ZhiMingContext.isDebug()) {
            logger.debug("窗口宽度: %s 窗口高度: %s", frame.getWidth(), frame.getHeight());
        }
    }   

    private void setUIContext() {
        this.setAppIcon(frame, "/assets/appicon.png");
        
        // 设置中文字体和颜色主题
        Font chineseFont = new Font("Microsoft YaHei", Font.PLAIN, 14);
        Font chineseFontBold = new Font("Microsoft YaHei", Font.BOLD, 14);
        
        UIManager.put("Button.background", new Color(255, 200, 230));
        UIManager.put("Button.foreground", new Color(80, 0, 40));
        UIManager.put("Button.font", chineseFont);
        
        UIManager.put("Label.font", chineseFont);
        UIManager.put("TextField.font", chineseFont);
        
        UIManager.put("Panel.background", new Color(245, 245, 250));
        UIManager.put("Frame.background", Color.white);
        
        UIManager.put("MenuBar.background", Color.white);
        UIManager.put("MenuBar.foreground", Color.black);
        UIManager.put("MenuBar.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));
        UIManager.put("MenuBar.font", chineseFont);
        
        UIManager.put("Menu.background", new Color(255, 220, 240));
        UIManager.put("Menu.foreground", new Color(70, 0, 50));
        UIManager.put("Menu.font", chineseFont);
        
        UIManager.put("MenuItem.background", new Color(255, 230, 245));
        UIManager.put("MenuItem.foreground", new Color(70, 0, 50));
        UIManager.put("MenuItem.font", chineseFont);
        
        // 设置文本框字体
        UIManager.put("TextField.font", chineseFont);
        UIManager.put("TextArea.font", chineseFont);
    }

    private void setAppIcon(JFrame frame, String imagePath) {
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource(imagePath)
            );
            frame.setIconImage(icon);
        } catch (Exception e) {
            System.err.println("无法加载图标: " + imagePath);
            e.printStackTrace();
        }
    }
}

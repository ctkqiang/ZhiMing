package xin.ctkqiang.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import xin.ctkqiang.common.ZhiMingContext;
import xin.ctkqiang.controller.Attack;
import xin.ctkqiang.interfaces.WindowInterface;
import xin.ctkqiang.interfaces.ZhiMing;
import xin.ctkqiang.model.AttackType;
import xin.ctkqiang.model.Platform;
import xin.ctkqiang.model.NetworkData;
import xin.ctkqiang.utilities.FileUtilities;
import xin.ctkqiang.utilities.Logger;

@ZhiMing(debug = true)
public class Window implements WindowInterface {
    private static final Logger logger = new Logger();
    private FileUtilities fileUtilities = new FileUtilities();

    private Attack attack = new Attack();

    private JFrame frame = new JFrame();
    private JMenuBar menubar = new JMenuBar();

    private AttackType attackType = AttackType.TCP80;

    private boolean isUserAcceptTheCnWarning  = false;
    private boolean isAttackBtnTriggered = false;

    private NetworkData networkData = new NetworkData();

    public Window() {
        this.onInit();
    }

    /**
     * 设置并配置目标输入面板，包含URL/IP输入框和开始攻击按钮
     * 
     * @param frame 要添加输入面板的父框架
     * @ZhiMing(debug=true) 表示该方法在调试模式下运行
     * 
     */
    @ZhiMing(debug = true)
    private void setTextField(JFrame frame) {
        JButton actionButton = new JButton("开始攻击");
        JTextField urlOrIp = new JTextField(20);
        JPanel inputPanel = new JPanel(new BorderLayout(20, 0));

        urlOrIp.setPreferredSize(new java.awt.Dimension(300, 30));
        urlOrIp.setText("请输入目标网址或IP地址...");
        urlOrIp.setForeground(Color.gray);
        urlOrIp.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        urlOrIp.addFocusListener(new java.awt.event.FocusAdapter() {
            /**
             * 处理焦点获得事件，当输入框获得焦点且显示默认提示文本时清空内容
             * 
             * @param e 焦点事件对象
             */
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (urlOrIp.getText().equals("请输入目标网址或IP地址...")) {
                    urlOrIp.setText("");
                    urlOrIp.setForeground(Color.BLACK);
                }
            }

            /**
             * 当输入框失去焦点时，如果内容为空则显示默认提示文本
             * 
             * @param e 焦点事件对象，包含焦点变化的相关信息
             */
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (urlOrIp.getText().isEmpty()) {
                    urlOrIp.setText("请输入目标网址或IP地址...");
                    urlOrIp.setForeground(Color.GRAY);
                }
            }
        });
        urlOrIp.addActionListener(new ActionListener() {
            /**
             * 处理按钮点击事件，触发指定按钮的点击动作
             * 
             * @param e 触发的事件对象
             */
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
                
                Color.gray));
        
        actionButton.addActionListener(new ActionListener() {
            /**
             * 处理用户输入的URL或IP地址，检查是否包含敏感关键词
             * 如果包含敏感词，显示安全警告对话框；否则直接开始扫描
             * 
             * @param e 触发此操作的事件对象
            */
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
                
                if (!matches.isEmpty() && !isUserAcceptTheCnWarning) {
                    Alert.showSecurityWarning(
                        new Consumer<String>() {
                            @Override
                            public void accept(String url) {
                                logger.warn("用户已确认，开始扫描: {}", url);

                                isUserAcceptTheCnWarning = true;
                                isAttackBtnTriggered = true;

                                networkData.setHost(text);
                                
                                attack.attack(isAttackBtnTriggered, new Runnable() {
                                    @Override
                                    public void run() {
                                        attack.launch(attackType, networkData);
                                    }
                                });
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
                    if (ZhiMingContext.isDebug()) {
                        logger.info("无匹配关键字，直接开始扫描");
                    }

                    networkData.setHost(text);
                    attack.attack(isAttackBtnTriggered, new Runnable() {
                        @Override
                        public void run() {   
                            attack.launch(attackType, networkData);
                        }
                    });
                }
            }
        });

        
        this.setRadioOptionAttackMode(frame);
        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
    }

    /**
     * 在窗口初始化时执行的操作
     * <p>
     * 该方法会在窗口初始化阶段被调用，用于执行窗口相关的初始化逻辑
     * </p>
     * 
     * @implNote 该方法会调用父类的onInit()方法并记录初始化日志
     */
    @Override
    public void onInit() {
        WindowInterface.super.onInit();
    }

    @Override
    public void onClose() {
        WindowInterface.super.onClose();
    }

    /**
     * 为指定的JFrame设置菜单栏，包含文件菜单和帮助菜单
     * 
     * 文件菜单包含以下功能项：
     * - 保存：记录保存操作日志
     * - 导入密码：从文本文件导入密码
     * - 退出：终止应用程序
     * 
     * 帮助菜单包含：
     * - Issues：在浏览器中打开项目问题页面
     * 
     * @param frame 要设置菜单栏的JFrame窗口
     */
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
                if (ZhiMingContext.isDebug()) {
                    logger.info("保存");
                }
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
                onClose();
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
                    if (ZhiMingContext.isDebug()) {
                        logger.error("无法打开浏览器: {}", ex.getMessage());
                    }
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

    /**
     * 初始化并显示主窗口，设置窗口大小、标题、布局等基本属性
     * 
     * @param width  窗口的宽度（像素）
     * @param height 窗口的高度（像素）
     * @param title  窗口的标题和名称
     * @debug 当处于调试模式时，会记录窗口尺寸日志
     */
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
        
        Font chineseFont = new Font("Microsoft YaHei", Font.PLAIN, 14);
        
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
        
        UIManager.put("TextField.font", chineseFont);
        UIManager.put("TextArea.font", chineseFont);

        if (ZhiMingContext.isDebug()) {
            logger.info("字体与全局界面样式已设置完成");
        }
     }

    /**
     * 为指定的JFrame设置应用程序图标
     * 
     * @param frame     要设置图标的JFrame窗口
     * @param imagePath 图标图片的资源路径
     * @throws Exception 如果无法加载指定路径的图标资源
    */
    private void setAppIcon(JFrame frame, String imagePath) {
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource(imagePath)
            );

            frame.setIconImage(icon);

            if (logger.getPlatform() == Platform.UNIX) {
                try {
                    Class<?> appClass = Class.forName("com.apple.eawt.Application");
                    Object app = appClass.getMethod("getApplication").invoke(null);

                    appClass.getMethod(
                        "setDockIconImage", 
                        Image.class
                    ).invoke(app, icon);
                } catch (Exception ignored) {
                    if (ZhiMingContext.isDebug()) {
                        logger.error("设置Dock图标失败");
                    }
                }
            }

        } catch (Exception e) {
            if(ZhiMingContext.isDebug()) {
                logger.error("无法加载图标: " + imagePath);
            }

            e.printStackTrace();
        } finally {
            if (ZhiMingContext.isDebug()) {
                logger.info("应用图标已加载完成，窗口初始化流程结束");
            }
        }
    }

    private void setRadioOptionAttackMode (JFrame frame) {
        ButtonGroup group = new ButtonGroup();
        JPanel panel = new JPanel(new GridLayout(6, 1));

        panel.setBorder(BorderFactory.createTitledBorder("攻击类型"));
        panel.setBackground(Color.WHITE);
        panel.setForeground(Color.BLACK);
        
        for (AttackType type : AttackType.values()) {
            JRadioButton btn = new JRadioButton(type.toString());
        
            if (type == AttackType.TCP80) btn.setSelected(true);
        
            group.add(btn);
            panel.add(btn);

            btn.setBackground(Color.WHITE);
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    attackType = type;

                    if (ZhiMingContext.isDebug()) {
                        logger.info("攻击类型：" + attackType.toString());
                    }
                    
                    switch (attackType) {
                        case TCP80:
                            networkData.setPort(80);
                            break;
                        case UDP:
                            networkData.setPort(53);
                            break;
                        case FTP:
                            networkData.setPort(21);
                            break;
                        case MYSQL:
                            networkData.setPort(3306);
                            break;
                        case SSH:
                            networkData.setPort(22);
                            break;
                        case SMTP:
                            networkData.setPort(25);
                            break;
                    }

                    attack.launch(attackType, networkData);
                }
            });
        }
        
        frame.add(panel, BorderLayout.WEST);
    }
}

package xin.ctkqiang.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.Highlighter;
import javax.swing.text.Keymap;
import javax.swing.text.Highlighter.Highlight;

import xin.ctkqiang.common.ZhiMingContext;
import xin.ctkqiang.controller.Attack;
import xin.ctkqiang.interfaces.WindowInterface;
import xin.ctkqiang.interfaces.ZhiMing;
import xin.ctkqiang.model.AttackType;
import xin.ctkqiang.model.HttpRequestMethod;
import xin.ctkqiang.model.NetworkData;
import xin.ctkqiang.model.Platform;
import xin.ctkqiang.utilities.FileUtilities;
import xin.ctkqiang.utilities.Logger;

@ZhiMing(debug = true)
public class Window implements WindowInterface {
    private static final Pattern ANSI_PATTERN = Pattern.compile("\u001B\\[[;\\d]*m");

    private static final Logger logger = new Logger();
    private FileUtilities fileUtilities = new FileUtilities();
    

    private Attack attack = new Attack();

    private JFrame frame = new JFrame();
    private JMenuBar menubar = new JMenuBar();
    private JTextArea console = new JTextArea();

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
        JPanel iconPanel = new JPanel();
        JButton actionButton = new JButton("开始攻击");
        JTextField urlOrIp = new JTextField(20);
        JPanel inputPanel = new JPanel(new BorderLayout(20, 0));

        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBorder(BorderFactory.createTitledBorder("请求体(数据)"));
        
        JTextArea requestBody = new JTextArea(3, 20);
        requestBody.setLineWrap(true);
        requestBody.setWrapStyleWord(true);
        
        JScrollPane bodyScroll = new JScrollPane(requestBody);
        bodyScroll.setPreferredSize(new Dimension(400, 80));

        JButton insertCustomBtn = new JButton("插入默认密码列表");
        insertCustomBtn.setToolTipText("点击插入默认密码列表变量 $default_password_list$");
        insertCustomBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String template = "$default_password_list$";
                int pos = requestBody.getCaretPosition();
                requestBody.insert(template, pos);
            }
        });


        JButton clearBtn = new JButton("清空");
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                requestBody.setText("");
            }
        });

        JButton exampleBtn = new JButton("示例");
        exampleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                requestBody.setText("{\n  \"username\": \"admin\",\n  \"password\": \"admin123\"\n}");
            }
        });

        iconPanel.add(insertCustomBtn);
        iconPanel.add(clearBtn);
        iconPanel.add(exampleBtn);

        bodyPanel.add(iconPanel, BorderLayout.NORTH);
        bodyPanel.add(bodyScroll, BorderLayout.CENTER);
      
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createTitledBorder("请求头"));
        
        JTextArea requestHeader = new JTextArea(2, 20);
        requestHeader.setLineWrap(true);
        requestHeader.setWrapStyleWord(true);
        
        requestHeader.setText(
            "User-Agent: Mozilla/5.0\n" +
            "Accept: */*\n" +
            "Accept-Charset: UTF-8"
        );
        
        JScrollPane headerScroll = new JScrollPane(requestHeader);
        headerScroll.setPreferredSize(new Dimension(400, 60));
        headerPanel.add(headerScroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(bodyPanel, BorderLayout.CENTER);
        bottomPanel.add(headerPanel, BorderLayout.SOUTH);

        urlOrIp.setPreferredSize(new Dimension(300, 30));
        urlOrIp.setText("请输入目标网址或IP地址...");
        urlOrIp.setForeground(Color.gray);
        urlOrIp.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
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
        inputPanel.setOpaque(false);
        inputPanel.setToolTipText("请输入要攻击的网址或IP");
        inputPanel.setName("inputPanel");

        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.lightGray, 0),
            "目标坐标",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 12), Color.gray)
        );
        
        actionButton.setPreferredSize(new Dimension(100, 30));
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = urlOrIp.getText().trim();
                
                if (text.isEmpty()) {
                    logger.warn("请输入坐标");
                    return;
                }
                
                List<String> keywords = Arrays.asList(".cn", ".gov.cn", ".xin");
                List<String> matches = new ArrayList<String>();
                for (String keyword : keywords) {
                    if (text.toLowerCase().contains(keyword.toLowerCase())) {
                        matches.add(keyword);
                    }
                }
                
                logger.info(text);
                logger.info("匹配到的关键字：" + matches);
                logger.info("匹配到的关键字数量：" + matches.size());
                
                if (!matches.isEmpty() && !isUserAcceptTheCnWarning) {
                    Alert.showSecurityWarning(
                        new Consumer<String>() {
                            @Override
                            public void accept(String url) {
                                logger.warn("用户已确认，开始扫描: " + url);
                                isUserAcceptTheCnWarning = true;
                                isAttackBtnTriggered = true;
                                networkData.setHost(text);
                                
                                attack.attack(isAttackBtnTriggered, new Runnable() {
                                    @Override
                                    public void run() {
                                        attack.launch(attackType, networkData);
                                    }
                                }, networkData);
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
                    }, networkData);
                }
            }
        });
        
        this.setRadioOptionAttackMode(frame);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.CENTER);
        
        frame.getContentPane().add(mainPanel, BorderLayout.NORTH);
        this.setDropDownMenu(inputPanel);
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
        JMenuItem showPasswordListMenuItem = new JMenuItem("查看密码列表");
        showPasswordListMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Path path = Paths.get(fileUtilities.PASSWORD_FILE_PATH);

                    if (Files.exists(path)) {
                        String content = new String(Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
                        JTextArea textArea = new JTextArea(content);
                        textArea.setEditable(false);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                        
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        scrollPane.setPreferredSize(new Dimension(400, 300));
                        
                        JOptionPane.showMessageDialog(
                            null,
                            scrollPane,
                            "本地密码列表",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(
                            null,
                            "本地密码文件不存在，请先导入",
                            "提示",
                            javax.swing.JOptionPane.WARNING_MESSAGE
                        );
                    }
                } catch (IOException ex) {
                    javax.swing.JOptionPane.showMessageDialog(
                        null,
                        "读取文件失败：" + ex.getMessage(),
                        "错误",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
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

        showPasswordListMenuItem.setMnemonic(KeyEvent.VK_X);
        showPasswordListMenuItem.setActionCommand("显示密码列表");
        showPasswordListMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ZhiMingContext.isDebug()) {
                    logger.info("显示密码列表");
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

        fileMenu.setBackground(Color.WHITE);
        helpMenu.setBackground(Color.WHITE);

        this.menubar.add(fileMenu);
        this.menubar.add(helpMenu);

        helpMenu.add(issueMenuItem);

        fileMenu.add(saveMenuItem);
        fileMenu.add(showPasswordListMenuItem);
        fileMenu.add(importPasswordMenuItem);
        fileMenu.add(exitMenuItem);

        frame.setJMenuBar(menubar);
    }

    private void setConsole(JFrame frame) {
        console.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14)); 
        console.setEditable(false);
        console.setFocusable(true);
        console.setAutoscrolls(true);

        JScrollPane scrollPane = new JScrollPane(
            console, 
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        console.setLineWrap(true); 
        console.setWrapStyleWord(true); 
        
        scrollPane.setPreferredSize(new Dimension(frame.getWidth()-20, 150));
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
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

        this.setConsole(frame);
        frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.getContentPane().setBackground(UIManager.getColor("Frame.background"));
        this.frame.setVisible(true);
        this.frame.setTitle(title);
        this.frame.setName(title);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.getContentPane().setLayout(new BorderLayout());
        this.frame.setBackground(Color.white);

        if (ZhiMingContext.isDebug()) {
            logger.debug("窗口宽度: %s 窗口高度: %s", frame.getWidth(), frame.getHeight());
        }

        UILogBridge.bind(this::showDebugMessageToUI);

        try {
            PrintStream ps = new PrintStream(new OutputStream() {
                private final ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

                @Override
                public void write(int b) {
                    if (b == '\n') {
                        byte[] bytes = byteBuffer.toByteArray();
                        byteBuffer.reset();

                        String line = new String(bytes, StandardCharsets.UTF_8);

                        if (line.endsWith("\r")) {
                            line = line.substring(0, line.length() - 1);
                        }

                        line = stripANSI(line);

                        final String safeLine = line;
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                UILogBridge.log(safeLine);
                            }
                        });

                    } else {
                        byteBuffer.write(b);
                    }
                }
            }, true, StandardCharsets.UTF_8.name());

            System.setOut(ps);
            System.setErr(ps);
        } catch (Exception e) {
            logger.error("无法设置日志输出编码 {}", e.getMessage());
        }
    }

    private void setUIContext() {
        this.setAppIcon(frame, "/assets/appicon.png");
        
        Font chineseFont = new Font("Microsoft YaHei", Font.PLAIN, 14);
        
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.font", chineseFont);
        
        UIManager.put("Label.font", chineseFont);
        UIManager.put("TextField.font", chineseFont);
        
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("Frame.background", Color.WHITE);
        
        UIManager.put("MenuBar.background", Color.WHITE);
        UIManager.put("MenuBar.foreground", Color.BLACK);
        UIManager.put("MenuBar.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));
        UIManager.put("MenuBar.border", Color.BLUE);
        UIManager.put("MenuBar.font", chineseFont);
        
        UIManager.put("Menu.background", Color.WHITE);
        UIManager.put("Menu.foreground", Color.BLACK);
        UIManager.put("Menu.font", chineseFont);
        
        UIManager.put("MenuItem.background", Color.WHITE);
        UIManager.put("MenuItem.foreground", Color.BLACK);
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
            if (ZhiMingContext.isDebug()) {
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

                    attack.launch(attackType, networkData);
                }
            });
        }
        
        frame.add(panel, BorderLayout.WEST);
    }

    private void setDropDownMenu(JPanel inputPanel) {
        JComboBox<HttpRequestMethod> methodDropdown = new JComboBox<>(HttpRequestMethod.values());
        JPanel rightContainer = new JPanel();
        rightContainer.setOpaque(false);
        rightContainer.add(methodDropdown);

        Component oldEast = inputPanel.getComponent(1);

        methodDropdown.setPreferredSize(new Dimension(100, 30));
        methodDropdown.setBackground(Color.WHITE);
        methodDropdown.setForeground(Color.BLACK);
        methodDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                HttpRequestMethod selectedMethod = (HttpRequestMethod) methodDropdown.getSelectedItem();
                networkData.setRequestMethod(selectedMethod);

                if (ZhiMingContext.isDebug()) {
                    logger.info("已选择HTTP请求方法: " + selectedMethod.getValue());
                }

                networkData.setRequestMethod(selectedMethod);
            }
        });

        oldEast.setBackground(Color.WHITE);

        rightContainer.add(oldEast);
        rightContainer.setBackground(Color.WHITE);
        inputPanel.add(rightContainer, BorderLayout.EAST);
    }

    
    private void showDebugMessageToUI(String message) {
        console.append(message + "\n");
        console.setCaretPosition(console.getDocument().getLength());
        console.setForeground(Color.BLACK);
        console.setBackground(Color.WHITE);
    }

    private String stripANSI(String text) {
        return ANSI_PATTERN.matcher(text).replaceAll("");
    }
}
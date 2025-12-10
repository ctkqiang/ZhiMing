package xin.ctkqiang.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import xin.ctkqiang.interfaces.WindowInterface;
import xin.ctkqiang.interfaces.ZhiMing;
import xin.ctkqiang.utilities.FileUtilities;

@ZhiMing(debug = true)
public class Window implements WindowInterface {
    private FileUtilities fileUtilities = new FileUtilities();

    private JFrame frame = new JFrame();
    private JMenuBar menubar = new JMenuBar();

    private void setTextField(JFrame frame) {
        JButton actionButton = new JButton("开始攻击");
        JTextField urlOrIp = new JTextField(20); 
        
        urlOrIp.setBorder(BorderFactory.createCompoundBorder(
            urlOrIp.getBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        inputPanel.add(urlOrIp, BorderLayout.CENTER);
        inputPanel.add(actionButton, BorderLayout.EAST);
        
        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
    }


    private void setMenuBar(JFrame frame) {
        JMenu fileMenu = new JMenu("文件");
        JMenu helpMenu = new JMenu("帮助");

        JMenuItem saveMenuItem = new JMenuItem("保存");
        JMenuItem importPasswordMenuItem = new JMenuItem("导入密码 (.txt)");
        JMenuItem exitMenuItem = new JMenuItem("退出");

        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setActionCommand("保存");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("保存");
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

        helpMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("");
            }
        });

        this.menubar.add(fileMenu);
        this.menubar.add(helpMenu);

        fileMenu.add(saveMenuItem);
        fileMenu.add(importPasswordMenuItem);
        fileMenu.add(exitMenuItem);

        frame.setJMenuBar(menubar);
    }
    
    public void main(int width, int height, String title) {   
        this.setUIContext();
        
        this.setMenuBar(this.frame);
        this.setTextField(this.frame);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(240, 240, 255));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Center Area"));
        frame.add(centerPanel, BorderLayout.CENTER);

        this.frame.getContentPane().setBackground(UIManager.getColor("Frame.background"));

        this.frame.setLayout(null);
        this.frame.setSize(width, height);
        this.frame.setAlwaysOnTop(true);
        this.frame.setForeground(Color.BLACK);
        this.frame.setTitle(title);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);

        System.out.println("Frame width: " + frame.getWidth());
        System.out.println("Frame height: " + frame.getHeight());
    }   

    private void setUIContext() {
        this.setAppIcon(frame, "/assets/appicon.png");
        
        UIManager.put("Button.background", new Color(255, 200, 230));
        UIManager.put("Button.foreground", Color.black);
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Panel.background", Color.white);
        UIManager.put("Frame.background", Color.white);
        UIManager.put("MenuBar.background", Color.white);
        UIManager.put("MenuBar.foreground", Color.black);
        UIManager.put("MenuBar.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));
        UIManager.put("Menu.background", Color.white);
        UIManager.put("Menu.foreground", Color.black);
        UIManager.put("MenuItem.background", Color.white);
        UIManager.put("MenuItem.foreground", Color.black);
    }

    private void setAppIcon(JFrame frame, String imagePath) {
        Image icon  = Toolkit.getDefaultToolkit().getImage(
            Window.class.getResource(imagePath)
        );
        frame.setIconImage(icon);
    }
}

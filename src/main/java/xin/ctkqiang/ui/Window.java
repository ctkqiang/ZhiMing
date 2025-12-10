package xin.ctkqiang.ui;

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
        JPanel panel = new JPanel();
        JTextField urlOrIp = new JTextField();

        urlOrIp = new JTextField();
        urlOrIp.setBounds(20, 20, frame.getWidth() - 240, 30);
        urlOrIp.setText("");
        
        actionButton.setBounds(800, 20, 180, 30);
        
        panel.setLayout(null);
        panel.add(urlOrIp);
        panel.add(actionButton);
        panel.setBounds(0, 0, frame.getWidth(), 80);
        
        frame.add(panel);
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
                try {
                    Desktop.getDesktop().browse(URI.create("https://github.com/ctkqiang/ZhiMing/issues"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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

        this.frame.getContentPane().setBackground(UIManager.getColor("Frame.background"));

        this.frame.setSize(width, height);
        this.frame.setTitle(title);
        this.frame.setLayout(null);
        this.frame.setBackground(Color.white);
        this.frame.setVisible(true);
    }   

    private void setUIContext() {
        this.setAppIcon(frame, "/assets/appicon.png");
        
        UIManager.put("Button.background", new Color(255, 200, 230));
        UIManager.put("Button.foreground", new Color(80, 0, 40));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Panel.background", new Color(245, 245, 250));
        UIManager.put("Frame.background", Color.white);
        UIManager.put("MenuBar.background", Color.white);
        UIManager.put("MenuBar.foreground", Color.white);
        UIManager.put("MenuBar.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));
        UIManager.put("Menu.background", new Color(255, 220, 240));
        UIManager.put("Menu.foreground", new Color(70, 0, 50));
        UIManager.put("MenuItem.background", new Color(255, 230, 245));
        UIManager.put("MenuItem.foreground", new Color(70, 0, 50));
    }

    private void setAppIcon(JFrame frame, String imagePath) {
        Image icon  = Toolkit.getDefaultToolkit().getImage(
            Window.class.getResource(imagePath)
        );
        frame.setIconImage(icon);
    }
}

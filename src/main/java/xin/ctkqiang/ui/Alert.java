package xin.ctkqiang.ui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class Alert {
    
    public static void showSecurityWarning(Consumer<String> onContinue, Runnable onCancel) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createSecurityAlertDialog(onContinue, onCancel);
            }
        });
    }
    
    private static void createSecurityAlertDialog(Consumer<String> onContinue, Runnable onCancel) {
        String title = "作者声明";
        
        String longText = "========================================================\n" +
                          "                  作者声明 \n" +
                          "========================================================\n\n" +
                          "您确定要扫攻击网站吗？\n\n" +
                          "==================== 中国网络安全法警告 ====================\n\n" +
                          "根据《中华人民共和国网络安全法》(2017年6月1日施行):\n\n" +
                          "第二十一条: 国家实行网络安全等级保护制度。网络运营者应当按照网络安全等级保护制度的要求，履行下列安全保护义务：\n" +
                          "(一) 制定内部安全管理制度和操作规程，确定网络安全负责人；\n" +
                          "(二) 采取防范计算机病毒和网络攻击、网络侵入等危害网络安全行为的技术措施；\n" +
                          "(三) 采取监测、记录网络运行状态、网络安全事件的技术措施，并按照规定留存相关的网络日志不少于六个月；\n" +
                          "(四) 采取数据分类、重要数据备份和加密等措施；\n" +
                          "(五) 法律、行政法规规定的其他义务。\n\n" +
                          "第二十七条: 任何个人和组织不得从事非法侵入他人网络、干扰他人网络正常功能、\n" +
                          "窃取网络数据等危害网络安全的活动；不得提供专门用于从事危害网络安全活动的程序、工具；\n" +
                          "明知他人从事危害网络安全的活动的，不得为其提供技术支持、广告推广、支付结算等帮助。\n\n" +
                          "第四十四条: 任何个人和组织不得窃取或者以其他非法方式获取个人信息，\n" +
                          "不得非法出售或者非法向他人提供个人信息。\n\n" +
                          "第四十六条: 任何个人和组织应当对其使用网络的行为负责，不得设立用于实施诈骗，\n" +
                          "传授犯罪方法，制作或者销售违禁物品、管制物品等违法犯罪活动的网站、通讯群组，\n" +
                          "不得利用网络发布涉及实施诈骗，制作或者销售违禁物品、管制物品\n" +
                          "以及其他违法犯罪活动的信息。\n\n" +
                          "==================== 相关法规条款 ====================\n\n" +
                          "《中华人民共和国数据安全法》(2021年9月1日施行):\n" +
                          "第三十二条: 任何组织、个人收集数据，应当采取合法、正当的方式，不得窃取\n" +
                          "或者以其他非法方式获取数据。\n\n" +
                          "《中华人民共和国个人信息保护法》(2021年11月1日施行):\n" +
                          "第十条: 任何组织、个人不得非法收集、使用、加工、传输他人个人信息，\n" +
                          "不得非法买卖、提供或者公开他人个人信息。\n\n" +
                          "《刑法》第二百八十五条【非法侵入计算机信息系统罪】:\n" +
                          "违反国家规定，侵入国家事务、国防建设、尖端科学技术领域的计算机信息系统的，\n" +
                          "处三年以下有期徒刑或者拘役。\n\n" +
                          "《刑法》第二百八十六条【破坏计算机信息系统罪】:\n" +
                          "违反国家规定，对计算机信息系统功能进行删除、修改、增加、干扰，\n" +
                          "造成计算机信息系统不能正常运行，后果严重的，处五年以下有期徒刑或者拘役；\n" +
                          "后果特别严重的，处五年以上有期徒刑。\n\n" +
                          "==================== 重要警告 ====================\n\n" +
                          "⚠️ 警告：未经授权的网络扫描可能违反以下法律条款：\n" +
                          "1. 非法侵入计算机信息系统\n" +
                          "2. 非法获取网络数据\n" +
                          "3. 危害网络安全\n" +
                          "4. 侵犯他人合法权益\n\n" +
                          "⚠️ 法律后果可能包括：\n" +
                          "• 行政处罚：警告、罚款、没收违法所得\n" +
                          "• 刑事处罚：拘役、有期徒刑\n" +
                          "• 民事责任：赔偿损失\n" +
                          "• 列入失信名单\n\n" +
                          "==================== 免责声明 ====================\n\n" +
                          "【作者正式声明 - AUTHOR'S FORMAL DECLARATION】\n\n" +
                          "如果您执意继续扫描操作：\n\n" +
                          "1. 您必须确保您的扫描行为符合：\n" +
                          "   • 《中华人民共和国网络安全法》\n" +
                          "   • 《中华人民共和国数据安全法》 \n" +
                          "   • 《中华人民共和国个人信息保护法》\n" +
                          "   • 相关地方法规和行政规章\n\n" +
                          "2. 您必须获得目标网站的明确书面授权\n\n" +
                          "3. 您必须遵守目标网站的服务条款和使用协议\n\n" +
                          "4. 您必须确保扫描行为不：\n" +
                          "   • 干扰目标网站的正常运行\n" +
                          "   • 获取未经授权的个人信息\n" +
                          "   • 破坏网络安全防护措施\n" +
                          "   • 违反任何适用的法律法规\n\n" +
                          "⚠️【作者免责条款】⚠️\n" +
                          "如果您执意继续，本工具作者不承担任何责任：\n" +
                          "• 本工具作者对您的扫描行为不承担任何法律责任\n" +
                          "• 本工具作者不对扫描可能造成的任何后果负责\n" +
                          "• 所有法律后果由扫描操作者自行承担\n" +
                          "• 使用本工具即表示您同意承担全部责任\n\n" +
                          "请确认您已阅读并理解以上所有法律条款和警告信息。\n" +
                          "========================================================\n";
        
        UIManager.put("Button.background", new Color(255, 200, 230));
        UIManager.put("Button.foreground", Color.black);
        
        JTextArea textArea = new JTextArea(longText);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JButton understandButton = new JButton("我理解并同意继续");
        JButton cancelButton = new JButton("取消扫描");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        JDialog dialog = new JDialog((Frame) null, title, true);

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        scrollPane.setPreferredSize(new Dimension(700, 400));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        understandButton.setForeground(new Color(0, 180, 0));
        understandButton.setBackground(Color.WHITE);
        understandButton.setFont(new Font("Microsoft YaHei", Font.BOLD, 12));
        
        cancelButton.setForeground(Color.RED);
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setFont(new Font("Microsoft YaHei", Font.BOLD, 12));

        buttonPanel.add(cancelButton);
        buttonPanel.add(understandButton);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        final String[] targetUrl = {null};
        
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int confirm = JOptionPane.showConfirmDialog(
                    dialog,
                    "您确认要退出攻击吗？",
                    "确认退出",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    if (onCancel != null) {
                        onCancel.run();
                    }
                    dialog.dispose();
                }
            }
        });
        
        understandButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    dialog,
                    "您确认已理解所有法律风险并同意继续攻击吗？",
                    "最终确认",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    dialog.dispose();
                    if (onContinue != null) {
                        onContinue.accept(targetUrl[0]);
                    }
                }
            }
        });
        
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dialog.dispose();
                if (onCancel != null) {
                    onCancel.run();
                }
            }
        });
        
        dialog.setContentPane(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
package xin.ctkqiang.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.hc.core5.http.HttpException;

import xin.ctkqiang.common.ZhiMingContext;
import xin.ctkqiang.constant.HttpStatusCode;
import xin.ctkqiang.interfaces.AttackInterface;
import xin.ctkqiang.interfaces.ZhiMing;
import xin.ctkqiang.model.AttackType;
import xin.ctkqiang.model.NetworkData;
import xin.ctkqiang.utilities.Logger;

@ZhiMing(debug = true)
public class Attack implements AttackInterface {
    private static final Logger logger = new Logger();

    @Override
    public void attack(boolean isTriggered, Runnable runnable, NetworkData networkData) {
        AttackInterface.super.attack(isTriggered, runnable, networkData);
    }

    @Override
    public void onStop(boolean isTriggered) {
        AttackInterface.super.onStop(isTriggered);
    }

    public void launch(AttackType attackType, NetworkData object) {
        NetworkData networkData = (NetworkData) object;

        if  (ZhiMingContext.isDebug()) {
            logger.info("NetworkData: " + networkData.toString());
        }
        
        switch (attackType) {
            case TCP80:        
                try {
                    try {
                        tcpAttack(
                            networkData.getHost(), 
                            networkData.getPort() != 0 ? networkData.getPort() : 0,
                            networkData.getRequestMethod()
                        );
                    } catch (HttpException e) {
                        if (ZhiMingContext.isDebug()) {    
                            logger.error("HTTP 异常：{}" +  e.getMessage());
                        }
                    }
                } catch (IOException e) {
                    if (ZhiMingContext.isDebug()) {
                        logger.error("TCP攻击失败：{}" +  e.getMessage());
                    }
                }
                break;
            case FTP:
            case MYSQL:
            case SSH:
            case UDP:
            case SMTP:
                break;
            default:
                break;
        }
    } 

    public void tcpAttack(String ip, int port, String method) throws IOException, HttpException {
        String inputLine;
        StringBuilder response = new StringBuilder();

        String host = ip + ((port != 0) ? ":" + port : "");

        try {
            URL url = new URL(host);

            if (ZhiMingContext.isDebug()) {
                logger.info("TCP攻击："+ url + "\n");
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod(method);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Accept", "*/*");

            int responseCode = conn.getResponseCode();

            logger.debug("=".repeat(100));
            logger.debug("攻击结果状态码：" + responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                
                String body = response.toString();
                logger.debug("响应内容: ");

                System.out.println("\u001B[32m " + body);

                this.getForm(body);
            }

            logger.info("响应状态码：" + HttpStatusCode.getMessage(responseCode));
            logger.debug("响应状态消息：" + conn.getResponseMessage());
            
        } catch (MalformedURLException e) {
            logger.error("URL错误：" + e.getMessage());
        }
    }

    public void udpAttack(String ip, int port) {

    }   

    public void ftpAttack(String ip) {

    }

    public void icmpAttack(String ip) {

    }

    public void dnsAttack(String ip) {

    }

    public void sqlInjection(String host, Runnable rrunnable) {

    }

    private void getForm(String html) {
        if (html == null || html.isEmpty()) {
            logger.debug("HTML内容为空");
            return;
        }
        
        String lowerHtml = html.toLowerCase();
        int formStart = lowerHtml.indexOf("<form");
        
        if (formStart == -1) {
            logger.debug("未检测到表单标签");
            return;
        }
        
        int formEnd = lowerHtml.indexOf("</form>", formStart);
        if (formEnd == -1) {
            logger.debug("表单标签未闭合");
            formEnd = html.length();
        }
        
        String formHtml = html.substring(formStart, formEnd + 7);
        
        // 直接打印完整的form标签内容
        logger.debug("=".repeat(100));
        logger.debug("表单HTML:");
        logger.debug(formHtml);
        
        // 提取并打印action属性
        String action = extractAttribute(formHtml, "action");
        logger.debug("表单action: " + (action == null ? "未指定" : action));

        String php = extractAttribute(formHtml, ".php");
        logger.debug("=".repeat(100));
        System.out.println ("PHP action: " + (php == null ? "未指定" :php));
    }

    private String extractAttribute(String tag, String attr) {
        int attrIndex = tag.indexOf(attr + "=\"");
        if (attrIndex == -1) return null;
        
        attrIndex += attr.length() + 2; // attr=" 的长度
        int endIndex = tag.indexOf("\"", attrIndex);
        if (endIndex == -1) return null;
        
        return tag.substring(attrIndex, endIndex);
    }
}
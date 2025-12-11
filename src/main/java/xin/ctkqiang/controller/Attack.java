package xin.ctkqiang.controller;

import xin.ctkqiang.common.ZhiMingContext;
import xin.ctkqiang.interfaces.AttackInterface;
import xin.ctkqiang.interfaces.ZhiMing;
import xin.ctkqiang.model.AttackType;
import xin.ctkqiang.model.NetworkData;
import xin.ctkqiang.utilities.Logger;

@ZhiMing(debug = true)
public class Attack implements AttackInterface {
    private static final Logger logger = new Logger();

    @Override
    public void attack(boolean isTriggered, Runnable runnable) {
        AttackInterface.super.attack(isTriggered, runnable);
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
                tcpAttack(networkData.getHost(), networkData.getPort());
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

    public void tcpAttack(String ip, int port) {
        port = 80;
        
        if (ZhiMingContext.isDebug()) {
            logger.info("TCP攻击：" + ip + ":" + port);
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
}

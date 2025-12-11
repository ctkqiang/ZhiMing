package xin.ctkqiang.controller;

import xin.ctkqiang.interfaces.AttackInterface;
import xin.ctkqiang.utilities.Logger;

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

    public void httpAttack(String url) {

    } 

    public void tcpAttack(String ip, int port) {

    }

    public void udpAttack(String ip, int port) {

    }   

    public void icmpAttack(String ip) {

    }

    public void dnsAttack(String ip) {

    }

    public void sqlInjection(String host, Runnable rrunnable) {
        
    }
}

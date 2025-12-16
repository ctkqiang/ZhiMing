package xin.ctkqiang.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import xin.ctkqiang.interfaces.HydraInterface;
import xin.ctkqiang.utilities.Logger;

public class Hydra implements HydraInterface{
    private static final Logger logger = new Logger();

    public boolean provideUserFile;
    public boolean providePasswordFile;

    public String filePathUser;
    public String filePathPassword;
    public String F;

    public String user;
    public String password;

    public Hydra() { }

    public String getF() {
        return F;
    }

    public String getUserFile() {
        return filePathUser;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordFile() {
        return filePathPassword;
    }

    public boolean isProvideUserFile() {
        return provideUserFile;
    }

    public boolean isProvidePasswordFile() {
        return providePasswordFile;
    }

    public void setF(String F) {
        this.F = F;
    } 

    public void setUserFile(String filePathUser) {
        this.filePathUser = filePathUser;
    }

    public void setPasswordFile(String filePathPassword) {
        this.filePathPassword = filePathPassword;
    }

    public void setProvideUserFile(boolean  doProvideUserFile) {
        this.provideUserFile = doProvideUserFile;
    }

    public void setProvidePasswordFile(boolean  doProvidePasswordFile) {
        this.providePasswordFile = doProvidePasswordFile;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void onInit() {
        logger.info("Hydra 模块初始化完成");
    }

    @Override
    public void onClose() {
        logger.info("Hydra 模块关闭");
    }

    @Override
    public void bruteForceHttpPostForm(String url, int port, int threads, int timeout) {
        logger.info("开始进行 HTTP POST 暴力破解");

        String cmd, line; 

        // hydra -L user.txt  -P rockyou.txt -f -t 1 -s 6022 -W 100000 -w 75  -vV 'http-post-form://urllogin:email=^USER^&password=^PASS^:F=用户名或密码错误'
        if (providePasswordFile && provideUserFile) {
            cmd = "hydra -L " + filePathUser + "  -P " + filePathPassword + " -f -t " + threads + " -s " + port + " -W 100000 -w 75  -vV 'http-post-form://" + url + ":email=^USER^&password=^PASS^:F=用户名或密码错误'";
        }

        else if (providePasswordFile && !provideUserFile) {
            cmd = "hydra -l " + getUser() + "-P " + filePathPassword + " -f -t " + threads + " -s " + port + " -W 100000 -w 75  -vV 'http-post-form://" + url + ":email=^USER^&password=^PASS^:F=用户名或密码错误'";
        } 

        else if (!providePasswordFile && provideUserFile) {
            cmd = "hydra -u -L " + filePathUser + " -p " + getPassword() + " -f -t " + threads + " -s " + port + " -W 100000 -w 75  -vV 'http-post-form://" + url + ":email=^USER^&password=^PASS^:F=用户名或密码错误'";
        }

        else {
            cmd = "hydra --version";
        }

        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            logger.info("Hydra 输出");

            while ((line = reader.readLine()) != null) System.out.println(line);
            int exitCode = process.waitFor();

            logger.info("Hydra 执行完成，退出码: " + exitCode);

        } catch (IOException  | InterruptedException e) {
            logger.error("执行 Hydra 命令时出错：{}", e.getMessage());
        }
    }
}

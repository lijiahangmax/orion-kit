package com.orion.ftp.server;

import com.orion.constant.Const;

import java.io.Serializable;

/**
 * FTP 连接配置信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/10 18:47
 */
public class FtpServerConfig implements Serializable {

    /**
     * 最大并发用户数量
     */
    private int maxLogin;

    /**
     * 是否可以匿名登陆
     */
    private boolean anonymousLogin;

    /**
     * 可匿名登陆的最大用户数
     */
    private int maxAnonymousLogin;

    /**
     * 登陆失败最大次数
     */
    private int maxLoginFailures;

    /**
     * 登陆失败延迟时间
     */
    private int loginFailureDelay;

    /**
     * 允许服务器创建以处理客户端请求的最大线程数
     */
    private int maxThreads;

    public FtpServerConfig() {
        this.maxLogin = 10;
        this.anonymousLogin = false;
        this.maxAnonymousLogin = 0;
        this.maxLoginFailures = 3;
        this.loginFailureDelay = Const.MS_S_1;
        this.maxThreads = 5;
    }

    public FtpServerConfig setMaxLogin(int maxLogin) {
        this.maxLogin = maxLogin;
        return this;
    }

    public FtpServerConfig setAnonymousLogin(boolean anonymousLogin) {
        this.anonymousLogin = anonymousLogin;
        return this;
    }

    public FtpServerConfig setMaxAnonymousLogin(int maxAnonymousLogin) {
        this.maxAnonymousLogin = maxAnonymousLogin;
        return this;
    }

    public FtpServerConfig setMaxLoginFailures(int maxLoginFailures) {
        this.maxLoginFailures = maxLoginFailures;
        return this;
    }

    public FtpServerConfig setLoginFailureDelay(int loginFailureDelay) {
        this.loginFailureDelay = loginFailureDelay;
        return this;
    }

    public FtpServerConfig setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
        return this;
    }

    public int getMaxLogin() {
        return maxLogin;
    }

    public boolean isAnonymousLogin() {
        return anonymousLogin;
    }

    public int getMaxAnonymousLogin() {
        return maxAnonymousLogin;
    }

    public int getMaxLoginFailures() {
        return maxLoginFailures;
    }

    public int getLoginFailureDelay() {
        return loginFailureDelay;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

}

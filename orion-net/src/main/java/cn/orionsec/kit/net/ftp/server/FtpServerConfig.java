/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.net.ftp.server;

import cn.orionsec.kit.lang.constant.Const;

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

    public void setMaxLogin(int maxLogin) {
        this.maxLogin = maxLogin;
    }

    public void setAnonymousLogin(boolean anonymousLogin) {
        this.anonymousLogin = anonymousLogin;
    }

    public void setMaxAnonymousLogin(int maxAnonymousLogin) {
        this.maxAnonymousLogin = maxAnonymousLogin;
    }

    public void setMaxLoginFailures(int maxLoginFailures) {
        this.maxLoginFailures = maxLoginFailures;
    }

    public void setLoginFailureDelay(int loginFailureDelay) {
        this.loginFailureDelay = loginFailureDelay;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
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

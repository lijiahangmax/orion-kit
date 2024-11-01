/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.net.ftp.client.config;

import cn.orionsec.kit.lang.constant.StandardTlsVersion;

import javax.net.ssl.SSLSocketFactory;

/**
 * FTP ssl 连接配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/11 17:09
 */
public class FtpsConfig extends FtpConfig {

    /**
     * ssl 协议
     */
    private String protocol;

    /**
     * 是否为隐式ssl
     */
    private boolean implicit;

    /**
     * 管道保护级别
     * C-清除
     * S-安全 (仅SSL协议)
     * E-机密 (仅SSL协议)
     * P-私人
     */
    private String protect;

    private SSLSocketFactory socketFactory;

    public FtpsConfig(String host) {
        this(host, 21);
    }

    public FtpsConfig(String host, int port) {
        super(host, port);
        this.protocol = StandardTlsVersion.TLS;
        this.implicit = false;
        this.protect = "P";
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setImplicit(boolean implicit) {
        this.implicit = implicit;
    }

    public void setProtect(String protect) {
        this.protect = protect;
    }

    public void setSocketFactory(SSLSocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    public String getProtocol() {
        return protocol;
    }

    public boolean isImplicit() {
        return implicit;
    }

    public String getProtect() {
        return protect;
    }

    public SSLSocketFactory getSocketFactory() {
        return socketFactory;
    }

}

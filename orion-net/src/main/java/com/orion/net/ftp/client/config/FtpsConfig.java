package com.orion.net.ftp.client.config;

import com.orion.constant.StandardTlsVersion;

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
     * S-安全（仅SSL协议）
     * E-机密（仅SSL协议）
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

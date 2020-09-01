package com.orion.mail;

/**
 * 服务器配置类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/16 16:06
 */
public enum MailServer {

    /**
     * 网易
     */
    WY163("smtp.163.com", 465),

    /**
     * QQ
     */
    QQ("smtp.qq.com", 465),

    /**
     * 移动
     */
    YD139("smtp.139.com", 465);

    private String smtpHost;
    private Integer smtpPort;

    MailServer(String smtpHost, Integer smtpPort) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

}


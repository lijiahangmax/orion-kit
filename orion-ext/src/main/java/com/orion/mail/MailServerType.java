package com.orion.mail;

/**
 * SMTP 邮件服务器类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/16 16:06
 */
public enum MailServerType implements MailServerProvider {

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

    private final String host;

    private final int port;

    MailServerType(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

}


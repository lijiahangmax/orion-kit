package com.orion.ext.mail;

import com.orion.ext.KitExtConfiguration;
import com.orion.lang.config.KitConfig;

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
    YD139("smtp.139.com", 465),

    /**
     * 自定义
     *
     * @see com.orion.ext.KitExtConfiguration#MAIL_CUSTOMER_HOST
     * @see com.orion.ext.KitExtConfiguration#MAIL_CUSTOMER_PORT
     */
    CUSTOMER(KitConfig.get(KitExtConfiguration.CONFIG.MAIL_CUSTOMER_HOST),
            KitConfig.get(KitExtConfiguration.CONFIG.MAIL_CUSTOMER_PORT)),
    ;

    private final String host;

    private final Integer port;

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


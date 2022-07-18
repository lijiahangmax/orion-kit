package com.orion.ext.mail;

import java.io.Serializable;

/**
 * SMTP 提供者
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/3 15:28
 */
public interface MailServerProvider extends Serializable {

    /**
     * 获取服务主机
     *
     * @return host
     */
    String getHost();

    /**
     * 获取服务端口
     *
     * @return port
     */
    int getPort();

}

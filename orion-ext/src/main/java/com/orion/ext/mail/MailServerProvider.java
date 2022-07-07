package com.orion.ext.mail;

import java.io.Serializable;

/**
 * smtp 提供者
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/3 15:28
 */
public interface MailServerProvider extends Serializable {

    /**
     * @return host
     */
    String getHost();

    /**
     * @return port
     */
    int getPort();

}

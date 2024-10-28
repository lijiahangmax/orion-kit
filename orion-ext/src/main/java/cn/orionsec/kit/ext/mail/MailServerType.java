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
package cn.orionsec.kit.ext.mail;

import cn.orionsec.kit.ext.KitExtConfiguration;
import cn.orionsec.kit.lang.config.KitConfig;

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
     * @see KitExtConfiguration#MAIL_CUSTOMER_HOST
     * @see KitExtConfiguration#MAIL_CUSTOMER_PORT
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


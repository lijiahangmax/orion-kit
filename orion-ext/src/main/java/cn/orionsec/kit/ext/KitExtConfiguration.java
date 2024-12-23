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
package cn.orionsec.kit.ext;

import cn.orionsec.kit.lang.config.KitConfig;
import cn.orionsec.kit.lang.utils.Strings;

/**
 * orion-ext 配置初始化器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/6 18:31
 */
public final class KitExtConfiguration {

    public static final KitExtConfiguration CONFIG = new KitExtConfiguration();

    public final String LOCATION_UNKNOWN = "location.address.unknown";

    public final String MAIL_CUSTOMER_HOST = "mail.customer.host";

    public final String MAIL_CUSTOMER_PORT = "mail.customer.port";

    static {
        KitConfig.init(CONFIG.LOCATION_UNKNOWN, "未知");
        KitConfig.init(CONFIG.MAIL_CUSTOMER_HOST, Strings.EMPTY);
        KitConfig.init(CONFIG.MAIL_CUSTOMER_PORT, 465);
    }

    private KitExtConfiguration() {
    }

}

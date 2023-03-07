package com.orion.ext;

import com.orion.lang.constant.KitConfig;

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
    }

}

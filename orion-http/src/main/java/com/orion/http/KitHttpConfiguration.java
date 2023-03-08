package com.orion.http;

import com.orion.http.useragent.StandardUserAgent;
import com.orion.lang.constant.KitConfig;

/**
 * orion-http 配置初始化器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/8 17:33
 */
public final class KitHttpConfiguration {

    public static final KitHttpConfiguration CONFIG = new KitHttpConfiguration();

    public final String HTTP_DEFAULT_USERAGENT = "http.default.useragent";

    static {
        KitConfig.init(CONFIG.HTTP_DEFAULT_USERAGENT, StandardUserAgent.CHROME_4);
    }

    private KitHttpConfiguration() {
    }

}

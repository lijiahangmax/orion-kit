package com.orion.ext.location;

import com.orion.ext.KitExtConfiguration;
import com.orion.lang.config.KitConfig;

/**
 * 地址常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/7 17:48
 */
public interface LocationConst {

    String CZ88_NET = "CZ88.NET";

    String UNKNOWN = KitConfig.get(KitExtConfiguration.CONFIG.LOCATION_UNKNOWN);

}

package com.orion.office;

import com.orion.lang.constant.Const;
import com.orion.lang.constant.KitConfig;

/**
 * orion-office 配置初始化器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/8 18:25
 */
public class KitOfficeConfiguration {

    public static final KitOfficeConfiguration CONFIG = new KitOfficeConfiguration();

    public final String EXCEL_DEFAULT_AUTHOR = "office.excel.default.author";

    public final String EXCEL_DEFAULT_APPLICATION = "office.excel.default.application";

    static {
        KitConfig.init(CONFIG.EXCEL_DEFAULT_AUTHOR, Const.ORION_KIT);
        KitConfig.init(CONFIG.EXCEL_DEFAULT_APPLICATION, Const.ORION_KIT);
    }

    private KitOfficeConfiguration() {
    }

}

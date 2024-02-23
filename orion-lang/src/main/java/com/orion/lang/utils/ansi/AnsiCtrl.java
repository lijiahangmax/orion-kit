package com.orion.lang.utils.ansi;

import static com.orion.lang.utils.ansi.AnsiConst.CSI_PREFIX;
import static com.orion.lang.utils.ansi.AnsiConst.ESC;

/**
 * ANSI 控制字符
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/29 15:04
 */
public enum AnsiCtrl implements AnsiElement {

    /**
     * 退格
     */
    BS("\b"),

    /**
     * \t
     */
    HT("\t"),

    /**
     * \r
     */
    CR("\r"),

    /**
     * \n
     */
    LF("\n"),

    /**
     * 开启隐私模式
     */
    START_PRIVACY_MESSAGE(ESC + "^"),

    /**
     * 插入模式
     */
    INSERT_MODE(CSI_PREFIX + "4h"),

    /**
     * 替换模式
     */
    REPLACE_MODE(CSI_PREFIX + "4l"),

    ;

    private final String code;

    AnsiCtrl(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

}

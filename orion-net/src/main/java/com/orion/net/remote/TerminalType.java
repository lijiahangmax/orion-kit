package com.orion.net.remote;

import com.orion.utils.Valid;

/**
 * 终端类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/26 17:40
 */
public enum TerminalType {

    /**
     * xterm
     */
    XTERM("xterm"),

    /**
     * xterm-16color
     */
    XTERM_16_COLOR("xterm-16color"),

    /**
     * xterm-256color
     */
    XTERM_256_COLOR("xterm-256color"),

    /**
     * bash
     */
    BASH("bash"),

    /**
     * vt100
     */
    VT_100("vt100"),

    /**
     * vt102
     */
    VT_102("vt102"),

    /**
     * vt220
     */
    VT_220("vt220"),

    /**
     * vt320
     */
    VT_320("vt320"),

    /**
     * linux
     */
    LINUX("linux"),

    /**
     * ansi
     */
    ANSI("ansi"),

    /**
     * dumb
     */
    DUMB("dumb"),

    /**
     * scoansi
     */
    SCO_ANSI("scoansi");

    TerminalType(String type) {
        this.type = type;
    }

    private final String type;

    public String getType() {
        return type;
    }

    public static TerminalType of(String type) {
        Valid.notBlank(type, "terminal type is blank");
        for (TerminalType value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}

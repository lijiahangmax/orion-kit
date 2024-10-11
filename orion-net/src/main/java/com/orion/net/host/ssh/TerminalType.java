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
package com.orion.net.host.ssh;

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

    private final String type;

    TerminalType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static TerminalType of(String type) {
        if (type == null) {
            return XTERM;
        }
        for (TerminalType value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return XTERM;
    }

}

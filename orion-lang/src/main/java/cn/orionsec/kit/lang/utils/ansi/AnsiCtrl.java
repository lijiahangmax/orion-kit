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
package cn.orionsec.kit.lang.utils.ansi;

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
    START_PRIVACY_MESSAGE(AnsiConst.ESC + "^"),

    /**
     * 插入模式
     */
    INSERT_MODE(AnsiConst.CSI_PREFIX + "4h"),

    /**
     * 替换模式
     */
    REPLACE_MODE(AnsiConst.CSI_PREFIX + "4l"),

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

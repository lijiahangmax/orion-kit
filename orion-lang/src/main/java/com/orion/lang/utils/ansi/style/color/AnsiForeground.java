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
package com.orion.lang.utils.ansi.style.color;

import com.orion.lang.constant.Const;

import static com.orion.lang.utils.ansi.AnsiConst.CSI_PREFIX;
import static com.orion.lang.utils.ansi.AnsiConst.SGR_SUFFIX;

/**
 * ANSI 前景色
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/20 10:51
 */
public enum AnsiForeground implements AnsiColor {

    /**
     * 黑色
     */
    BLACK(30),

    /**
     * 红色
     */
    RED(31),

    /**
     * 绿色
     */
    GREEN(32),

    /**
     * 黄色
     */
    YELLOW(33),

    /**
     * 蓝色
     */
    BLUE(34),

    /**
     * 紫色
     */
    PURPLE(35),

    /**
     * 青色
     */
    CYAN(36),

    /**
     * 白色
     */
    WHITE(37),

    /**
     * 默认
     */
    DEFAULT(39),


    // -------------------- 亮色 --------------------

    /**
     * 亮黑色 (灰)
     */
    BRIGHT_BLACK(90),

    /**
     * 亮红色
     */
    BRIGHT_RED(91),

    /**
     * 亮绿色
     */
    BRIGHT_GREEN(92),

    /**
     * 亮黄色
     */
    BRIGHT_YELLOW(93),

    /**
     * 亮蓝色
     */
    BRIGHT_BLUE(94),

    /**
     * 亮紫色
     */
    BRIGHT_PURPLE(95),

    /**
     * 亮青色
     */
    BRIGHT_CYAN(96),

    /**
     * 亮白色
     */
    BRIGHT_WHITE(97),

    ;

    /**
     * 颜色码
     */
    public final int code;

    AnsiForeground(int code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code + Const.EMPTY;
    }

    @Override
    public String toString() {
        return CSI_PREFIX + code + SGR_SUFFIX;
    }

}

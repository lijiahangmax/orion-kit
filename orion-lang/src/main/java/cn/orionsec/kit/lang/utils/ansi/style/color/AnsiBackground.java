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
package cn.orionsec.kit.lang.utils.ansi.style.color;

import cn.orionsec.kit.lang.constant.Const;

import static cn.orionsec.kit.lang.utils.ansi.AnsiConst.CSI_PREFIX;
import static cn.orionsec.kit.lang.utils.ansi.AnsiConst.SGR_SUFFIX;

/**
 * ANSI 背景色
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/20 10:51
 */
public enum AnsiBackground implements AnsiColor {

    /**
     * 黑色
     */
    BLACK(40),

    /**
     * 红色
     */
    RED(41),

    /**
     * 绿色
     */
    GREEN(42),

    /**
     * 黄色
     */
    YELLOW(43),

    /**
     * 蓝色
     */
    BLUE(44),

    /**
     * 紫色
     */
    PURPLE(45),

    /**
     * 青色
     */
    CYAN(46),

    /**
     * 白色
     */
    WHITE(47),

    /**
     * 默认
     */
    DEFAULT(49),

    // -------------------- 亮色 --------------------

    /**
     * 亮黑色 (灰)
     */
    GLOSS_BLACK(100),

    /**
     * 亮红色
     */
    GLOSS_RED(101),

    /**
     * 亮绿色
     */
    GLOSS_GREEN(102),

    /**
     * 亮黄色
     */
    GLOSS_YELLOW(103),

    /**
     * 亮蓝色
     */
    GLOSS_BLUE(104),

    /**
     * 亮紫色
     */
    GLOSS_PURPLE(105),

    /**
     * 亮青色
     */
    GLOSS_CYAN(106),

    /**
     * 亮白色
     */
    GLOSS_WHITE(107),

    ;

    /**
     * 颜色码
     */
    public final int code;

    AnsiBackground(int code) {
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

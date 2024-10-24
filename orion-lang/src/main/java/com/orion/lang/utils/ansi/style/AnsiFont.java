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
package com.orion.lang.utils.ansi.style;

import com.orion.lang.constant.Const;

import static com.orion.lang.utils.ansi.AnsiConst.CSI_PREFIX;
import static com.orion.lang.utils.ansi.AnsiConst.SGR_SUFFIX;

/**
 * ANIS 字体
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 11:37
 */
public enum AnsiFont implements AnsiStyle {

    /**
     * 重置
     */
    RESET(0),

    /**
     * 粗体
     */
    BOLD(1),

    /**
     * 弱化
     */
    FAINT(2),

    /**
     * 斜体
     */
    ITALIC(3),

    /**
     * 下划线
     */
    UNDERLINE(4),

    /**
     * 缓慢闪烁
     */
    SLOW_BLINK(5),

    /**
     * 快速闪烁
     */
    FAST_BLINK(6),

    /**
     * 反色
     */
    REVERSE_COLOR(7),

    /**
     * 前景隐藏
     */
    HIDDEN(8),

    /**
     * 删除线
     */
    STRIKETHROUGH(9),

    /**
     * 重置 粗体
     */
    RESET_BOLD(21),

    /**
     * 重置 弱化
     */
    RESET_FAINT(22),

    /**
     * 重置 斜体
     */
    RESET_ITALIC(23),

    /**
     * 重置 下划线
     */
    RESET_UNDERLINE(24),

    /**
     * 重置 缓慢闪烁
     */
    RESET_SLOW_BLINK(25),

    /**
     * 重置 快速闪烁
     */
    RESET_FAST_BLINK(26),

    /**
     * 重置 反色
     */
    RESET_REVERSE_COLOR(27),

    /**
     * 重置 前景隐藏
     */
    RESET_HIDDEN(28),

    /**
     * 重置 删除线
     */
    RESET_STRIKETHROUGH(29),

    ;

    private final int code;

    AnsiFont(int code) {
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

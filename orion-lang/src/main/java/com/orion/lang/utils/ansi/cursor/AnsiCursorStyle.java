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
package com.orion.lang.utils.ansi.cursor;

import com.orion.lang.utils.ansi.AnsiElement;

import static com.orion.lang.utils.ansi.AnsiConst.CSI_PREFIX;

/**
 * ANSI 光标样式
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/29 14:13
 */
public enum AnsiCursorStyle implements AnsiElement {

    /**
     * 块
     */
    STEADY_BLOCK(1),

    /**
     * 闪烁块
     */
    BLINK_BLOCK(2),

    /**
     * 下滑线
     */
    STEADY_UNDERLINE(3),

    /**
     * 闪烁下滑线
     */
    BLINK_UNDERLINE(4),

    /**
     * 数显
     */
    STEADY_BAR(5),

    /**
     * 闪烁竖线
     */
    BLINK_BAR(6),

    ;

    private final int code;

    AnsiCursorStyle(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return CSI_PREFIX + code + " q";
    }

}

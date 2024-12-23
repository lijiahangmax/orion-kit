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
package cn.orionsec.kit.lang.utils.ansi.style.color;

import cn.orionsec.kit.lang.utils.Colors;

import static cn.orionsec.kit.lang.utils.ansi.AnsiConst.*;

/**
 * ANSI bit24 颜色
 * <p>
 * 前景: 38:2:r;g;b
 * 背景: 48:2:r;g;b
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 16:40
 */
public class AnsiBit24Color extends AnsiBitColor {

    public AnsiBit24Color(String code) {
        super(code);
    }

    /**
     * 24bit 前景色
     *
     * @param r r
     * @param g g
     * @param b b
     * @return color
     */
    public static AnsiColor foreground(int r, int g, int b) {
        return new AnsiBit24Color(color(COLOR_FG, COLOR_BIT24, r, g, b));
    }

    public static AnsiColor foreground(int[] rgb) {
        return new AnsiBit24Color(color(COLOR_FG, COLOR_BIT24, rgb));
    }

    public static AnsiColor foreground(String hex) {
        return new AnsiBit24Color(color(COLOR_FG, COLOR_BIT24, Colors.toRgbColor(hex)));
    }

    /**
     * 24bit 背景色
     *
     * @param r r
     * @param g g
     * @param b b
     * @return color
     */
    public static AnsiColor background(int r, int g, int b) {
        return new AnsiBit24Color(color(COLOR_BG, COLOR_BIT24, r, g, b));
    }

    public static AnsiColor background(int[] rgb) {
        return new AnsiBit24Color(color(COLOR_BG, COLOR_BIT24, rgb));
    }

    public static AnsiColor background(String hex) {
        return new AnsiBit24Color(color(COLOR_BG, COLOR_BIT24, Colors.toRgbColor(hex)));
    }

}

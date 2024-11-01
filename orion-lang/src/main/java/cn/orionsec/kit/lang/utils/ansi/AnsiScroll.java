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
package cn.orionsec.kit.lang.utils.ansi;

import static cn.orionsec.kit.lang.utils.ansi.AnsiConst.*;

/**
 * ANSI 滚动
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/29 11:01
 */
public class AnsiScroll implements AnsiElement {

    private final String code;

    public AnsiScroll(String code) {
        this.code = code;
    }

    public static AnsiScroll up() {
        return new AnsiScroll(SCROLL_UP);
    }

    /**
     * 向上滚动 N 行
     *
     * @param line line
     * @return scroll
     */
    public static AnsiScroll up(int line) {
        return new AnsiScroll(line + SCROLL_UP);
    }

    public static AnsiScroll down() {
        return new AnsiScroll(SCROLL_DOWN);
    }

    /**
     * 向下滚动 N 行
     *
     * @param line line
     * @return scroll
     */
    public static AnsiScroll down(int line) {
        return new AnsiScroll(line + SCROLL_DOWN);
    }

    public static AnsiScroll right() {
        return new AnsiScroll(SCROLL_RIGHT);
    }

    /**
     * 向右滚动 N 行
     *
     * @param line line
     * @return scroll
     */
    public static AnsiScroll right(int line) {
        return new AnsiScroll(line + SCROLL_RIGHT);
    }

    public static AnsiScroll left() {
        return new AnsiScroll(SCROLL_LEFT);
    }

    /**
     * 向左滚动 N 行
     *
     * @param line line
     * @return scroll
     */
    public static AnsiScroll left(int line) {
        return new AnsiScroll(line + SCROLL_LEFT);
    }

    @Override
    public String toString() {
        return CSI_PREFIX + code;
    }

}

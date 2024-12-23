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

import cn.orionsec.kit.lang.utils.ansi.style.AnsiStyle;

/**
 * ANSI 拼接器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 11:20
 */
public class AnsiAppender {

    private StringBuilder builder;

    public AnsiAppender() {
        this.builder = new StringBuilder();
    }

    public static AnsiAppender create() {
        return new AnsiAppender();
    }

    /**
     * 拼接
     *
     * @param text text
     * @return this
     */
    public AnsiAppender append(String text) {
        builder.append(text);
        return this;
    }

    /**
     * 拼接
     *
     * @param style style
     * @param text  text
     * @return this
     */
    public AnsiAppender append(AnsiStyle style, String text) {
        builder.append(style.toString())
                .append(text)
                .append(AnsiConst.CSI_RESET);
        return this;
    }

    /**
     * 拼接
     *
     * @param element element
     * @return this
     */
    public AnsiAppender append(AnsiElement element) {
        builder.append(element.toString());
        return this;
    }

    /**
     * 拼接
     *
     * @param elements elements
     * @return this
     */
    public AnsiAppender append(Object... elements) {
        for (Object element : elements) {
            builder.append(element.toString());
        }
        return this;
    }

    /**
     * 拼接 reset
     *
     * @return this
     */
    public AnsiAppender reset() {
        builder.append(AnsiConst.CSI_RESET);
        return this;
    }

    /**
     * 新行
     *
     * @return this
     */
    public AnsiAppender newLine() {
        builder.append(AnsiCtrl.CR)
                .append(AnsiCtrl.LF);
        return this;
    }

    /**
     * 重置
     */
    public void clear() {
        this.builder = new StringBuilder();
    }

    /**
     * 获取 value
     *
     * @return value
     */
    public String get() {
        return builder.toString();
    }

    /**
     * 获取后重置
     *
     * @return value
     */
    public String getAndClear() {
        String r = this.get();
        this.clear();
        return r;
    }

    @Override
    public String toString() {
        return this.get();
    }

}

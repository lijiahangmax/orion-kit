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
package com.orion.lang.define.wrapper;

import com.orion.lang.constant.Const;
import com.orion.lang.define.support.CloneSupport;
import com.orion.lang.utils.Urls;
import com.orion.lang.utils.Xsses;
import com.orion.lang.utils.codec.Base64s;

/**
 * restful 文本包装
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/1/2 18:00
 */
public class TextWrapper extends CloneSupport<TextWrapper> implements Wrapper<String> {

    private static final long serialVersionUID = 5693256198048111L;

    private final StringBuilder sb;

    public TextWrapper() {
        this(new StringBuilder());
    }

    public TextWrapper(String s) {
        this(new StringBuilder(s));
    }

    public TextWrapper(StringBuilder sb) {
        if (sb == null) {
            this.sb = new StringBuilder();
        } else {
            this.sb = sb;
        }
    }

    public static TextWrapper get() {
        return new TextWrapper();
    }

    public static TextWrapper get(String s) {
        return new TextWrapper(s);
    }

    public static TextWrapper get(StringBuilder sb) {
        return new TextWrapper(sb);
    }

    public TextWrapper append(String s) {
        sb.append(s);
        return this;
    }

    public TextWrapper appendLine(String s) {
        sb.append(s).append(Const.LF);
        return this;
    }

    public TextWrapper newLine() {
        sb.append(Const.LF);
        return this;
    }

    public TextWrapper newLine(String eof) {
        sb.append(eof);
        return this;
    }

    public String encodeXss() {
        return Xsses.clean(sb.toString());
    }

    public String decodeXss() {
        return Xsses.recode(sb.toString());
    }

    public String encodeUrl() {
        return Urls.encode(sb.toString());
    }

    public String decodeUrl() {
        return Urls.decode(sb.toString());
    }

    public String encodeBase64() {
        return Base64s.encode(sb.toString());
    }

    public String decodeBase64() {
        return Base64s.decode(sb.toString());
    }

    public String text() {
        return sb.toString();
    }

    @Override
    public String toString() {
        return sb.toString();
    }

}

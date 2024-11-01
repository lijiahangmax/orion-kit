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
package cn.orionsec.kit.lang.utils;

import cn.orionsec.kit.lang.constant.Const;

import java.nio.charset.Charset;

/**
 * 编码工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/5 14:47
 */
public class Charsets {

    public static final Charset UTF_8 = of(Const.UTF_8);

    public static final Charset GBK = of(Const.GBK);

    public static final Charset GB_2312 = of(Const.GB_2312);

    public static final Charset ISO_8859_1 = of(Const.ISO_8859_1);

    private Charsets() {
    }

    public static Charset of(String charset) {
        return Charset.forName(charset);
    }

    /**
     * 是否是支持的编码集
     *
     * @param charset charset
     * @return support
     */
    public static boolean isSupported(String charset) {
        if (Strings.isBlank(charset)) {
            return false;
        }
        try {
            return Charset.isSupported(charset);
        } catch (Exception e) {
            return false;
        }
    }

}

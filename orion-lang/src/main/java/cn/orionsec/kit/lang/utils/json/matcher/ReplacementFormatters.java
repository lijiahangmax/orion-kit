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
package cn.orionsec.kit.lang.utils.json.matcher;

/**
 * json 占位符替换器 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/11 15:42
 */
public class ReplacementFormatters {

    private ReplacementFormatters() {
    }

    public static ReplacementFormatter create() {
        return new ReplacementFormatter("${", "}");
    }

    public static ReplacementFormatter create(String prefix, String suffix) {
        return new ReplacementFormatter(prefix, suffix);
    }

    public static String format(String template, Object o) {
        return create().format(template, o);
    }

    public static String format(String template, String s) {
        return create().format(template, s);
    }

    public static String format(String prefix, String suffix, String template, Object o) {
        return create(prefix, suffix).format(template, o);
    }

    public static String format(String prefix, String suffix, String template, String s) {
        return create(prefix, suffix).format(template, s);
    }

}

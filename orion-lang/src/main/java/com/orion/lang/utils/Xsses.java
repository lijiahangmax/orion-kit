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
package com.orion.lang.utils;

import com.orion.lang.KitLangConfiguration;
import com.orion.lang.config.KitConfig;

import java.util.regex.Pattern;

/**
 * xss 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/9/20 17:07
 */
public class Xsses {

    private static final Pattern SCRIPT_PATTERN = KitConfig.get(KitLangConfiguration.CONFIG.XSS_SCRIPT_PATTERN);

    private static final Pattern STYLE_PATTERN = KitConfig.get(KitLangConfiguration.CONFIG.XSS_STYLE_PATTERN);

    private static final Pattern HTML_TAG_PATTERN = KitConfig.get(KitLangConfiguration.CONFIG.XSS_HTML_TAG_PATTERN);

    private Xsses() {
    }

    /**
     * 过滤xss
     *
     * @param s html代码
     * @return ignore
     */
    public static String clean(String s) {
        if (Strings.isBlank(s)) {
            return s;
        }
        // 过滤 script 标签
        if (SCRIPT_PATTERN != null) {
            s = SCRIPT_PATTERN.matcher(s).replaceAll(Strings.EMPTY);
        }
        // 过滤 style 标签
        if (STYLE_PATTERN != null) {
            s = STYLE_PATTERN.matcher(s).replaceAll(Strings.EMPTY);
        }
        // 过滤 html 标签
        if (HTML_TAG_PATTERN != null) {
            s = HTML_TAG_PATTERN.matcher(s).replaceAll(Strings.EMPTY);
        }

        // 过滤特殊字符
        s = s.replaceAll("&", "&amp;");
        s = s.replaceAll("<", "&lt;");
        s = s.replaceAll(">", "&gt;");
        s = s.replaceAll(" ", "&nbsp;");
        s = s.replaceAll("'", "&apos;");
        s = s.replaceAll("\"", "&quot;");
        s = s.replaceAll("\n", "<br/>");
        s = s.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        return s;
    }

    /**
     * html代码反转义
     *
     * @param s html代码
     * @return ignore
     */
    public static String recode(String s) {
        if (Strings.isBlank(s)) {
            return s;
        }
        s = s.replaceAll("&amp;", "&");
        s = s.replaceAll("&lt;", "<");
        s = s.replaceAll("&gt;", ">");
        s = s.replaceAll("&nbsp;", " ");
        s = s.replaceAll("&apos;", "'");
        s = s.replaceAll("&quot;", "\"");
        s = s.replaceAll("<br/>", "\n");
        s = s.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;", "\t");
        return s;
    }

}

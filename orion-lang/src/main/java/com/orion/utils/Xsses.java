package com.orion.utils;

import java.util.regex.Pattern;

/**
 * xss工具类
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/9/20 17:07
 */
public class Xsses {

    private static final Pattern SCRIPT_PATTERN = Pattern.compile("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", Pattern.CASE_INSENSITIVE);

    private static final Pattern STYLE_PATTERN = Pattern.compile("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", Pattern.CASE_INSENSITIVE);

    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);

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
        // 过滤script标签
        s = SCRIPT_PATTERN.matcher(s).replaceAll(Strings.EMPTY);
        // 过滤style标签
        s = STYLE_PATTERN.matcher(s).replaceAll(Strings.EMPTY);
        // 过滤html标签
        // s = HTML_TAG_PATTERN.matcher(s).replaceAll(Strings.EMPTY);

        // 过滤特殊字符
        s = s.replaceAll("&", "&amp;");
        s = s.replaceAll("<", "&lt;");
        s = s.replaceAll(">", "&gt;");
        s = s.replaceAll(" ", "&nbsp;");
        s = s.replaceAll("'", "&#39;");
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
        s = s.replaceAll("&#39;", "'");
        s = s.replaceAll("&apos;", "'");
        s = s.replaceAll("&quot;", "\"");
        s = s.replaceAll("<br/>", "\n");
        s = s.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;", "\t");
        return s;
    }

}

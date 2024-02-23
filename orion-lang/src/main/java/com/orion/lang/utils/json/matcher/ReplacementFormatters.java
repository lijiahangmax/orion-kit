package com.orion.lang.utils.json.matcher;

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

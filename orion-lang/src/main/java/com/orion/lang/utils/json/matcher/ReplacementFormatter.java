package com.orion.lang.utils.json.matcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Objects1;
import com.orion.lang.utils.Strings;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * json 占位符替换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/11 10:37
 */
public class ReplacementFormatter {

    private final Pattern pattern;

    private final String perfix;

    private final String suffix;

    private NoMatchStrategy noMatchStrategy;

    private Map<String, Object> defaults;

    public ReplacementFormatter(String perfix, String suffix) {
        this.perfix = perfix;
        this.suffix = suffix;
        this.pattern = this.createPattern();
        this.noMatchStrategy = NoMatchStrategy.EMPTY;
    }

    public static ReplacementFormatter create() {
        return new ReplacementFormatter("${", "}");
    }

    public static ReplacementFormatter create(String perfix, String suffix) {
        return new ReplacementFormatter(perfix, suffix);
    }

    /**
     * 创建正则
     *
     * @param perfix perfix
     * @param suffix suffix
     * @return 正则
     */
    private Pattern createPattern() {
        String evalPerfix = perfix.chars()
                .mapToObj(s -> "\\" + (char) s)
                .collect(Collectors.joining());
        String evalSuffix = suffix.chars()
                .mapToObj(s -> "\\" + (char) s)
                .collect(Collectors.joining());
        return Pattern.compile("(" + evalPerfix + ")(.+?)(" + evalSuffix + ")");
    }

    /**
     * 设置未匹配策略
     *
     * @param noMatchStrategy noMatchStrategy
     * @return this
     */
    public ReplacementFormatter noMatchStrategy(NoMatchStrategy noMatchStrategy) {
        this.noMatchStrategy = noMatchStrategy;
        return this;
    }

    /**
     * 添加默认值
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public ReplacementFormatter defaultValue(String key, Object value) {
        if (defaults == null) {
            this.defaults = new HashMap<>();
        }
        defaults.put(key, value);
        return this;
    }

    /**
     * 添加默认值
     *
     * @param map map
     * @return this
     */
    public ReplacementFormatter defaultValue(Map<String, ?> map) {
        if (defaults == null) {
            this.defaults = new HashMap<>();
        }
        defaults.putAll(map);
        return this;
    }

    /**
     * 格式化
     *
     * @param template template
     * @param o        o
     * @return value
     */
    public String format(String template, Object o) {
        return this.format(template, JSON.toJSONString(o));
    }

    /**
     * 格式化
     *
     * @param template template
     * @param json     json
     * @return value
     */
    public String format(String template, String json) {
        if (Strings.isAnyEmpty(template, json)) {
            return template;
        }
        String result = template;
        Matcher matcher = pattern.matcher(template);
        while (matcher.find()) {
            // 获取替换符
            String replacement = matcher.group();
            String path = replacement.substring(perfix.length(), replacement.length() - suffix.length()).trim();
            Object readValue = JSONPath.read(json, path);
            if (readValue == null) {
                // 获取默认值
                if (defaults != null) {
                    Object defaultValue = defaults.get(path);
                    if (defaultValue != null) {
                        result = result.replace(replacement, Objects1.toString(defaultValue));
                        continue;
                    }
                }
                // 使用策略
                if (NoMatchStrategy.KEEP.equals(noMatchStrategy)) {
                    continue;
                } else if (NoMatchStrategy.EMPTY.equals(noMatchStrategy)) {
                    result = result.replace(replacement, Const.EMPTY);
                }
            } else {
                result = result.replace(replacement, Objects1.toString(readValue));
            }
        }
        return result;
    }

}

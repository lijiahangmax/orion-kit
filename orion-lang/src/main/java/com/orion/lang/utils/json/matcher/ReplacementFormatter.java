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
package com.orion.lang.utils.json.matcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Exceptions;
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

    private final String prefix;

    private final String suffix;

    private NoMatchStrategy noMatchStrategy;

    private ErrorStrategy errorStrategy;

    private Map<String, Object> defaults;

    public ReplacementFormatter(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.pattern = this.createPattern();
        this.noMatchStrategy = NoMatchStrategy.EMPTY;
        this.errorStrategy = ErrorStrategy.THROW;
    }

    /**
     * 创建正则
     *
     * @return 正则
     */
    private Pattern createPattern() {
        String evalPrefix = prefix.chars()
                .mapToObj(s -> "\\" + (char) s)
                .collect(Collectors.joining());
        String evalSuffix = suffix.chars()
                .mapToObj(s -> "\\" + (char) s)
                .collect(Collectors.joining());
        return Pattern.compile("(" + evalPrefix + ")(.+?)(" + evalSuffix + ")");
    }

    /**
     * 设置未匹配到策略
     *
     * @param noMatchStrategy noMatchStrategy
     * @return this
     */
    public ReplacementFormatter noMatchStrategy(NoMatchStrategy noMatchStrategy) {
        this.noMatchStrategy = noMatchStrategy;
        return this;
    }

    /**
     * 设置错误处理策略
     *
     * @param errorStrategy errorStrategy
     * @return this
     */
    public ReplacementFormatter errorStrategy(ErrorStrategy errorStrategy) {
        this.errorStrategy = errorStrategy;
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
            String path = replacement.substring(prefix.length(), replacement.length() - suffix.length()).trim();
            Object readValue = null;
            try {
                // 解析内容
                readValue = JSONPath.read(json, path);
                // 设置默认值
                if (readValue == null && defaults != null) {
                    readValue = defaults.get(path);
                }
            } catch (Exception e) {
                // 解析失败
                if (ErrorStrategy.DEFAULT.equals(errorStrategy)) {
                    // 使用默认值
                    if (defaults != null) {
                        readValue = defaults.get(path);
                    }
                } else if (ErrorStrategy.EMPTY.equals(errorStrategy)) {
                    // 设置为空串
                    readValue = Const.EMPTY;
                } else if (ErrorStrategy.THROW.equals(errorStrategy)) {
                    // 抛出异常
                    throw Exceptions.argument("parse argument " + path + " error", e);
                } else {
                    // 保留占位符
                    continue;
                }
            }
            // 未匹配策略
            if (readValue == null) {
                if (NoMatchStrategy.EMPTY.equals(noMatchStrategy)) {
                    // 设置为空串
                    readValue = Const.EMPTY;
                } else if (NoMatchStrategy.THROW.equals(noMatchStrategy)) {
                    // 抛出异常
                    throw Exceptions.argument("argument " + path + " is null");
                } else {
                    // 保留占位符
                    continue;
                }
            }
            // 替换内容
            result = result.replace(replacement, Objects1.toString(readValue));
        }
        return result;
    }

}

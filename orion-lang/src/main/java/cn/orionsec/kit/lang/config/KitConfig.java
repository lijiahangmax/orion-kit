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
package cn.orionsec.kit.lang.config;

import cn.orionsec.kit.lang.utils.collect.Maps;

import java.util.Map;

/**
 * orion-kit 配置项
 * <p>
 * 用于 kit 底层默认封装的配置覆盖
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/6 17:00
 */
public class KitConfig {

    private static final Map<String, Object> CONFIG = Maps.newMap();

    /**
     * 覆盖配置 (会覆盖) 一般用于重写配置
     *
     * @param key key
     * @param v   value
     */
    public static void override(String key, Object v) {
        CONFIG.put(key, v);
    }

    /**
     * 设置配置 (不会覆盖) 一般用于默认配置
     *
     * @param key key
     * @param v   value
     */
    public static void init(String key, Object v) {
        CONFIG.putIfAbsent(key, v);
    }

    /**
     * 获取配置
     *
     * @param key key
     * @param <T> T
     * @return value
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) CONFIG.get(key);
    }

    /**
     * 删除配置
     *
     * @param key key
     */
    public static void remove(String key) {
        CONFIG.remove(key);
    }

    /**
     * 获取配置
     *
     * @param key key
     * @param def def
     * @param <T> T
     * @return value
     */
    @SuppressWarnings("unchecked")
    public static <T> T getOrDefault(String key, T def) {
        return (T) CONFIG.getOrDefault(key, def);
    }

    /**
     * 获取配置对象
     *
     * @return 配置
     */
    public static Map<String, Object> getConfig() {
        return CONFIG;
    }

}

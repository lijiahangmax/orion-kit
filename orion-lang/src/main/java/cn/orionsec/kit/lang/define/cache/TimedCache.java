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
package cn.orionsec.kit.lang.define.cache;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 过期缓存
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/13 12:00
 */
public interface TimedCache extends Closeable {

    /**
     * 添加
     *
     * @param key      key
     * @param supplier supplier
     * @param <V>      V
     * @return V
     */
    <V> V put(String key, Supplier<V> supplier);

    /**
     * 添加
     *
     * @param key   key
     * @param value value
     */
    void put(String key, Object value);

    /**
     * 添加
     *
     * @param key      key
     * @param supplier supplier
     * @param <V>      V
     * @return value
     */
    <V> V putIfAbsent(String key, Supplier<V> supplier);

    /**
     * 添加
     *
     * @param key   key
     * @param value value
     * @param <V>   V
     * @return value
     */
    <V> V putIfAbsent(String key, Object value);

    /**
     * 添加并指定过期时间
     *
     * @param key         key
     * @param value       value
     * @param expireAfter 自定义过期时间
     */
    void put(String key, Object value, long expireAfter);

    /**
     * 添加并指定过期时间
     *
     * @param key         key
     * @param supplier    supplier
     * @param expireAfter 自定义过期时间
     * @param <V>         V
     * @return V
     */
    <V> V put(String key, Supplier<V> supplier, long expireAfter);

    /**
     * 添加并指定过期时间
     *
     * @param key         key
     * @param value       value
     * @param expireAfter 自定义过期时间
     * @param <V>         V
     * @return value
     */
    <V> V putIfAbsent(String key, Object value, long expireAfter);

    /**
     * 添加并指定过期时间
     *
     * @param key         key
     * @param supplier    supplier
     * @param expireAfter 自定义过期时间
     * @param <V>         V
     * @return value
     */
    <V> V putIfAbsent(String key, Supplier<V> supplier, long expireAfter);

    /**
     * 添加
     *
     * @param map map
     */
    void putAll(Map<String, Object> map);

    /**
     * 获取值
     *
     * @param key key
     * @param <V> V
     * @return value
     */
    <V> V get(String key);

    /**
     * 获取值
     *
     * @param key key
     * @param def def
     * @param <V> V
     * @return value
     */
    <V> V getOrDefault(String key, V def);

    /**
     * 检测是否有此缓存
     *
     * @param key key
     * @return contains
     */
    boolean containsKey(String key);

    /**
     * 清空
     */
    void clear();

    /**
     * 获取存储容器
     *
     * @return store
     */
    ConcurrentHashMap<String, TimedCacheValue> getStore();

}
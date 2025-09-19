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

import cn.orionsec.kit.lang.utils.Objects1;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 过期缓存
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/13 12:00
 */
public class TimedCacheImpl<T> implements TimedCache<T> {

    private final int expireAfter;

    private final Map<String, TimedCacheValue<T>> store;

    private final TimedCacheChecker<T> checker;

    TimedCacheImpl(int expireAfter,
                   Map<String, TimedCacheValue<T>> store,
                   TimedCacheChecker<T> checker) {
        this.expireAfter = expireAfter;
        this.store = store;
        this.checker = checker;
        // 开始检测
        checker.start();
    }

    @Override
    public T put(String key, T value) {
        TimedCacheValue<T> returnValue = store.put(key, this.createValue(value));
        return this.safeGet(returnValue);
    }

    @Override
    public T put(String key, T value, long expireAfter) {
        TimedCacheValue<T> returnValue = store.put(key, this.createValue(value, expireAfter));
        return this.safeGet(returnValue);
    }

    @Override
    public T putIfAbsent(String key, T value) {
        TimedCacheValue<T> returnValue = store.putIfAbsent(key, this.createValue(value));
        return this.safeGet(returnValue);
    }

    @Override
    public T putIfAbsent(String key, T value, long expireAfter) {
        TimedCacheValue<T> returnValue = store.putIfAbsent(key, this.createValue(value, expireAfter));
        return this.safeGet(returnValue);
    }

    @Override
    public void putAll(Map<? extends String, ? extends T> map) {
        map.forEach((k, v) -> store.put(k, this.createValue(v)));
    }

    @Override
    public T get(Object key) {
        return this.getOrDefault(key, null);
    }

    @Override
    public T getOrDefault(Object key, T defaultValue) {
        TimedCacheValue<T> value = store.get((String) key);
        return Objects1.def(this.safeGet(value), defaultValue);
    }

    @Override
    public T remove(Object key) {
        return this.safeGet(store.remove(key));
    }

    @Override
    public int size() {
        return store.size();
    }

    @Override
    public boolean isEmpty() {
        return store.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return store.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return store.values()
                .stream()
                .anyMatch(v -> Objects.equals(value, v.value));
    }

    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public Set<String> keySet() {
        return store.keySet();
    }

    @Override
    public Collection<T> values() {
        return store.values()
                .stream()
                .map(this::safeGet)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Entry<String, T>> entrySet() {
        return store.keySet()
                .stream()
                .map(s -> new AbstractMap.SimpleEntry<>(s, this.safeGet(store.get(s))))
                .collect(Collectors.toSet());
    }

    @Override
    public Map<String, TimedCacheValue<T>> getStore() {
        return store;
    }

    /**
     * 安全获取缓存值
     *
     * @param value value
     * @return value
     */
    private T safeGet(TimedCacheValue<T> value) {
        if (value == null) {
            return null;
        }
        return value.value;
    }

    /**
     * 创建缓存值
     *
     * @param o o
     * @return value
     */
    private TimedCacheValue<T> createValue(T o) {
        return this.createValue(o, expireAfter);
    }

    /**
     * 创建缓存值
     *
     * @param o           o
     * @param expireAfter expireAfter
     * @return value
     */
    private TimedCacheValue<T> createValue(T o, long expireAfter) {
        return new TimedCacheValue<T>(System.currentTimeMillis() + expireAfter, o);
    }

    @Override
    public void close() {
        store.clear();
        checker.close();
    }

    @Override
    public String toString() {
        return store.toString();
    }

}

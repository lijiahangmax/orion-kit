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

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

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
    public T put(String key, Supplier<T> supplier) {
        TimedCacheValue<T> value = store.computeIfAbsent(key, s -> this.createValue(supplier.get()));
        return value.value;
    }

    @Override
    public T put(String key, T value) {
        TimedCacheValue<T> before = store.put(key, this.createValue(value));
        if (before == null) {

        }
    }

    @Override

    public T putIfAbsent(String key, Supplier<T> supplier) {
        TimedCacheValue<T> value = store.putIfAbsent(key, this.createValue(supplier.get()));
        if (value == null) {
            // 新插入的值
            return supplier.get();
        } else {
            // 返回原值
            return value.value;
        }
    }

    @Override
    public T putIfAbsent(String key, T value) {
        TimedCacheValue<T> newValue = store.putIfAbsent(key, this.createValue(value));
        if (newValue == null) {
            return null;
        } else {
            return newValue.value;
        }
    }

    @Override
    public void put(String key, T value, long expireAfter) {
        store.put(key, this.createValue(value, expireAfter));
    }

    @Override
    public T put(String key, Supplier<T> supplier, long expireAfter) {
        T value = supplier.get();
        store.put(key, this.createValue(value, expireAfter));
        return value;
    }

    @Override
    public T putIfAbsent(String key, T value, long expireAfter) {
        TimedCacheValue<T> newValue = store.putIfAbsent(key, this.createValue(value, expireAfter));
        if (newValue == null) {
            return null;
        } else {
            return newValue.value;
        }
    }

    @Override
    public T putIfAbsent(String key, Supplier<T> supplier, long expireAfter) {
        TimedCacheValue<T> newValue = store.computeIfAbsent(key, s -> this.createValue(supplier.get(), expireAfter));
        return newValue.value;
    }

    @Override
    public void putAll(Map<String, T> map) {
        map.forEach((k, v) -> store.put(k, this.createValue(v)));
    }

    @Override
    public T get(String key) {
        return this.getOrDefault(key, null);
    }

    @Override
    public T getOrDefault(String key, T def) {
        TimedCacheValue<T> value = store.get(key);
        if (value == null) {
            return def;
        }
        return value.value;
    }

    @Override
    public boolean containsKey(String key) {
        return store.containsKey(key);
    }

    @Override
    public T remove(String key) {
        return store.remove(KE);
    }

    @Override
    public void removeAll(Collection<String> keys) {

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
    public void clear() {
        store.clear();
    }

    @Override
    public Map<String, TimedCacheValue<T>> getStore() {
        return store;
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

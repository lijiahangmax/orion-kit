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
package cn.orionsec.kit.lang.define.cache;

import cn.orionsec.kit.lang.define.collect.ConcurrentReferenceHashMap;
import cn.orionsec.kit.lang.utils.Objects1;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * 软引用缓存
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/18 20:34
 */
public class SoftCache<K, V> implements Iterable<Map.Entry<K, V>>, Serializable {

    private final ConcurrentReferenceHashMap<K, V> CACHE;

    private AtomicLong count;

    private AtomicLong hit;

    /**
     * 启用缓存命记录
     */
    private boolean useHit;

    public SoftCache() {
        this(false);
    }

    public SoftCache(boolean useHit) {
        this.CACHE = new ConcurrentReferenceHashMap<>(ConcurrentReferenceHashMap.ReferenceType.SOFT);
        if (useHit) {
            this.count = new AtomicLong();
            this.hit = new AtomicLong();
            this.useHit = true;
        }
    }

    public SoftCache(Map<K, V> m) {
        this(false);
        CACHE.putAll(m);
    }

    public SoftCache(Map<K, V> m, boolean useHit) {
        this(useHit);
        CACHE.putAll(m);
    }

    public static <K, V> SoftCache<K, V> create() {
        return new SoftCache<>();
    }

    public static <K, V> SoftCache<K, V> create(boolean useHit) {
        return new SoftCache<>(useHit);
    }

    public static <K, V> SoftCache<K, V> create(Map<K, V> m) {
        return new SoftCache<>(m);
    }

    public static <K, V> SoftCache<K, V> create(Map<K, V> m, boolean useHit) {
        return new SoftCache<>(m, useHit);
    }

    public V get(K key) {
        V v = CACHE.get(key);
        if (useHit) {
            count.incrementAndGet();
            if (v != null) {
                hit.incrementAndGet();
            }
        }
        return v;
    }

    public V get(K key, V def) {
        return Objects1.def(this.get(key), def);
    }

    public V put(K key, V value) {
        return CACHE.put(key, value);
    }

    public V remove(K key) {
        return CACHE.remove(key);
    }

    public void clear() {
        CACHE.clear();
    }

    /**
     * @return 查询数
     */
    public long getCounts() {
        return useHit ? count.get() : 0;
    }

    /**
     * @return 命中数
     */
    public long getHits() {
        return useHit ? hit.get() : 0;
    }

    /**
     * @return 缓存命中率
     */
    public double getHitsRate() {
        return useHit ? ((double) hit.get()) / ((double) count.get()) : 0;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return CACHE.entrySet().iterator();
    }

    @Override
    public void forEach(Consumer<? super Map.Entry<K, V>> action) {
        CACHE.entrySet().forEach(action);
    }

}

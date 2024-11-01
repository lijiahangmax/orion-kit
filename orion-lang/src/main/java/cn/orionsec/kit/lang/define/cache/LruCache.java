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

import cn.orionsec.kit.lang.constant.Const;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LRU 缓存
 * 最先进最先淘汰
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/8/22 20:54
 */
public class LruCache<K, V> extends LinkedHashMap<K, V> {

    /**
     * 负载因子
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;

    /**
     * 默认最大容量
     */
    private static final int DEFAULT_MAX_CAPACITY = 10;

    /**
     * 重入锁
     */
    private final Lock lock;

    /**
     * 最大容量
     */
    private volatile int maxCapacity;

    public LruCache() {
        this(DEFAULT_MAX_CAPACITY);
    }

    public LruCache(int maxCapacity) {
        super(Const.CAPACITY_16, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
        this.lock = new ReentrantLock();
    }

    public static <K, V> LruCache<K, V> create() {
        return new LruCache<>();
    }

    public static <K, V> LruCache<K, V> create(int maxCapacity) {
        return new LruCache<>(maxCapacity);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > maxCapacity;
    }

    @Override
    public boolean containsKey(Object key) {
        lock.lock();
        try {
            return super.containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V get(Object key) {
        lock.lock();
        try {
            return super.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        lock.lock();
        try {
            return super.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        lock.lock();
        try {
            return super.remove(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        lock.lock();
        try {
            return super.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            super.clear();
        } finally {
            lock.unlock();
        }
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

}
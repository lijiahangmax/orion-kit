package com.orion.lang.define.cache;

import com.orion.lang.constant.Const;

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

    public static <K, V> LruCache<K, V> newLru() {
        return new LruCache<>();
    }

    public static <K, V> LruCache<K, V> newLru(int maxCapacity) {
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
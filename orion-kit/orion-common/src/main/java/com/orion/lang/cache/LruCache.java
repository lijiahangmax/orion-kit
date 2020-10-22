package com.orion.lang.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LRU缓存
 * 最先进最先淘汰
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/8/22 20:54
 */
public class LruCache<K, V> extends LinkedHashMap<K, V> {

    /**
     * 负载因子
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 默认最大容量
     */
    private static final int DEFAULT_MAX_CAPACITY = 10;

    /**
     * 重入锁
     */
    private final Lock lock = new ReentrantLock();

    /**
     * 最大容量
     */
    private volatile int maxCapacity;

    public LruCache() {
        this(DEFAULT_MAX_CAPACITY);
    }

    public LruCache(int maxCapacity) {
        super(16, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    public static <K, V> LruCache<K, V> newLru() {
        return new LruCache<>();
    }

    public static <K, V> LruCache<K, V> newLru(int maxCapacity) {
        return new LruCache<>(maxCapacity);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxCapacity;
    }

    @Override
    public boolean containsKey(Object key) {
        try {
            lock.lock();
            return super.containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V get(Object key) {
        try {
            lock.lock();
            return super.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        try {
            lock.lock();
            return super.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        try {
            lock.lock();
            return super.remove(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        try {
            lock.lock();
            return super.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            super.clear();
        } finally {
            lock.unlock();
        }
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public LruCache<K, V> setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        return this;
    }

}
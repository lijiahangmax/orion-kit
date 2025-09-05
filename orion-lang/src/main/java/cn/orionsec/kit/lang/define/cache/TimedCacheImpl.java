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

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.define.thread.ExecutorBuilder;
import cn.orionsec.kit.lang.utils.Threads;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * 过期缓存
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/13 12:00
 */
public class TimedCacheImpl implements TimedCache {

    /**
     * 检测线程池
     */
    private static final ExecutorService CHECKER_EXECUTOR = ExecutorBuilder.create()
            .namedThreadFactory("orion-cache-checker-")
            .corePoolSize(1)
            .maxPoolSize(Integer.MAX_VALUE)
            .keepAliveTime(Const.MS_S_60)
            .workQueue(new SynchronousQueue<>())
            .allowCoreThreadTimeout(true)
            .build();

    private final int expireAfter;

    private final Checker checker;

    private final ConcurrentHashMap<String, TimedCacheValue> store;

    public TimedCacheImpl(int expireAfter, int checkInterval, BiConsumer<String, Object> expiredListener) {
        this.expireAfter = expireAfter;
        this.store = new ConcurrentHashMap<>();
        this.checker = new Checker(checkInterval, store, expiredListener);
        CHECKER_EXECUTOR.execute(checker);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V put(String key, Supplier<V> supplier) {
        TimedCacheValue value = store.computeIfAbsent(key, s -> this.createValue(supplier.get()));
        return (V) value.value;
    }

    @Override
    public void put(String key, Object value) {
        store.put(key, this.createValue(value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V putIfAbsent(String key, Supplier<V> supplier) {
        TimedCacheValue value = store.putIfAbsent(key, this.createValue(supplier.get()));
        if (value == null) {
            // 新插入的值
            return supplier.get();
        } else {
            // 返回原值
            return (V) value.value;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V putIfAbsent(String key, Object value) {
        TimedCacheValue newValue = store.putIfAbsent(key, this.createValue(value));
        if (newValue == null) {
            return null;
        } else {
            return (V) newValue.value;
        }
    }

    @Override
    public void put(String key, Object value, long expireAfter) {
        store.put(key, this.createValue(value, expireAfter));
    }

    @Override
    public <V> V put(String key, Supplier<V> supplier, long expireAfter) {
        V value = supplier.get();
        store.put(key, this.createValue(value, expireAfter));
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V putIfAbsent(String key, Object value, long expireAfter) {
        TimedCacheValue newValue = store.putIfAbsent(key, this.createValue(value, expireAfter));
        if (newValue == null) {
            return null;
        } else {
            return (V) newValue.value;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V putIfAbsent(String key, Supplier<V> supplier, long expireAfter) {
        TimedCacheValue newValue = store.computeIfAbsent(key, s -> this.createValue(supplier.get(), expireAfter));
        return (V) newValue.value;
    }

    @Override
    public void putAll(Map<String, Object> map) {
        map.forEach((k, v) -> store.put(k, this.createValue(v)));
    }

    @Override
    public <V> V get(String key) {
        return this.getOrDefault(key, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V getOrDefault(String key, V def) {
        TimedCacheValue value = store.get(key);
        if (value == null) {
            return def;
        }
        return (V) value.value;
    }

    @Override
    public boolean containsKey(String key) {
        return store.containsKey(key);
    }

    /**
     * 创建缓存值
     *
     * @param o o
     * @return value
     */
    private TimedCacheValue createValue(Object o) {
        return this.createValue(o, expireAfter);
    }

    /**
     * 创建缓存值
     *
     * @param o           o
     * @param expireAfter expireAfter
     * @return value
     */
    private TimedCacheValue createValue(Object o, long expireAfter) {
        return new TimedCacheValue(System.currentTimeMillis() + expireAfter, o);
    }

    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public ConcurrentHashMap<String, TimedCacheValue> getStore() {
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

    /**
     * 缓存检测
     */
    static class Checker implements Runnable, Closeable {

        private final ConcurrentHashMap<String, TimedCacheValue> store;

        private final int checkInterval;

        private final BiConsumer<String, Object> expiredListener;

        private volatile boolean run;

        public Checker(int checkInterval,
                       ConcurrentHashMap<String, TimedCacheValue> store,
                       BiConsumer<String, Object> expiredListener) {
            this.run = true;
            this.checkInterval = checkInterval;
            this.expiredListener = expiredListener;
            this.store = store;
        }

        @Override
        public void run() {
            while (run) {
                Threads.sleep(checkInterval);
                long curr = System.currentTimeMillis();
                for (String key : store.keySet()) {
                    TimedCacheValue value = store.get(key);
                    if (value.expireTime < curr) {
                        // 删除
                        store.remove(key);
                        // 通知
                        expiredListener.accept(key, value.value);
                    }
                }
            }
        }

        @Override
        public void close() {
            this.run = false;
        }
    }

}

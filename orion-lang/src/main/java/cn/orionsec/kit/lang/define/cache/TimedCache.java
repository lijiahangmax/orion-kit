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
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Threads;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 过期缓存
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/13 12:00
 */
public class TimedCache implements Closeable {

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

    private final ConcurrentHashMap<String, Value> store;

    private final Checker checker;

    public TimedCache(int expireAfter, int checkInterval, BiConsumer<String, Object> expiredListener) {
        this.expireAfter = expireAfter;
        this.store = new ConcurrentHashMap<>();
        this.checker = new Checker(checkInterval, store, expiredListener);
        CHECKER_EXECUTOR.execute(checker);
    }

    /**
     * 添加
     *
     * @param key           key
     * @param valueSupplier valueSupplier
     * @param <V>           V
     * @return V
     */
    @SuppressWarnings("unchecked")
    public <V> V put(String key, Supplier<V> valueSupplier) {
        Value ref = store.computeIfAbsent(key, s -> this.createValue(valueSupplier.get()));
        return (V) ref.value;
    }

    /**
     * 添加
     *
     * @param key           key
     * @param valueSupplier valueSupplier
     * @param <V>           V
     * @return V
     */
    @SuppressWarnings("unchecked")
    public <V> V put(String key, Function<String, V> valueSupplier) {
        Value ref = store.computeIfAbsent(key, s -> this.createValue(valueSupplier.apply(key)));
        return (V) ref.value;
    }

    /**
     * 添加
     *
     * @param key   key
     * @param value value
     */
    public void put(String key, Object value) {
        store.put(key, this.createValue(value));
    }

    /**
     * 添加
     *
     * @param map map
     */
    public void putAll(Map<String, Object> map) {
        map.forEach((k, v) -> store.put(k, this.createValue(v)));
    }

    /**
     * 获取值
     *
     * @param key key
     * @param <V> V
     * @return value
     */
    public <V> V get(String key) {
        return this.getOrDefault(key, null);
    }

    /**
     * 获取值
     *
     * @param key key
     * @param def def
     * @param <V> V
     * @return value
     */
    @SuppressWarnings("unchecked")
    public <V> V getOrDefault(String key, V def) {
        Value value = store.get(key);
        if (value == null) {
            return def;
        }
        return (V) value.value;
    }

    /**
     * 检测是否有此缓存
     *
     * @param key key
     * @return contains
     */
    public boolean containsKey(String key) {
        return store.containsKey(key);
    }

    /**
     * 清空
     */
    public void clear() {
        store.clear();
    }

    public ConcurrentHashMap<String, Value> getStore() {
        return store;
    }

    /**
     * 创建缓存值
     *
     * @param o o
     * @return value
     */
    private Value createValue(Object o) {
        return new Value(System.currentTimeMillis() + expireAfter, o);
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
     * 缓存值
     */
    static class Value {

        /**
         * 过期时间
         */
        private final long expireTime;

        /**
         * 值
         */
        private final Object value;

        public Value(long expireTime, Object value) {
            this.expireTime = expireTime;
            this.value = value;
        }

        @Override
        public String toString() {
            return Objects1.toString(value);
        }
    }

    /**
     * 缓存检测
     */
    static class Checker implements Runnable, Closeable {

        private final ConcurrentHashMap<String, Value> store;

        private final int checkDelay;

        private final BiConsumer<String, Object> expiredListener;

        private volatile boolean run;

        public Checker(int checkDelay,
                       ConcurrentHashMap<String, Value> store,
                       BiConsumer<String, Object> expiredListener) {
            this.run = true;
            this.checkDelay = checkDelay;
            this.expiredListener = expiredListener;
            this.store = store;
        }

        @Override
        public void run() {
            while (run) {
                Threads.sleep(checkDelay);
                long curr = System.currentTimeMillis();
                for (String key : store.keySet()) {
                    Value value = store.get(key);
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

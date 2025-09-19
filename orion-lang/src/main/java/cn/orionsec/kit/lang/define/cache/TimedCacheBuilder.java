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

import cn.orionsec.kit.lang.able.Buildable;
import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.define.thread.ExecutorBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.function.BiConsumer;

/**
 * 过期缓存
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/13 13:42
 */
public class TimedCacheBuilder<T> implements Buildable<TimedCache<T>> {

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

    private static final int DEFAULT_CHECK_INTERVAL = 5000;

    /**
     * 过期时间 ms
     */
    private int expireAfter;

    /**
     * 检测间隔 ms
     */
    private int checkInterval;

    /**
     * 检查线程池
     */
    private Executor checkExecutor;

    /**
     * 过期监听器
     */
    private BiConsumer<String, T> expiredListener;

    private TimedCacheBuilder() {
        this.checkInterval = DEFAULT_CHECK_INTERVAL;
        this.checkExecutor = CHECKER_EXECUTOR;
    }

    /**
     * 创建过期缓存
     *
     * @param <T> T
     * @return cache
     */
    public static <T> TimedCacheBuilder<T> create() {
        return new TimedCacheBuilder<>();
    }

    /**
     * 创建过期缓存
     *
     * @param expireAfter 过期时间
     * @return cache
     */
    public static <T> TimedCache<T> create(int expireAfter) {
        return new TimedCacheBuilder<T>()
                .expireAfter(expireAfter)
                .build();
    }

    /**
     * 创建过期缓存
     *
     * @param expireAfter   过期时间
     * @param checkInterval 检测时间
     * @return cache
     */
    public static <T> TimedCache<T> create(int expireAfter, int checkInterval) {
        return new TimedCacheBuilder<T>()
                .expireAfter(expireAfter)
                .checkInterval(checkInterval)
                .build();
    }

    /**
     * 设置过期时间 ms
     *
     * @param expireAfter expireAfter
     * @return this
     */
    public TimedCacheBuilder<T> expireAfter(int expireAfter) {
        this.expireAfter = expireAfter;
        return this;
    }

    /**
     * 设置检测延迟 ms
     *
     * @param checkInterval checkInterval
     * @return this
     */
    public TimedCacheBuilder<T> checkInterval(int checkInterval) {
        this.checkInterval = checkInterval;
        return this;
    }

    /**
     * 设置检查线程池
     *
     * @param checkExecutor checkExecutor
     * @return this
     */
    public TimedCacheBuilder<T> checkExecutor(Executor checkExecutor) {
        this.checkExecutor = checkExecutor;
        return this;
    }

    /**
     * 过期监听器
     *
     * @param expiredListener expiredListener
     * @return this
     */
    public TimedCacheBuilder<T> expiredListener(BiConsumer<String, T> expiredListener) {
        this.expiredListener = expiredListener;
        return this;
    }

    @Override
    public TimedCache<T> build() {
        Map<String, TimedCacheValue<T>> store = new ConcurrentHashMap<>();
        TimedCacheChecker<T> checker = new TimedCacheChecker<>(
                checkInterval,
                checkExecutor,
                store,
                expiredListener
        );
        return new TimedCacheImpl<>(expireAfter, store, checker);
    }

}

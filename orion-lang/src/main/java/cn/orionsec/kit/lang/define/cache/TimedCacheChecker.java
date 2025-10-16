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

import cn.orionsec.kit.lang.utils.Threads;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

/**
 * 过期缓存检查
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/13 12:00
 */
public class TimedCacheChecker<T> implements Runnable, Closeable {

    private final Map<String, TimedCacheValue<T>> store;

    private final Executor executor;

    private final int checkInterval;

    private final BiConsumer<String, T> expiredListener;

    private volatile boolean run;

    public TimedCacheChecker(int checkInterval,
                             Executor executor,
                             Map<String, TimedCacheValue<T>> store,
                             BiConsumer<String, T> expiredListener) {
        this.run = false;
        this.store = store;
        this.executor = executor;
        this.checkInterval = checkInterval;
        this.expiredListener = expiredListener;
    }

    /**
     * 启动检查
     */
    public void start() {
        this.run = true;
        executor.execute(this);
    }

    @Override
    public void run() {
        while (run) {
            Threads.sleep(checkInterval);
            long curr = System.currentTimeMillis();
            for (String key : store.keySet()) {
                TimedCacheValue<T> value = store.get(key);
                if (value.expireTime < curr) {
                    // 删除
                    store.remove(key);
                    // 通知
                    if (expiredListener != null) {
                        expiredListener.accept(key, value.value);
                    }
                }
            }
        }
    }

    @Override
    public void close() {
        this.run = false;
    }
}

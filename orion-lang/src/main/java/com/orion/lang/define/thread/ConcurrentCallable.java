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
package com.orion.lang.define.thread;

import com.orion.lang.utils.Valid;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * 具有并发特性的Callable
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/7 14:39
 */
public class ConcurrentCallable<V> implements Callable<V> {

    private final Callable<V> c;

    private CyclicBarrier cb;

    private CountDownLatch cd;

    public ConcurrentCallable(Callable<V> c, CyclicBarrier cb) {
        Valid.notNull(c, "callable is null");
        Valid.notNull(cb, "cyclicBarrier is null");
        this.c = c;
        this.cb = cb;
    }

    public ConcurrentCallable(Callable<V> c, CountDownLatch cd) {
        Valid.notNull(c, "callable is null");
        Valid.notNull(cd, "countDownLatch is null");
        this.c = c;
        this.cd = cd;
    }

    @Override
    public V call() throws Exception {
        if (cb != null) {
            cb.await();
        } else if (cd != null) {
            cd.await();
        }
        return c.call();
    }

}

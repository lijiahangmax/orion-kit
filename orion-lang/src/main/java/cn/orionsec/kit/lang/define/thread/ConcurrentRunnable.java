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
package cn.orionsec.kit.lang.define.thread;

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * 可以并发执行的runnable
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/7 11:29
 */
public class ConcurrentRunnable implements Runnable {

    private final Runnable r;

    private CyclicBarrier cb;

    private CountDownLatch cd;

    public ConcurrentRunnable(Runnable r, CyclicBarrier cb) {
        Assert.notNull(r, "runnable is null");
        Assert.notNull(cb, "cyclicBarrier is null");
        this.r = r;
        this.cb = cb;
    }

    public ConcurrentRunnable(Runnable r, CountDownLatch cd) {
        Assert.notNull(r, "runnable is null");
        Assert.notNull(cd, "countDownLatch is null");
        this.r = r;
        this.cd = cd;
    }

    @Override
    public void run() {
        try {
            if (cb != null) {
                cb.await();
            } else if (cd != null) {
                cd.await();
            }
        } catch (Exception e) {
            throw Exceptions.task(e);
        }
        r.run();
    }

}

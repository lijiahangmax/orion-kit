package com.orion.lang.thread;

import com.orion.utils.Exceptions;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * 具有并发特性的Callable
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/7 14:39
 */
public class ConcurrentCallable<V> implements Callable<V> {

    private Callable<V> c;
    private CyclicBarrier cb;
    private CountDownLatch cd;

    public ConcurrentCallable(Callable<V> c, CyclicBarrier cb) {
        this.c = c;
        this.cb = cb;
    }

    public ConcurrentCallable(Callable<V> c, CountDownLatch cd) {
        this.c = c;
        this.cd = cd;
    }

    @Override
    public V call() throws Exception {
        if (c != null) {
            if (cb != null) {
                cb.await();
            } else if (cd != null) {
                cd.await();
            } else {
                throw Exceptions.nullPoint("countDownLatch or cyclicBarrier is null");
            }
        } else {
            throw Exceptions.nullPoint("callable or consumer is null");
        }
        return c.call();
    }

}

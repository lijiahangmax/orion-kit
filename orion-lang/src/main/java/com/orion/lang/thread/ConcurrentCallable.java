package com.orion.lang.thread;

import com.orion.utils.Valid;

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

    private Callable<V> c;

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

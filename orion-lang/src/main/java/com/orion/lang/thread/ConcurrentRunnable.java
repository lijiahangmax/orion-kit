package com.orion.lang.thread;

import com.orion.utils.Exceptions;
import com.orion.utils.Valid;

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

    private Runnable r;

    private CyclicBarrier cb;

    private CountDownLatch cd;

    public ConcurrentRunnable(Runnable r, CyclicBarrier cb) {
        Valid.notNull(r, "runnable is null");
        Valid.notNull(cb, "cyclicBarrier is null");
        this.r = r;
        this.cb = cb;
    }

    public ConcurrentRunnable(Runnable r, CountDownLatch cd) {
        Valid.notNull(r, "runnable is null");
        Valid.notNull(cd, "countDownLatch is null");
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

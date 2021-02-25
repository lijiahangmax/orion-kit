package com.orion.lang.thread;

import com.orion.utils.Exceptions;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * 可以并发执行的runnable
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/7 11:29
 */
public class ConcurrentRunnable implements Runnable {

    private Runnable r;
    private CyclicBarrier cb;
    private CountDownLatch cd;

    public ConcurrentRunnable(Runnable r, CyclicBarrier cb) {
        this.r = r;
        this.cb = cb;
    }

    public ConcurrentRunnable(Runnable r, CountDownLatch cd) {
        this.r = r;
        this.cd = cd;
    }

    @Override
    public void run() {
        if (r != null) {
            if (cb != null) {
                try {
                    cb.await();
                } catch (Exception e) {
                    throw Exceptions.task(e);
                }
            } else if (cd != null) {
                try {
                    cd.await();
                } catch (Exception e) {
                    throw Exceptions.task(e);
                }
            } else {
                throw Exceptions.nullPoint("cyclicBarrier or countDownLatch is null");
            }
        } else {
            throw Exceptions.nullPoint("runnable or consumer is null");
        }
        r.run();
    }

}

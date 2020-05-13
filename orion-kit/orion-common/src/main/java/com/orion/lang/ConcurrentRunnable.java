package com.orion.lang;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * 可以并发执行的runnable
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/7 11:29
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
                    throw new RuntimeException(e);
                }
            } else if (cd != null) {
                try {
                    cd.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new NullPointerException("cyclicBarrier or countDownLatch is null");
            }
        } else {
            throw new NullPointerException("runnable or consumer is null");
        }
        r.run();
    }

}

package com.orion.lang.collect;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 固长队列
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/11/6 11:30
 */
public class LimitQueue<E> extends ConcurrentLinkedQueue<E> {

    private int limit;

    public LimitQueue(int limit) {
        if (limit == 0) {
            this.limit = 10;
        } else {
            this.limit = limit;
        }
    }

    @Override
    public boolean offer(E s) {
        if (size() > limit) {
            poll();
        }
        return super.offer(s);
    }

}

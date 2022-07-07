package com.orion.lang.define.collect;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 固长队列
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/6 11:30
 */
public class FixedQueue<E> extends ConcurrentLinkedQueue<E> implements Serializable {

    private static final long serialVersionUID = -12908043940801293L;

    private final int limit;

    public FixedQueue(int limit) {
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

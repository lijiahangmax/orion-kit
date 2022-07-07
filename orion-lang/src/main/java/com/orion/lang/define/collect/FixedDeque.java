package com.orion.lang.define.collect;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 固长队列
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/6 11:30
 */
public class FixedDeque<E> extends ConcurrentLinkedDeque<E> implements Serializable {

    private static final long serialVersionUID = 923412312354068942L;

    private int limit;

    public FixedDeque(int limit) {
        if (limit == 0) {
            this.limit = 10;
        } else {
            this.limit = limit;
        }
    }

    @Override
    public boolean offerFirst(E s) {
        if (full()) {
            pollLast();
        }
        return super.offerFirst(s);
    }

    @Override
    public boolean offerLast(E s) {
        if (full()) {
            pollFirst();
        }
        return super.offerLast(s);
    }

    public boolean full() {
        return size() > limit;
    }

}

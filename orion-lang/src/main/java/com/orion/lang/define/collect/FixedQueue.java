package com.orion.lang.define.collect;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Valid;

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

    private final int maxSize;

    public FixedQueue(int maxSize) {
        this.maxSize = Valid.gt(maxSize, Const.N_0);
    }

    public static <E> FixedQueue<E> create(int maxSize) {
        return new FixedQueue<>(maxSize);
    }

    @Override
    public boolean offer(E s) {
        if (this.isFull()) {
            // 首位出
            this.poll();
        }
        return super.offer(s);
    }

    /**
     * 队列是否已满
     *
     * @return 是否已满
     */
    public boolean isFull() {
        return this.size() >= maxSize;
    }

}

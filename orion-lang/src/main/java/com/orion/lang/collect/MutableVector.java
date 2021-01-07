package com.orion.lang.collect;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

/**
 * 可转换的Vector
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/5 17:45
 */
public class MutableVector<E> extends Vector<E> implements MutableList<E>, Serializable {

    private static final long serialVersionUID = 8976493198238094L;

    public MutableVector() {
        super();
    }

    public MutableVector(Collection<? extends E> c) {
        super(c);
    }

    public MutableVector(int initialCapacity) {
        super(initialCapacity);
    }

    public MutableVector(int initialCapacity, int capacityIncrement) {
        super(initialCapacity, capacityIncrement);
    }

}

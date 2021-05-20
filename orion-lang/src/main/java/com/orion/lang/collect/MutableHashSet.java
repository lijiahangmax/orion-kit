package com.orion.lang.collect;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * 可转换的HashSet
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/2/29 20:50
 */
public class MutableHashSet<E> extends HashSet<E> implements MutableSet<E>, Serializable {

    private static final long serialVersionUID = 9982378111284539L;

    public MutableHashSet() {
        super();
    }

    public MutableHashSet(Collection<? extends E> c) {
        super(c);
    }

    public MutableHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MutableHashSet(int initialCapacity) {
        super(initialCapacity);
    }

}

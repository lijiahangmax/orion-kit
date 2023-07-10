package com.orion.lang.define.collect;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * 可转换的 LinkedHashSet
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/5 17:42
 */
public class MutableLinkedHashSet<E> extends LinkedHashSet<E> implements MutableSet<E>, Serializable {

    private static final long serialVersionUID = -822822893457969823L;

    public MutableLinkedHashSet() {
        super();
    }

    public MutableLinkedHashSet(Collection<? extends E> c) {
        super(c);
    }

    public MutableLinkedHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MutableLinkedHashSet(int initialCapacity) {
        super(initialCapacity);
    }

    public static <E> MutableLinkedHashSet<E> create() {
        return new MutableLinkedHashSet<>();
    }

    public static <E> MutableLinkedHashSet<E> create(Collection<? extends E> c) {
        return new MutableLinkedHashSet<>(c);
    }

}

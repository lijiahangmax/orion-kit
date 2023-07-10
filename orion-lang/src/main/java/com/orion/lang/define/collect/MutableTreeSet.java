package com.orion.lang.define.collect;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 可转换的 TreeSet
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/5 17:36
 */
public class MutableTreeSet<E> extends TreeSet<E> implements MutableSet<E>, Serializable {

    private static final long serialVersionUID = -9345897822739429L;

    public MutableTreeSet() {
        super();
    }

    public MutableTreeSet(Comparator<? super E> comparator) {
        super(comparator);
    }

    public MutableTreeSet(Collection<? extends E> c) {
        super(c);
    }

    public MutableTreeSet(SortedSet<E> s) {
        super(s);
    }

    public static <E> MutableTreeSet<E> create() {
        return new MutableTreeSet<>();
    }

    public static <E> MutableTreeSet<E> create(Comparator<? super E> comparator) {
        return new MutableTreeSet<>(comparator);
    }

    public static <E> MutableTreeSet<E> create(Collection<? extends E> c) {
        return new MutableTreeSet<>(c);
    }

}

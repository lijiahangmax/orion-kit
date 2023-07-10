package com.orion.lang.define.collect;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * 可转换的 LinkedList
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/5 17:35
 */
public class MutableLinkedList<E> extends LinkedList<E> implements MutableList<E>, Serializable {

    private static final long serialVersionUID = 112934589387485912L;

    public MutableLinkedList() {
        super();
    }

    public MutableLinkedList(Collection<? extends E> c) {
        super(c);
    }

    public static <E> MutableLinkedList<E> create() {
        return new MutableLinkedList<>();
    }

    public static <E> MutableLinkedList<E> create(Collection<? extends E> c) {
        return new MutableLinkedList<>(c);
    }

}

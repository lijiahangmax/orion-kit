package com.orion.lang.define.collect;

import com.orion.lang.utils.Exceptions;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 空元素集合
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/22 17:20
 */
public class EmptySet<E> extends AbstractSet<E> implements Serializable {

    public static final EmptySet<?> EMPTY = new EmptySet<>();

    private static final long serialVersionUID = 7489127391029385L;

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean contains(Object obj) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c.isEmpty();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length > 0) {
            a[0] = null;
        }
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public E next() {
                throw Exceptions.noSuchElement();
            }
        };
    }

    @Override
    public void forEach(Consumer<? super E> action) {
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return false;
    }

}
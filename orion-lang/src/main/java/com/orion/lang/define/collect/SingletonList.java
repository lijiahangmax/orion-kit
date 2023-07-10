package com.orion.lang.define.collect;

import com.orion.lang.define.iterator.SingletonIterator;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Objects1;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 单元素集合
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/22 14:09
 */
public class SingletonList<E> extends AbstractList<E> implements RandomAccess, Serializable {

    private static final long serialVersionUID = -945902293805399L;

    private final E element;

    public SingletonList(E o) {
        element = o;
    }

    public static <E> SingletonList<E> create(E o) {
        return new SingletonList<>(o);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean contains(Object obj) {
        return Objects1.eq(obj, element);
    }

    @Override
    public E get(int index) {
        if (index != 0) {
            throw Exceptions.index("index: " + index + ", size: 1");
        }
        return element;
    }

    @Override
    public Iterator<E> iterator() {
        return new SingletonIterator<>(element);
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        action.accept(element);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        throw Exceptions.unsupported();
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        throw Exceptions.unsupported();
    }

    @Override
    public void sort(Comparator<? super E> c) {
    }

}

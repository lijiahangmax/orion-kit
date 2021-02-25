package com.orion.lang.collect;

import com.orion.lang.iterator.SingletonIterator;
import com.orion.utils.Exceptions;
import com.orion.utils.Objects1;

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
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/22 14:09
 */
public class SingletonList<E> extends AbstractList<E> implements RandomAccess, Serializable {

    private static final long serialVersionUID = -945902293805399L;

    private final E element;

    public SingletonList(E o) {
        element = o;
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
        throw Exceptions.unSupport();
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        throw Exceptions.unSupport();
    }

    @Override
    public void sort(Comparator<? super E> c) {
    }

}

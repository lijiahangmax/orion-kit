package com.orion.lang.define.iterator;

import com.orion.lang.utils.Exceptions;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 单元素迭代器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/22 14:17
 */
public class SingletonIterator<E> implements Iterator<E>, Iterable<E> {

    private final E element;

    private boolean hasNext = true;

    public SingletonIterator(E element) {
        this.element = element;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public E next() {
        if (hasNext) {
            hasNext = false;
            return element;
        }
        throw Exceptions.noSuchElement();
    }

    @Override
    public void remove() {
        throw Exceptions.unsupported();
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        if (hasNext) {
            action.accept(element);
            hasNext = false;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }

}

package com.orion.lang.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 单元素迭代器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/22 14:17
 */
public class SingletonIterator<E> implements Iterator<E>, Iterable<E> {

    private final E element;

    public SingletonIterator(E element) {
        this.element = element;
    }

    private boolean hasNext = true;

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
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
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

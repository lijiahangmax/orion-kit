package com.orion.lang.define.collect;

import com.orion.lang.define.iterator.SingletonIterator;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Objects1;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 单元素集合
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/22 14:09
 */
public class SingletonSet<E> extends AbstractSet<E> implements Serializable {

    private static final long serialVersionUID = 1283912054341293L;

    private final E element;

    public SingletonSet(E obj) {
        element = obj;
    }

    public static <E> SingletonSet<E> create(E obj) {
        return new SingletonSet<>(obj);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean contains(Object obj) {
        return Objects1.eq(obj, element);
    }

    public E get() {
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

}

/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.define.collect;

import cn.orionsec.kit.lang.utils.Exceptions;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 空元素集合
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/22 14:09
 */
public class EmptyList<E> extends AbstractList<E> implements RandomAccess, Serializable {

    public static final EmptyList<?> EMPTY = new EmptyList<>();

    private static final long serialVersionUID = -4359234034983487L;

    @Override
    public int size() {
        return 0;
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
    public E get(int index) {
        throw Exceptions.index("index: " + index + ", size: 0");
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
        throw Exceptions.unsupported();
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        throw Exceptions.unsupported();
    }

    @Override
    public void sort(Comparator<? super E> c) {
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof List) && ((List<?>) o).isEmpty();
    }

    @Override
    public int hashCode() {
        return 1;
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

}

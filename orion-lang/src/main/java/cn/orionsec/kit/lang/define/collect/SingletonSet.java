/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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

import cn.orionsec.kit.lang.define.iterator.SingletonIterator;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Objects1;

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

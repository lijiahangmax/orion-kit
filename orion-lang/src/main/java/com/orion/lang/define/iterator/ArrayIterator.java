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
package com.orion.lang.define.iterator;

import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Exceptions;

import java.io.Serializable;
import java.util.Iterator;

/**
 * 数组迭代器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/17 12:54
 */
public class ArrayIterator<E> implements Iterator<E>, Iterable<E>, Serializable {

    private static final long serialVersionUID = 1832735384832234L;

    /**
     * 数组
     */
    private final E[] array;

    /**
     * 起始位置
     */
    private int startIndex;

    /**
     * 结束位置
     */
    private int endIndex;

    /**
     * 当前位置
     */
    private int index;

    public ArrayIterator(E[] array) {
        this(array, 0);
    }

    public ArrayIterator(E[] array, int startIndex) {
        this(array, startIndex, -1);
    }

    public ArrayIterator(E[] array, int startIndex, int endIndex) {
        this.endIndex = Arrays1.length(array);
        if (endIndex > 0 && endIndex < this.endIndex) {
            this.endIndex = endIndex;
        }

        if (startIndex >= 0 && startIndex < this.endIndex) {
            this.startIndex = startIndex;
        }
        this.array = array;
        this.index = this.startIndex;
    }

    @Override
    public boolean hasNext() {
        return (index < endIndex);
    }

    @Override
    public E next() {
        if (hasNext()) {
            return array[index++];
        }
        throw Exceptions.noSuchElement();
    }

    @Override
    public void remove() {
        throw Exceptions.unsupported("this is a read-only iterator");
    }

    /**
     * 重置索引
     */
    public void reset() {
        this.index = this.startIndex;
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }

    public E[] getArray() {
        return array;
    }

}

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
package com.orion.lang.define.collect;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.math.Numbers;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 分片 set
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/1/16 16:40
 */
public class PartitionSet<E> extends AbstractSet<Set<E>> implements Iterator<Set<E>> {

    private final Iterator<E> iterator;

    private final int size;

    private final int totalSize;

    private int current;

    public PartitionSet(Set<E> set, int size) {
        this.iterator = set.iterator();
        this.size = size;
        this.totalSize = set.size();
    }

    public static <E> PartitionSet<E> create(Set<E> set, int size) {
        return new PartitionSet<>(set, size);
    }

    @Override
    public int size() {
        return (totalSize + size - 1) / size;
    }

    @Override
    public boolean isEmpty() {
        return totalSize == 0;
    }

    @Override
    public Iterator<Set<E>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return this.size() > current;
    }

    @Override
    public Set<E> next() {
        if (!this.hasNext()) {
            throw Exceptions.noSuchElement("there are no more elements");
        }
        current++;
        Set<E> set = new HashSet<>(Numbers.getMin2Power(size));
        for (int i = 0; i < size; i++) {
            if (iterator.hasNext()) {
                set.add(iterator.next());
            } else {
                break;
            }
        }
        return set;
    }

}
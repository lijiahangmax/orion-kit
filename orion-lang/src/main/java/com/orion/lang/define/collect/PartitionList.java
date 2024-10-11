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

import com.orion.lang.utils.Valid;

import java.util.AbstractList;
import java.util.List;

/**
 * 分片 list
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/1/16 16:26
 */
public class PartitionList<E> extends AbstractList<List<E>> {

    private final List<E> list;

    private final int size;

    public PartitionList(List<E> list, int size) {
        this.list = list;
        this.size = size;
    }

    public static <E> PartitionList<E> create(List<E> list, int size) {
        return new PartitionList<>(list, size);
    }

    @Override
    public List<E> get(int index) {
        int listSize = this.size();
        Valid.gte(index, 0, "index {} must not be negative", index);
        Valid.lt(index, listSize, "index {} must be less than size {}", index, listSize);
        int start = index * size;
        int end = Math.min(start + size, list.size());
        return list.subList(start, end);
    }

    @Override
    public int size() {
        return (list.size() + size - 1) / size;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

}
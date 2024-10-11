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

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Valid;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 固长集合
 * <p>
 * 添加元素时超长则会删除头部元素
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/7/1 16:54
 */
public class FixedArrayList<E> extends ArrayList<E> {

    /**
     * 最大数量
     */
    private final int maxSize;

    public FixedArrayList(int maxSize) {
        super();
        this.maxSize = Valid.gt(maxSize, Const.N_0);
    }

    public static <E> FixedArrayList<E> create(int maxSize) {
        return new FixedArrayList<>(maxSize);
    }

    @Override
    public boolean add(E e) {
        this.removeIfFull(1);
        return super.add(e);
    }

    @Override
    public void add(int index, E element) {
        this.removeIfFull(1);
        super.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Valid.lte(c.size(), maxSize);
        this.removeIfFull(c.size());
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        Valid.lte(c.size(), maxSize);
        this.removeIfFull(c.size());
        return super.addAll(index, c);
    }

    /**
     * 如果集合已满则删除
     *
     * @param count 添加数量
     */
    private void removeIfFull(int count) {
        int newSize = this.size() + count;
        if (newSize > maxSize) {
            this.subList(0, newSize - maxSize).clear();
        }
    }

}

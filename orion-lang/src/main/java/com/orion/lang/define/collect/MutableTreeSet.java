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

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 可转换的 TreeSet
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/5 17:36
 */
public class MutableTreeSet<E> extends TreeSet<E> implements MutableSet<E>, Serializable {

    private static final long serialVersionUID = -9345897822739429L;

    public MutableTreeSet() {
        super();
    }

    public MutableTreeSet(Comparator<? super E> comparator) {
        super(comparator);
    }

    public MutableTreeSet(Collection<? extends E> c) {
        super(c);
    }

    public MutableTreeSet(SortedSet<E> s) {
        super(s);
    }

    public static <E> MutableTreeSet<E> create() {
        return new MutableTreeSet<>();
    }

    public static <E> MutableTreeSet<E> create(Comparator<? super E> comparator) {
        return new MutableTreeSet<>(comparator);
    }

    public static <E> MutableTreeSet<E> create(Collection<? extends E> c) {
        return new MutableTreeSet<>(c);
    }

}

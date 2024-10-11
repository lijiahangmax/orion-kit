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
import java.util.HashSet;

/**
 * 可转换的 HashSet
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/2/29 20:50
 */
public class MutableHashSet<E> extends HashSet<E> implements MutableSet<E>, Serializable {

    private static final long serialVersionUID = 9982378111284539L;

    public MutableHashSet() {
        super();
    }

    public MutableHashSet(Collection<? extends E> c) {
        super(c);
    }

    public MutableHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MutableHashSet(int initialCapacity) {
        super(initialCapacity);
    }

    public static <E> MutableHashSet<E> create() {
        return new MutableHashSet<>();
    }

    public static <E> MutableHashSet<E> create(Collection<? extends E> c) {
        return new MutableHashSet<>(c);
    }

}

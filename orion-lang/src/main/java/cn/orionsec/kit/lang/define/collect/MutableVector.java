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

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

/**
 * 可转换的 Vector
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/5 17:45
 */
public class MutableVector<E> extends Vector<E> implements MutableList<E>, Serializable {

    private static final long serialVersionUID = 8976493198238094L;

    public MutableVector() {
        super();
    }

    public MutableVector(Collection<? extends E> c) {
        super(c);
    }

    public MutableVector(int initialCapacity) {
        super(initialCapacity);
    }

    public MutableVector(int initialCapacity, int capacityIncrement) {
        super(initialCapacity, capacityIncrement);
    }

    public static <E> MutableVector<E> create() {
        return new MutableVector<>();
    }

    public static <E> MutableVector<E> create(Collection<? extends E> c) {
        return new MutableVector<>(c);
    }

}

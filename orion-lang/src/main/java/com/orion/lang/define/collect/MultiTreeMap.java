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
import java.util.Comparator;
import java.util.TreeMap;

/**
 * MultiTreeMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/19 16:56
 */
public class MultiTreeMap<K, V, E> extends TreeMap<K, TreeMap<V, E>>
        implements MultiMap<K, V, E, TreeMap<V, E>>, Serializable {

    private static final long serialVersionUID = -786438489922021L;

    /**
     * value 比较器
     */
    private Comparator<? super V> valueComparator;

    public MultiTreeMap() {
    }

    public MultiTreeMap(Comparator<? super K> comparator) {
        super(comparator);
    }

    public MultiTreeMap(Comparator<? super K> comparator, Comparator<? super V> valueComparator) {
        super(comparator);
        this.valueComparator = valueComparator;
    }

    public static <K, V, E> MultiTreeMap<K, V, E> create() {
        return new MultiTreeMap<>();
    }

    public static <K, V, E> MultiTreeMap<K, V, E> create(Comparator<? super K> comparator) {
        return new MultiTreeMap<>(comparator);
    }

    public static <K, V, E> MultiTreeMap<K, V, E> create(Comparator<? super K> comparator, Comparator<? super V> keyComparator) {
        return new MultiTreeMap<>(comparator, keyComparator);
    }

    /**
     * 设置 value 比较器
     *
     * @param valueComparator valueComparator
     */
    public void valueComparator(Comparator<? super V> valueComparator) {
        this.valueComparator = valueComparator;
    }

    @Override
    public TreeMap<V, E> computeSpace(K e) {
        return super.computeIfAbsent(e, k -> new TreeMap<V, E>(valueComparator));
    }

}

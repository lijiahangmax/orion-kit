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
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 可转换的 TreeMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/5 17:37
 */
public class MutableTreeMap<K, V> extends TreeMap<K, V> implements MutableMap<K, V>, Serializable {

    private static final long serialVersionUID = 89347891239048579L;

    public MutableTreeMap() {
        super();
    }

    public MutableTreeMap(Comparator<? super K> comparator) {
        super(comparator);
    }

    public MutableTreeMap(SortedMap<K, ? extends V> m) {
        super(m);
    }

    public MutableTreeMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public static <K, V> MutableTreeMap<K, V> create() {
        return new MutableTreeMap<>();
    }

    public static <K, V> MutableTreeMap<K, V> create(Map<? extends K, ? extends V> m) {
        return new MutableTreeMap<>(m);
    }

}

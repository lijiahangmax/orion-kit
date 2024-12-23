/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 可转换的 LinkedHashMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/15 10:34
 */
public class MutableLinkedHashMap<K, V> extends LinkedHashMap<K, V> implements MutableMap<K, V>, Serializable {

    private static final long serialVersionUID = 876812409459012839L;

    public MutableLinkedHashMap() {
        super();
    }

    public MutableLinkedHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public MutableLinkedHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MutableLinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public MutableLinkedHashMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public static <K, V> MutableLinkedHashMap<K, V> create() {
        return new MutableLinkedHashMap<>();
    }

    public static <K, V> MutableLinkedHashMap<K, V> create(Map<? extends K, ? extends V> m) {
        return new MutableLinkedHashMap<>(m);
    }

}

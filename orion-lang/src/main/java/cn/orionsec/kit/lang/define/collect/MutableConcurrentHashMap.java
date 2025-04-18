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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 可转换的ConcurrentHashMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/2/27 1:03
 */
public class MutableConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> implements MutableMap<K, V>, Serializable {

    private static final long serialVersionUID = 142731998248615024L;

    public MutableConcurrentHashMap() {
        super();
    }

    public MutableConcurrentHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MutableConcurrentHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public MutableConcurrentHashMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public static <K, V> MutableConcurrentHashMap<K, V> create() {
        return new MutableConcurrentHashMap<>();
    }

    public static <K, V> MutableConcurrentHashMap<K, V> create(Map<? extends K, ? extends V> m) {
        return new MutableConcurrentHashMap<>(m);
    }

}

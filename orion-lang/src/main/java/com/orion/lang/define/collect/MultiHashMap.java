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

import java.io.Serializable;
import java.util.HashMap;

/**
 * MultiHashMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/19 16:56
 */
public class MultiHashMap<K, V, E> extends HashMap<K, HashMap<V, E>>
        implements MultiMap<K, V, E, HashMap<V, E>>, Serializable {

    private static final long serialVersionUID = 4834450551128977L;

    /**
     * value 初始化空间
     */
    private int valueInitialCapacity;

    public MultiHashMap() {
        this(Const.CAPACITY_16, Const.CAPACITY_16);
    }

    public MultiHashMap(int initialCapacity) {
        this(initialCapacity, initialCapacity);
    }

    public MultiHashMap(int keyInitialCapacity, int valueInitialCapacity) {
        super(keyInitialCapacity);
        this.valueInitialCapacity = valueInitialCapacity;
    }

    public static <E, K, V> MultiHashMap<E, K, V> create() {
        return new MultiHashMap<>();
    }

    /**
     * 设置 value 初始化空间
     *
     * @param valueInitialCapacity value 初始化空间
     */
    public void valueInitialCapacity(int valueInitialCapacity) {
        this.valueInitialCapacity = valueInitialCapacity;
    }

    @Override
    public HashMap<V, E> computeSpace(K e) {
        return super.computeIfAbsent(e, k -> new HashMap<V, E>(valueInitialCapacity));
    }

}

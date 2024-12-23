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

import cn.orionsec.kit.lang.constant.Const;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MultiConcurrentHashMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/19 16:39
 */
public class MultiConcurrentHashMap<K, V, E> extends ConcurrentHashMap<K, ConcurrentHashMap<V, E>>
        implements MultiMap<K, V, E, ConcurrentHashMap<V, E>>, Serializable {

    private static final long serialVersionUID = 8455892712354974891L;

    /**
     * value 初始化空间
     */
    private int valueInitialCapacity;

    public MultiConcurrentHashMap() {
        this(Const.CAPACITY_16, Const.CAPACITY_16);
    }

    public MultiConcurrentHashMap(int initialCapacity) {
        this(initialCapacity, initialCapacity);
    }

    public MultiConcurrentHashMap(int keyInitialCapacity, int valueInitialCapacity) {
        super(keyInitialCapacity);
        this.valueInitialCapacity = valueInitialCapacity;
    }

    public static <K, V, E> MultiConcurrentHashMap<K, V, E> create() {
        return new MultiConcurrentHashMap<>();
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
    public ConcurrentHashMap<V, E> computeSpace(K e) {
        return super.computeIfAbsent(e, k -> new ConcurrentHashMap<>(valueInitialCapacity));
    }

}


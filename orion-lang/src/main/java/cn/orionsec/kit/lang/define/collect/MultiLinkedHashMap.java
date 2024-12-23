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
import java.util.LinkedHashMap;

/**
 * MultiLinkedHashMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/19 16:56
 */
public class MultiLinkedHashMap<K, V, E> extends LinkedHashMap<K, LinkedHashMap<V, E>>
        implements MultiMap<K, V, E, LinkedHashMap<V, E>>, Serializable {

    private static final long serialVersionUID = 1237895897912782748L;

    private static final float DEFAULT_LOAD_FACTOR = 0.75F;

    /**
     * value 初始化空间
     */
    private int valueInitialCapacity;

    /**
     * value 负载因子
     */
    private float valueLoadFactor;

    /**
     * value 排序模式
     */
    private boolean valueAccessOrder;

    public MultiLinkedHashMap() {
        this(Const.CAPACITY_16, DEFAULT_LOAD_FACTOR, false);
    }

    public MultiLinkedHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, false);
    }

    public MultiLinkedHashMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, false);
    }

    public MultiLinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
        this.valueInitialCapacity = initialCapacity;
        this.valueLoadFactor = loadFactor;
        this.valueAccessOrder = accessOrder;
    }

    public static <K, V, E> MultiLinkedHashMap<K, V, E> create() {
        return new MultiLinkedHashMap<>();
    }

    public static <K, V, E> MultiLinkedHashMap<K, V, E> create(int initialCapacity, float loadFactor, boolean accessOrder) {
        return new MultiLinkedHashMap<>(initialCapacity, loadFactor, accessOrder);
    }

    public void valueCapacity(int valueInitialCapacity) {
        this.valueCapacity(valueInitialCapacity, DEFAULT_LOAD_FACTOR, false);
    }

    public void valueCapacity(int valueInitialCapacity, float valueLoadFactor) {
        this.valueCapacity(valueInitialCapacity, valueLoadFactor, false);
    }

    /**
     * 设置 value 初始化参数
     *
     * @param valueInitialCapacity 初始化空间
     * @param valueLoadFactor      负载因子
     * @param valueAccessOrder     排序模式
     */
    public void valueCapacity(int valueInitialCapacity, float valueLoadFactor, boolean valueAccessOrder) {
        this.valueInitialCapacity = valueInitialCapacity;
        this.valueLoadFactor = valueLoadFactor;
        this.valueAccessOrder = valueAccessOrder;
    }

    @Override
    public LinkedHashMap<V, E> computeSpace(K e) {
        return super.computeIfAbsent(e, k -> new LinkedHashMap<V, E>(valueInitialCapacity, valueLoadFactor, valueAccessOrder));
    }

}

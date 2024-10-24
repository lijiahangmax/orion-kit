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
package com.orion.lang.define.wrapper;

import com.orion.lang.define.support.CloneSupport;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * Map 的 Entry 实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/8/23 10:40
 */
public class Pair<K, V> extends CloneSupport<Pair<K, V>> implements Map.Entry<K, V>, Serializable {

    private static final long serialVersionUID = 3797556461612462L;

    private K key;

    private V value;

    public Pair() {
    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Pair(Map.Entry<K, V> entry) {
        this.key = entry.getKey();
        this.value = entry.getValue();
    }

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    public static <K, V> Pair<K, V> of(Map.Entry<K, V> entry) {
        return new Pair<>(entry);
    }

    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public V setValue(V value) {
        this.value = value;
        return value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    /**
     * @return key Optional
     */
    public Optional<K> keyOptional() {
        return Optional.ofNullable(key);
    }

    /**
     * @return value Optional
     */
    public Optional<V> valueOptional() {
        return Optional.ofNullable(value);
    }

    @Override
    public int hashCode() {
        return key.hashCode() ^ value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        Object k, v;
        Map.Entry<?, ?> e;
        return ((o instanceof Map.Entry)
                && (k = (e = (Map.Entry<?, ?>) o).getKey()) != null
                && (v = e.getValue()) != null
                && (k == key || k.equals(key))
                && (v == value || v.equals(value)));
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

}

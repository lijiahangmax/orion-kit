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

import cn.orionsec.kit.lang.utils.Exceptions;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 空元素 map
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/22 17:24
 */
public class EmptyMap<K, V> extends AbstractMap<K, V> implements Serializable {

    public static final EmptyMap<?, ?> EMPTY = new EmptyMap<>();

    private static final long serialVersionUID = 7432616076500907729L;

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keySet() {
        return (Set<K>) EmptySet.EMPTY;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<V> values() {
        return (Set<V>) EmptySet.EMPTY;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return (Set<Map.Entry<K, V>>) EmptySet.EMPTY;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Map) && ((Map<?, ?>) o).isEmpty();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public V getOrDefault(Object k, V defaultValue) {
        return defaultValue;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Objects.requireNonNull(function);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        throw Exceptions.unsupported();
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw Exceptions.unsupported();
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        throw Exceptions.unsupported();
    }

    @Override
    public V replace(K key, V value) {
        throw Exceptions.unsupported();
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        throw Exceptions.unsupported();
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw Exceptions.unsupported();
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw Exceptions.unsupported();
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        throw Exceptions.unsupported();
    }

}

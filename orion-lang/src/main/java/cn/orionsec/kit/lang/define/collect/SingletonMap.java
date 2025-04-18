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

import cn.orionsec.kit.lang.define.wrapper.Pair;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Objects1;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 单元素 map
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/22 14:24
 */
public class SingletonMap<K, V> extends AbstractMap<K, V> implements Serializable {

    private static final long serialVersionUID = 2238919328937489523L;

    private final K k;

    private final V v;

    private transient Set<K> keySet;

    private transient Set<Map.Entry<K, V>> entrySet;

    private transient Collection<V> values;

    public SingletonMap(K key, V value) {
        k = key;
        v = value;
    }

    public static <K, V> SingletonMap<K, V> create(K key, V value) {
        return new SingletonMap<>(key, value);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return Objects1.eq(key, k);
    }

    @Override
    public boolean containsValue(Object value) {
        return Objects1.eq(value, v);
    }

    @Override
    public V get(Object key) {
        return (Objects1.eq(key, k) ? v : null);
    }

    @Override
    public Set<K> keySet() {
        if (keySet == null) {
            keySet = new SingletonSet<>(k);
        }
        return keySet;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new SingletonSet<>(new Pair<>(k, v));
        }
        return entrySet;
    }

    @Override
    public Collection<V> values() {
        if (values == null) {
            values = new SingletonSet<>(v);
        }
        return values;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return Objects1.eq(key, k) ? v : defaultValue;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        action.accept(k, v);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        throw Exceptions.unsupported();
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

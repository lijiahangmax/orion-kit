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

import java.util.*;
import java.util.function.BiConsumer;

/**
 * MultiMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/2 13:52
 */
public interface MultiMap<K, V, E, M extends Map<V, E>> extends Map<K, M> {

    /**
     * 开辟空间
     *
     * @param k e
     * @return map
     */
    M computeSpace(K k);

    /**
     * 插入元素
     *
     * @param key   key
     * @param value value
     * @param elem  elem
     * @return elem
     */
    default E put(K key, V value, E elem) {
        return this.computeSpace(key).put(value, elem);
    }

    /**
     * 获取元素
     *
     * @param key key
     * @return elemMap
     */
    default Map<V, E> getOrDefault(K key) {
        // 开辟空间
        return this.computeSpace(key);
    }

    /**
     * 插入全部元素
     *
     * @param key   key
     * @param value value
     */
    default void putAll(K key, Map<V, E> value) {
        this.computeSpace(key).putAll(value);
    }

    /**
     * 移除元素
     *
     * @param key   key
     * @param value value
     * @return elem
     */
    default E removeElement(K key, V value) {
        Map<V, E> r = this.get(key);
        if (r == null || r.isEmpty()) {
            return null;
        }
        return r.remove(value);
    }

    /**
     * 移除元素
     *
     * @param key   key
     * @param value value
     * @param elem  elem
     * @return elem
     */
    default boolean removeElement(K key, V value, E elem) {
        Map<V, E> r = this.get(key);
        if (r == null || r.isEmpty()) {
            return false;
        }
        return r.remove(value, elem);
    }

    /**
     * 获取元素
     *
     * @param key   key
     * @param value value
     * @return 元素
     */
    default E get(K key, V value) {
        Map<V, E> r = this.get(key);
        if (r == null || r.isEmpty()) {
            return null;
        }
        return r.get(value);
    }

    /**
     * 获取元素
     *
     * @param key   key
     * @param value value
     * @param elem  elem
     * @return 元素
     */
    default E getOrDefault(K key, V value, E elem) {
        Map<V, E> r = this.get(key);
        if (r == null || r.isEmpty()) {
            return elem;
        }
        return r.getOrDefault(value, elem);
    }

    /**
     * 获取元素数量
     *
     * @param key key
     * @return 元素数量
     */
    default int size(K key) {
        Map<V, E> r = this.get(key);
        if (r == null || r.isEmpty()) {
            return 0;
        }
        return r.size();
    }

    /**
     * 元素是否为空
     *
     * @param key key
     * @return 是否为空
     */
    default boolean isEmpty(K key) {
        Map<V, E> r = this.get(key);
        if (r == null) {
            return true;
        }
        return r.isEmpty();
    }

    /**
     * 清空元素
     *
     * @param key key
     */
    default void clear(K key) {
        Map<V, E> r = this.get(key);
        if (r != null) {
            r.clear();
        }
    }

    /**
     * 是否包含元素
     *
     * @param key   key
     * @param value value
     * @return 是否包含
     */
    default boolean containsKey(K key, V value) {
        Map<V, E> r = this.get(key);
        if (r == null || r.isEmpty()) {
            return false;
        }
        return r.containsKey(value);
    }

    /**
     * 获取所有元素
     *
     * @param key key
     * @return 所有元素
     */
    default Collection<E> values(K key) {
        Map<V, E> r = this.get(key);
        if (r == null || r.isEmpty()) {
            return new ArrayList<>();
        }
        return r.values();
    }

    /**
     * 获取所有元素
     *
     * @param key key
     * @return 元素
     */
    default Set<Map.Entry<V, E>> entrySet(K key) {
        Map<V, E> r = this.get(key);
        if (r == null || r.isEmpty()) {
            return new HashSet<>();
        }
        return r.entrySet();
    }

    /**
     * 遍历元素
     *
     * @param key    key
     * @param action action
     */
    default void forEach(K key, BiConsumer<? super V, ? super E> action) {
        Map<V, E> r = this.get(key);
        if (r == null || r.isEmpty()) {
            return;
        }
        r.forEach(action);
    }

}

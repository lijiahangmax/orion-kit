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
package com.orion.lang.define.iterator;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * map迭代器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/18 20:39
 */
public class MapIterator<K, V> implements Iterator<Map.Entry<K, V>>, Iterable<Map.Entry<K, V>>, Serializable {

    private static final long serialVersionUID = -2395934120935349L;

    private Map<K, V> map;

    private Iterator<Map.Entry<K, V>> iterator;

    public MapIterator<K, V> setMap(Map<K, V> map) {
        this.map = map;
        this.iterator = this.map.entrySet().iterator();
        return this;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    public void forEach(Consumer<? super Map.Entry<K, V>> action) {
        map.entrySet().forEach(action);
    }

    @Override
    public Spliterator<Map.Entry<K, V>> spliterator() {
        return map.entrySet().spliterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Map.Entry<K, V> next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super Map.Entry<K, V>> action) {
        iterator.forEachRemaining(action);
    }

}

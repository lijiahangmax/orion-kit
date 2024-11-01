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
import cn.orionsec.kit.lang.utils.math.Numbers;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 分片 map
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/1/16 17:25
 */
public class PartitionMap<K, V> extends AbstractSet<Map<K, V>> implements Iterator<Map<K, V>> {

    private final Iterator<Map.Entry<K, V>> iterator;

    private final int size;

    private final int totalSize;

    private int current;

    public PartitionMap(Map<K, V> map, int size) {
        this.iterator = map.entrySet().iterator();
        this.size = size;
        this.totalSize = map.size();
    }

    public static <K, V> PartitionMap<K, V> create(Map<K, V> map, int size) {
        return new PartitionMap<>(map, size);
    }

    @Override
    public int size() {
        return (totalSize + size - 1) / size;
    }

    @Override
    public boolean isEmpty() {
        return totalSize == 0;
    }

    @Override
    public Iterator<Map<K, V>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return this.size() > current;
    }

    @Override
    public Map<K, V> next() {
        if (!this.hasNext()) {
            throw Exceptions.noSuchElement("there are no more elements");
        }
        current++;
        Map<K, V> map = new HashMap<>(Numbers.getMin2Power(size));
        for (int i = 0; i < size; i++) {
            if (iterator.hasNext()) {
                Map.Entry<K, V> next = iterator.next();
                map.put(next.getKey(), next.getValue());
            } else {
                break;
            }
        }
        return map;
    }

}
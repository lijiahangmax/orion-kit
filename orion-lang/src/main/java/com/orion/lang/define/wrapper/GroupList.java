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

import com.orion.lang.utils.Objects1;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 分组集合
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/1/3 15:59
 */
public class GroupList<E> {

    private final Collection<E> list;

    public GroupList(Collection<E> list) {
        this.list = list;
    }

    public static <E> GroupList<E> of(Collection<E> list) {
        return new GroupList<>(list);
    }

    /**
     * 通过字段和值提取分组
     *
     * @param mapping 字段
     * @param value   值
     * @param <V>     V
     * @return ignore
     */
    public <V> List<E> group(Function<E, V> mapping, V value) {
        List<E> groupList = new ArrayList<>();
        for (E v : list) {
            if (v == null) {
                continue;
            }
            if (Objects1.eq(value, mapping.apply(v))) {
                groupList.add(v);
            }
        }
        return groupList;
    }

    /**
     * 通过字段和值提取分组
     *
     * @param mapping 字段
     * @param f       Predicate
     * @param <V>     V
     * @return ignore
     */
    public <V> List<E> group(Function<E, V> mapping, Predicate<V> f) {
        List<E> groupList = new ArrayList<>();
        for (E v : list) {
            if (v == null) {
                continue;
            }
            V apply = mapping.apply(v);
            if (f.test(apply)) {
                groupList.add(v);
            }
        }
        return groupList;
    }

    /**
     * 通过字段提取分组
     *
     * @param mapping 字段
     * @param <V>     V
     * @return ignore
     */
    public <V> Map<V, List<E>> group(Function<E, V> mapping) {
        Map<V, List<E>> map = new LinkedHashMap<>();
        for (E v : list) {
            if (v == null) {
                continue;
            }
            V apply = mapping.apply(v);
            List<E> vs = map.computeIfAbsent(apply, k -> new ArrayList<>());
            vs.add(v);
        }
        return map;
    }

}

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

import cn.orionsec.kit.lang.define.Null;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * 基于 ConcurrentHashMap 实现的 ConcurrentHashSet
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/8/23 11:08
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Serializable {

    private static final long serialVersionUID = -6809192700238394L;

    private static final Null VALUE = Null.VALUE;

    private final ConcurrentMap<E, Null> store;

    public ConcurrentHashSet() {
        store = new ConcurrentHashMap<>();
    }

    public ConcurrentHashSet(int initialCapacity) {
        store = new ConcurrentHashMap<>(initialCapacity);
    }

    public ConcurrentHashSet(Map<? extends E, ?> m) {
        this.store = new ConcurrentHashMap<>(m.size());
        for (E e : m.keySet()) {
            store.put(e, VALUE);
        }
    }

    public ConcurrentHashSet(Collection<? extends E> s) {
        this.store = new ConcurrentHashMap<>(s.size());
        for (E e : s) {
            store.put(e, VALUE);
        }
    }

    public static <E> ConcurrentHashSet<E> create() {
        return new ConcurrentHashSet<>();
    }

    public static <E> ConcurrentHashSet<E> create(Map<? extends E, ?> m) {
        return new ConcurrentHashSet<>(m);
    }

    public static <E> ConcurrentHashSet<E> create(Collection<? extends E> s) {
        return new ConcurrentHashSet<>(s);
    }

    @Override
    public Iterator<E> iterator() {
        return store.keySet().iterator();
    }

    @Override
    public int size() {
        return store.size();
    }

    @Override
    public boolean isEmpty() {
        return store.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return store.containsKey(o);
    }

    @Override
    public boolean add(E e) {
        return store.put(e, VALUE) == null;
    }

    @Override
    public boolean remove(Object o) {
        return store.remove(o) == null;
    }

    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        for (Map.Entry<E, Null> e : store.entrySet()) {
            action.accept(e.getKey());
        }
    }

    public ConcurrentMap<E, Null> getStore() {
        return store;
    }

    @Override
    public String toString() {
        return store.keySet().toString();
    }

}
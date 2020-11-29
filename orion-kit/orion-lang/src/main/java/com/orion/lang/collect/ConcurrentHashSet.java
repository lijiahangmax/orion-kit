package com.orion.lang.collect;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * 基于ConcurrentHashMap实现的ConcurrentHashSet
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/8/23 11:08
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Serializable {

    private static final long serialVersionUID = -6809192700238394L;

    private Object o = new Object();

    private final ConcurrentMap<E, Object> map;

    public ConcurrentHashSet() {
        map = new ConcurrentHashMap<>();
    }

    public ConcurrentHashSet(int initialCapacity) {
        map = new ConcurrentHashMap<>(initialCapacity);
    }

    public ConcurrentHashSet(Map<? extends E, ?> m) {
        map = new ConcurrentHashMap<>(m);
    }

    public ConcurrentHashSet(Collection<? extends E> s) {
        map = new ConcurrentHashMap<>(s.size());
        for (E e : s) {
            map.put(e, o);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean add(E e) {
        return map.put(e, o) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) == null;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        for (Map.Entry<E, Object> e : map.entrySet()) {
            action.accept(e.getKey());
        }
    }
}
package com.orion.lang.collect;

import com.orion.lang.Null;

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

    private static final Null VALUE = Null.VALUE;

    private final ConcurrentMap<E, Null> store;

    public ConcurrentHashSet() {
        store = new ConcurrentHashMap<>();
    }

    public ConcurrentHashSet(int initialCapacity) {
        store = new ConcurrentHashMap<>(initialCapacity);
    }

    public ConcurrentHashSet(Map<? extends E, ?> m) {
        store = new ConcurrentHashMap<>(m.size());
        for (E e : m.keySet()) {
            store.put(e, VALUE);
        }
    }

    public ConcurrentHashSet(Collection<? extends E> s) {
        store = new ConcurrentHashMap<>(s.size());
        for (E e : s) {
            store.put(e, VALUE);
        }
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
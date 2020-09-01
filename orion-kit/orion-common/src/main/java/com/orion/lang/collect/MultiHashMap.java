package com.orion.lang.collect;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * MultiHashMap
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/8/21 20:46
 */
@SuppressWarnings("unchecked")
public class MultiHashMap<E, K, V> implements Serializable {

    private static final long serialVersionUID = 4834450551128977L;

    private final int DEFAULT_STORE_CAPACITY;

    private Map<E, Map<K, V>> store;

    private boolean concurrent;

    public MultiHashMap() {
        this(16, 16, false);
    }

    public MultiHashMap(int initialElementCapacity) {
        this(initialElementCapacity, 16, false);
    }

    public MultiHashMap(int initialElementCapacity, int initialKeyCapacity) {
        this(initialElementCapacity, initialKeyCapacity, false);
    }

    public MultiHashMap(boolean concurrent) {
        this(16, 16, concurrent);
        this.concurrent = concurrent;
    }

    public MultiHashMap(int initialElementCapacity, boolean concurrent) {
        this(initialElementCapacity, 16, concurrent);
    }

    public MultiHashMap(int initialElementCapacity, int initialKeyCapacity, boolean concurrent) {
        DEFAULT_STORE_CAPACITY = initialKeyCapacity;
        this.concurrent = concurrent;
        if (this.concurrent) {
            store = new ConcurrentHashMap<>(initialElementCapacity);
        } else {
            store = new HashMap<>(initialElementCapacity);
        }
    }

    public static <E, K, V> MultiHashMap<E, K, V> newMultiHashMap() {
        return new MultiHashMap<>(16, 16, false);
    }

    public static <E, K, V> MultiHashMap<E, K, V> newMultiHashMap(int initialElementCapacity) {
        return new MultiHashMap<>(initialElementCapacity, 16, false);
    }

    public static <E, K, V> MultiHashMap<E, K, V> newMultiHashMap(int initialElementCapacity, int initialKeyCapacity) {
        return new MultiHashMap<>(initialElementCapacity, initialKeyCapacity, false);
    }

    public static <E, K, V> MultiHashMap<E, K, V> newMultiHashMap(boolean concurrent) {
        return new MultiHashMap<>(16, 16, concurrent);
    }

    public static <E, K, V> MultiHashMap<E, K, V> newMultiHashMap(int initialElementCapacity, boolean concurrent) {
        return new MultiHashMap<>(initialElementCapacity, 16, concurrent);
    }

    public static <E, K, V> MultiHashMap<E, K, V> newMultiHashMap(int initialElementCapacity, int initialKeyCapacity, boolean concurrent) {
        return new MultiHashMap<>(initialElementCapacity, initialKeyCapacity, concurrent);
    }

    private void initSpace(E e) {
        if (!store.containsKey(e)) {
            if (this.concurrent) {
                store.put(e, new ConcurrentHashMap<>(DEFAULT_STORE_CAPACITY));
            } else {
                store.put(e, new HashMap<>(DEFAULT_STORE_CAPACITY));
            }
        }
    }

    public V put(E elem, K key, V value) {
        initSpace(elem);
        return store.get(elem).put(key, value);
    }

    public void put(E elem, Map.Entry<K, V>... entries) {
        initSpace(elem);
        Map<K, V> kvMap = store.get(elem);
        for (Map.Entry<K, V> entry : entries) {
            kvMap.put(entry.getKey(), entry.getValue());
        }
    }

    public void putAll(E elem, Map<K, V> map) {
        initSpace(elem);
        store.get(elem).putAll(map);
    }

    public Map<K, V> remove(E elem) {
        return store.remove(elem);
    }

    public boolean remove(E elem, Map<K, V> v) {
        return store.remove(elem, v);
    }

    public void remove(E... elems) {
        for (E elem : elems) {
            store.remove(elem);
        }
    }

    public V remove(E elem, K key) {
        Map<K, V> r = store.get(elem);
        if (r == null || r.isEmpty()) {
            return null;
        }
        return r.remove(key);
    }

    public boolean remove(E elem, K key, V value) {
        Map<K, V> r = store.get(elem);
        if (r == null || r.isEmpty()) {
            return false;
        }
        return r.remove(key, value);
    }

    public void remove(E elem, K... keys) {
        Map<K, V> r = store.get(elem);
        if (r == null || r.isEmpty()) {
            return;
        }
        for (K key : keys) {
            r.remove(key);
        }
    }

    public Map<K, V> get(E elem) {
        return store.get(elem);
    }

    public List<Map<K, V>> get(E... elems) {
        List<Map<K, V>> list = new ArrayList<>();
        for (E elem : elems) {
            list.add(store.get(elem));
        }
        return list;
    }

    public V get(E elem, K key) {
        Map<K, V> r = store.get(elem);
        if (r == null || r.isEmpty()) {
            return null;
        }
        return r.get(key);
    }

    public List<V> get(E elem, K... keys) {
        List<V> list = new ArrayList<>();
        Map<K, V> r = store.get(elem);
        if (r == null || r.isEmpty()) {
            return new ArrayList<>(1);
        }
        for (K key : keys) {
            list.add(r.get(key));
        }
        return list;
    }

    public int size() {
        return store.size();
    }

    public int size(E elem) {
        Map<K, V> r = store.get(elem);
        if (r == null || r.isEmpty()) {
            return 0;
        }
        return r.size();
    }

    public boolean isEmpty() {
        return store.isEmpty();
    }

    public boolean isEmpty(E elem) {
        Map<K, V> r = store.get(elem);
        if (r == null) {
            return true;
        }
        return r.isEmpty();
    }

    public void clear() {
        store.clear();
    }

    public void clear(E elem) {
        Map<K, V> r = store.get(elem);
        if (r != null) {
            r.clear();
        }
    }

    public boolean containsKey(E elem) {
        return store.containsKey(elem);
    }

    public boolean containsKey(E elem, K key) {
        Map<K, V> r = store.get(elem);
        if (r == null || r.isEmpty()) {
            return false;
        }
        return r.containsKey(key);
    }

    public Set<E> keySet() {
        return store.keySet();
    }

    public Set<K> keySet(E elem) {
        Map<K, V> r = store.get(elem);
        if (r == null || r.isEmpty()) {
            return new HashSet<>(1);
        }
        return r.keySet();
    }

    public Collection<Map<K, V>> values() {
        return store.values();
    }

    public Collection<V> values(E elem) {
        Map<K, V> r = store.get(elem);
        if (r == null || r.isEmpty()) {
            return new ArrayList<>(1);
        }
        return r.values();
    }

    public Set<Map.Entry<E, Map<K, V>>> entrySet() {
        return store.entrySet();
    }

    public Set<Map.Entry<K, V>> entrySet(E elem) {
        Map<K, V> r = store.get(elem);
        if (r == null || r.isEmpty()) {
            return new HashSet<>(1);
        }
        return r.entrySet();
    }

    public void replace(E elem, Map<K, V> newV) {
        store.replace(elem, newV);
    }

    public void replace(E elem, Map<K, V> oldV, Map<K, V> newV) {
        store.replace(elem, oldV, newV);
    }

    public void replace(E elem, K key, V newV) {
        Map<K, V> r = store.get(elem);
        if (r == null) {
            return;
        }
        r.replace(key, newV);
    }

    public void replace(E elem, K key, V oldV, V newV) {
        Map<K, V> r = store.get(elem);
        if (r == null) {
            return;
        }
        r.replace(key, oldV, newV);
    }

    public void forEach(BiConsumer<? super E, ? super Map<K, V>> action) {
        store.forEach(action);
    }

    public void forEach(E elem, BiConsumer<? super K, ? super V> action) {
        Map<K, V> r = store.get(elem);
        if (r == null || r.isEmpty()) {
            return;
        }
        r.forEach(action);
    }

    @Override
    public String toString() {
        return store.toString();
    }

}

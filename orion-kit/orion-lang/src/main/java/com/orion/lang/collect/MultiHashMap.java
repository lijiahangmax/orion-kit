package com.orion.lang.collect;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * MultiHashMap
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/19 16:56
 */
public class MultiHashMap<E, K, V> extends HashMap<E, HashMap<K, V>> implements Serializable {

    private static final long serialVersionUID = 4834450551128977L;

    private int storeCapacity;

    public MultiHashMap() {
        this(16, 16);
    }

    public MultiHashMap(int initialElementCapacity) {
        this(initialElementCapacity, 16);
    }

    public MultiHashMap(int initialElementCapacity, int initialKeyCapacity) {
        super(initialElementCapacity);
        this.storeCapacity = initialKeyCapacity;
    }

    private Map<K, V> getSpace(E e) {
        return super.computeIfAbsent(e, k -> new HashMap<K, V>(storeCapacity));
    }

    public V put(E elem, K key, V value) {
        return getSpace(elem).put(key, value);
    }

    public void putAll(E elem, Map<K, V> map) {
        getSpace(elem).putAll(map);
    }

    public V removeKey(E elem, K key) {
        Map<K, V> r = super.get(elem);
        if (r == null || r.isEmpty()) {
            return null;
        }
        return r.remove(key);
    }

    public boolean removeKey(E elem, K key, V value) {
        Map<K, V> r = super.get(elem);
        if (r == null || r.isEmpty()) {
            return false;
        }
        return r.remove(key, value);
    }

    public V get(E elem, K key) {
        Map<K, V> r = super.get(elem);
        if (r == null || r.isEmpty()) {
            return null;
        }
        return r.get(key);
    }

    public int size(E elem) {
        Map<K, V> r = super.get(elem);
        if (r == null || r.isEmpty()) {
            return 0;
        }
        return r.size();
    }

    public boolean isEmpty(E elem) {
        Map<K, V> r = super.get(elem);
        if (r == null) {
            return true;
        }
        return r.isEmpty();
    }

    public void clear(E elem) {
        Map<K, V> r = super.get(elem);
        if (r != null) {
            r.clear();
        }
    }

    public boolean containsKey(E elem, K key) {
        Map<K, V> r = super.get(elem);
        if (r == null || r.isEmpty()) {
            return false;
        }
        return r.containsKey(key);
    }

    public Collection<V> values(E elem) {
        Map<K, V> r = super.get(elem);
        if (r == null || r.isEmpty()) {
            return new ArrayList<>(1);
        }
        return r.values();
    }

    public Set<Entry<K, V>> entrySet(E elem) {
        Map<K, V> r = super.get(elem);
        if (r == null || r.isEmpty()) {
            return new HashSet<>(1);
        }
        return r.entrySet();
    }

    public void forEach(E elem, BiConsumer<? super K, ? super V> action) {
        Map<K, V> r = super.get(elem);
        if (r == null || r.isEmpty()) {
            return;
        }
        r.forEach(action);
    }

}

package com.orion.lang.collect;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * MultiMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/2 13:52
 */
public interface MultiMap<E, K, V, M extends Map<K, V>> extends Map<E, M> {

    /**
     * 开辟空间
     *
     * @param e e
     * @return map
     */
    M computeSpace(E e);

    default V put(E elem, K key, V value) {
        return this.computeSpace(elem).put(key, value);
    }

    default void putAll(E elem, Map<K, V> map) {
        this.computeSpace(elem).putAll(map);
    }

    default V removeKey(E elem, K key) {
        Map<K, V> r = this.get(elem);
        if (r == null || r.isEmpty()) {
            return null;
        }
        return r.remove(key);
    }

    default boolean removeKey(E elem, K key, V value) {
        Map<K, V> r = this.get(elem);
        if (r == null || r.isEmpty()) {
            return false;
        }
        return r.remove(key, value);
    }

    default V get(E elem, K key) {
        Map<K, V> r = this.get(elem);
        if (r == null || r.isEmpty()) {
            return null;
        }
        return r.get(key);
    }

    default int size(E elem) {
        Map<K, V> r = this.get(elem);
        if (r == null || r.isEmpty()) {
            return 0;
        }
        return r.size();
    }

    default boolean isEmpty(E elem) {
        Map<K, V> r = this.get(elem);
        if (r == null) {
            return true;
        }
        return r.isEmpty();
    }

    default void clear(E elem) {
        Map<K, V> r = this.get(elem);
        if (r != null) {
            r.clear();
        }
    }

    default boolean containsKey(E elem, K key) {
        Map<K, V> r = this.get(elem);
        if (r == null || r.isEmpty()) {
            return false;
        }
        return r.containsKey(key);
    }

    default Collection<V> values(E elem) {
        Map<K, V> r = this.get(elem);
        if (r == null || r.isEmpty()) {
            return new ArrayList<>(1);
        }
        return r.values();
    }

    default Set<Map.Entry<K, V>> entrySet(E elem) {
        Map<K, V> r = this.get(elem);
        if (r == null || r.isEmpty()) {
            return new HashSet<>(1);
        }
        return r.entrySet();
    }

    default void forEach(E elem, BiConsumer<? super K, ? super V> action) {
        Map<K, V> r = this.get(elem);
        if (r == null || r.isEmpty()) {
            return;
        }
        r.forEach(action);
    }

}

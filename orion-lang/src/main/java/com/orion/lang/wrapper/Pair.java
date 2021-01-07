package com.orion.lang.wrapper;

import com.orion.lang.support.CloneSupport;

import java.io.Serializable;
import java.util.Map;

/**
 * Map的Entry实现
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/8/23 10:40
 */
public class Pair<K, V> extends CloneSupport<Pair<K, V>> implements Map.Entry<K, V>, Serializable {

    private static final long serialVersionUID = 3797556461612462L;

    private K key;
    private V value;

    public Pair() {
    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 将map的实体转化为MapEntry
     *
     * @param entry Map.Entry
     * @param <K>   key
     * @param <V>   value
     * @return MapEntry
     */
    public static <K, V> Pair<K, V> toMapEntry(Map.Entry<K, V> entry) {
        return new Pair<>(entry.getKey(), entry.getValue());
    }

    @Override
    public K getKey() {
        return key;
    }

    public Pair<K, V> setKey(K key) {
        this.key = key;
        return this;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        this.value = value;
        return value;
    }

    @Override
    public int hashCode() {
        return key.hashCode() ^ value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        Object k, v;
        Map.Entry<?, ?> e;
        return ((o instanceof Map.Entry) &&
                (k = (e = (Map.Entry<?, ?>) o).getKey()) != null &&
                (v = e.getValue()) != null &&
                (k == key || k.equals(key)) &&
                (v == value || v.equals(value)));
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

}

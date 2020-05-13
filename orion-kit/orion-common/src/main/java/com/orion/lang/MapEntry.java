package com.orion.lang;

import java.io.Serializable;
import java.util.Map;

/**
 * Map的Entry实现
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/8/23 10:40
 */
public class MapEntry<K, V> implements Map.Entry<K, V>, Serializable {

    private static final long serialVersionUID = 3797556461612462L;

    private K key;
    private V value;

    public MapEntry() {
    }

    public MapEntry(K key, V value) {
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
    public static <K, V> MapEntry<K, V> toMapEntry(Map.Entry<K, V> entry) {
        return new MapEntry<>(entry.getKey(), entry.getValue());
    }

    @Override
    public K getKey() {
        return key;
    }

    public MapEntry<K, V> setKey(K key) {
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
    public String toString() {
        return "MapEntry{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

}

package com.orion.lang.define.collect;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 可转换的 LinkedHashMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/15 10:34
 */
public class MutableLinkedHashMap<K, V> extends LinkedHashMap<K, V> implements MutableMap<K, V>, Serializable {

    private static final long serialVersionUID = 876812409459012839L;

    public MutableLinkedHashMap() {
        super();
    }

    public MutableLinkedHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public MutableLinkedHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MutableLinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public MutableLinkedHashMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public static <K, V> MutableLinkedHashMap<K, V> create() {
        return new MutableLinkedHashMap<>();
    }

    public static <K, V> MutableLinkedHashMap<K, V> create(Map<? extends K, ? extends V> m) {
        return new MutableLinkedHashMap<>(m);
    }

}

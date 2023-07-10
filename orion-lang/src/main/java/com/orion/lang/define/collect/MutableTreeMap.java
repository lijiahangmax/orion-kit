package com.orion.lang.define.collect;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 可转换的 TreeMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/5 17:37
 */
public class MutableTreeMap<K, V> extends TreeMap<K, V> implements MutableMap<K, V>, Serializable {

    private static final long serialVersionUID = 89347891239048579L;

    public MutableTreeMap() {
        super();
    }

    public MutableTreeMap(Comparator<? super K> comparator) {
        super(comparator);
    }

    public MutableTreeMap(SortedMap<K, ? extends V> m) {
        super(m);
    }

    public MutableTreeMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public static <K, V> MutableTreeMap<K, V> create() {
        return new MutableTreeMap<>();
    }

    public static <K, V> MutableTreeMap<K, V> create(Map<? extends K, ? extends V> m) {
        return new MutableTreeMap<>(m);
    }

}

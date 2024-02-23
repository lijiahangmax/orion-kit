package com.orion.lang.define.collect;

import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * MultiTreeMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/19 16:56
 */
public class MultiTreeMap<K, V, E> extends TreeMap<K, TreeMap<V, E>>
        implements MultiMap<K, V, E, TreeMap<V, E>>, Serializable {

    private static final long serialVersionUID = -786438489922021L;

    /**
     * value 比较器
     */
    private Comparator<? super V> valueComparator;

    public MultiTreeMap() {
    }

    public MultiTreeMap(Comparator<? super K> comparator) {
        super(comparator);
    }

    public MultiTreeMap(Comparator<? super K> comparator, Comparator<? super V> valueComparator) {
        super(comparator);
        this.valueComparator = valueComparator;
    }

    public static <K, V, E> MultiTreeMap<K, V, E> create() {
        return new MultiTreeMap<>();
    }

    public static <K, V, E> MultiTreeMap<K, V, E> create(Comparator<? super K> comparator) {
        return new MultiTreeMap<>(comparator);
    }

    public static <K, V, E> MultiTreeMap<K, V, E> create(Comparator<? super K> comparator, Comparator<? super V> keyComparator) {
        return new MultiTreeMap<>(comparator, keyComparator);
    }

    /**
     * 设置 value 比较器
     *
     * @param valueComparator valueComparator
     */
    public void valueComparator(Comparator<? super V> valueComparator) {
        this.valueComparator = valueComparator;
    }

    @Override
    public TreeMap<V, E> computeSpace(K e) {
        return super.computeIfAbsent(e, k -> new TreeMap<V, E>(valueComparator));
    }

}

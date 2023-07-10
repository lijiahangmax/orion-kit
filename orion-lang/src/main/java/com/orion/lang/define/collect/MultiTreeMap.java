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
public class MultiTreeMap<E, K, V> extends TreeMap<E, TreeMap<K, V>>
        implements MultiMap<E, K, V, TreeMap<K, V>>, Serializable {

    private static final long serialVersionUID = -786438489922021L;

    /**
     * key 比较器
     */
    private Comparator<? super K> keyComparator;

    public MultiTreeMap() {
    }

    public MultiTreeMap(Comparator<? super E> comparator) {
        super(comparator);
    }

    public MultiTreeMap(Comparator<? super E> comparator, Comparator<? super K> keyComparator) {
        super(comparator);
        this.keyComparator = keyComparator;
    }

    public static <E, K, V> MultiTreeMap<E, K, V> create() {
        return new MultiTreeMap<>();
    }

    public static <E, K, V> MultiTreeMap<E, K, V> create(Comparator<? super E> comparator) {
        return new MultiTreeMap<>(comparator);
    }

    public static <E, K, V> MultiTreeMap<E, K, V> create(Comparator<? super E> comparator, Comparator<? super K> keyComparator) {
        return new MultiTreeMap<>(comparator, keyComparator);
    }

    /**
     * 设置 key 比较器
     *
     * @param keyComparator keyComparator
     */
    public void keyComparator(Comparator<? super K> keyComparator) {
        this.keyComparator = keyComparator;
    }

    @Override
    public TreeMap<K, V> computeSpace(E e) {
        return super.computeIfAbsent(e, k -> new TreeMap<K, V>(keyComparator));
    }

}

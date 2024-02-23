package com.orion.lang.define.collect;

import com.orion.lang.constant.Const;

import java.io.Serializable;
import java.util.HashMap;

/**
 * MultiHashMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/19 16:56
 */
public class MultiHashMap<K, V, E> extends HashMap<K, HashMap<V, E>>
        implements MultiMap<K, V, E, HashMap<V, E>>, Serializable {

    private static final long serialVersionUID = 4834450551128977L;

    /**
     * value 初始化空间
     */
    private int valueInitialCapacity;

    public MultiHashMap() {
        this(Const.CAPACITY_16, Const.CAPACITY_16);
    }

    public MultiHashMap(int initialCapacity) {
        this(initialCapacity, initialCapacity);
    }

    public MultiHashMap(int keyInitialCapacity, int valueInitialCapacity) {
        super(keyInitialCapacity);
        this.valueInitialCapacity = valueInitialCapacity;
    }

    public static <E, K, V> MultiHashMap<E, K, V> create() {
        return new MultiHashMap<>();
    }

    /**
     * 设置 value 初始化空间
     *
     * @param valueInitialCapacity value 初始化空间
     */
    public void valueInitialCapacity(int valueInitialCapacity) {
        this.valueInitialCapacity = valueInitialCapacity;
    }

    @Override
    public HashMap<V, E> computeSpace(K e) {
        return super.computeIfAbsent(e, k -> new HashMap<V, E>(valueInitialCapacity));
    }

}

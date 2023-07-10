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
public class MultiHashMap<E, K, V> extends HashMap<E, HashMap<K, V>>
        implements MultiMap<E, K, V, HashMap<K, V>>, Serializable {

    private static final long serialVersionUID = 4834450551128977L;

    /**
     * key 初始化空间
     */
    private int keyInitialCapacity;

    public MultiHashMap() {
        this(Const.CAPACITY_16, Const.CAPACITY_16);
    }

    public MultiHashMap(int elementInitialCapacity) {
        this(elementInitialCapacity, Const.CAPACITY_16);
    }

    public MultiHashMap(int elementInitialCapacity, int keyInitialCapacity) {
        super(elementInitialCapacity);
        this.keyInitialCapacity = keyInitialCapacity;
    }

    public static <E, K, V> MultiHashMap<E, K, V> create() {
        return new MultiHashMap<>();
    }

    /**
     * 设置 key 初始化空间
     *
     * @param keyInitialCapacity key 初始化空间
     */
    public void keyInitialCapacity(int keyInitialCapacity) {
        this.keyInitialCapacity = keyInitialCapacity;
    }

    @Override
    public HashMap<K, V> computeSpace(E e) {
        return super.computeIfAbsent(e, k -> new HashMap<K, V>(keyInitialCapacity));
    }

}

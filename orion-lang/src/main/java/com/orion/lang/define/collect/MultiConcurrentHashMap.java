package com.orion.lang.define.collect;

import com.orion.lang.constant.Const;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MultiConcurrentHashMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/19 16:39
 */
public class MultiConcurrentHashMap<E, K, V> extends ConcurrentHashMap<E, ConcurrentHashMap<K, V>>
        implements MultiMap<E, K, V, ConcurrentHashMap<K, V>>, Serializable {

    private static final long serialVersionUID = 8455892712354974891L;

    /**
     * key 初始化空间
     */
    private int keyInitialCapacity;

    public MultiConcurrentHashMap() {
        this(Const.CAPACITY_16, Const.CAPACITY_16);
    }

    public MultiConcurrentHashMap(int elementInitialCapacity) {
        this(elementInitialCapacity, Const.CAPACITY_16);
    }

    public MultiConcurrentHashMap(int elementInitialCapacity, int keyInitialCapacity) {
        super(elementInitialCapacity);
        this.keyInitialCapacity = keyInitialCapacity;
    }

    public static <E, K, V> MultiConcurrentHashMap<E, K, V> create() {
        return new MultiConcurrentHashMap<>();
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
    public ConcurrentHashMap<K, V> computeSpace(E e) {
        return super.computeIfAbsent(e, k -> new ConcurrentHashMap<>(keyInitialCapacity));
    }

}


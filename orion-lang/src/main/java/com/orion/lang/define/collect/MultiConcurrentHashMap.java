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
public class MultiConcurrentHashMap<K, V, E> extends ConcurrentHashMap<K, ConcurrentHashMap<V, E>>
        implements MultiMap<K, V, E, ConcurrentHashMap<V, E>>, Serializable {

    private static final long serialVersionUID = 8455892712354974891L;

    /**
     * value 初始化空间
     */
    private int valueInitialCapacity;

    public MultiConcurrentHashMap() {
        this(Const.CAPACITY_16, Const.CAPACITY_16);
    }

    public MultiConcurrentHashMap(int initialCapacity) {
        this(initialCapacity, initialCapacity);
    }

    public MultiConcurrentHashMap(int keyInitialCapacity, int valueInitialCapacity) {
        super(keyInitialCapacity);
        this.valueInitialCapacity = valueInitialCapacity;
    }

    public static <K, V, E> MultiConcurrentHashMap<K, V, E> create() {
        return new MultiConcurrentHashMap<>();
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
    public ConcurrentHashMap<V, E> computeSpace(K e) {
        return super.computeIfAbsent(e, k -> new ConcurrentHashMap<>(valueInitialCapacity));
    }

}


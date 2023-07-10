package com.orion.lang.define.collect;

import com.orion.lang.constant.Const;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * MultiLinkedHashMap
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/19 16:56
 */
public class MultiLinkedHashMap<E, K, V> extends LinkedHashMap<E, LinkedHashMap<K, V>>
        implements MultiMap<E, K, V, LinkedHashMap<K, V>>, Serializable {

    private static final long serialVersionUID = 1237895897912782748L;

    private static final float DEFAULT_LOAD_FACTOR = 0.75F;

    /**
     * key 初始化空间
     */
    private int keyInitialCapacity;

    /**
     * key 负载因子
     */
    private float keyLoadFactor;

    /**
     * key 排序模式
     */
    private boolean keyAccessOrder;

    public MultiLinkedHashMap() {
        this(Const.CAPACITY_16, DEFAULT_LOAD_FACTOR, false);
    }

    public MultiLinkedHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, false);
    }

    public MultiLinkedHashMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, false);
    }

    public MultiLinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
        this.keyInitialCapacity = initialCapacity;
        this.keyLoadFactor = loadFactor;
        this.keyAccessOrder = accessOrder;
    }

    public static <E, K, V> MultiLinkedHashMap<E, K, V> create() {
        return new MultiLinkedHashMap<>();
    }

    public static <E, K, V> MultiLinkedHashMap<E, K, V> create(int initialCapacity, float loadFactor, boolean accessOrder) {
        return new MultiLinkedHashMap<>(initialCapacity, loadFactor, accessOrder);
    }

    public void keyCapacity(int keyInitialCapacity) {
        this.keyCapacity(keyInitialCapacity, DEFAULT_LOAD_FACTOR, false);
    }

    public void keyCapacity(int keyInitialCapacity, float keyLoadFactor) {
        this.keyCapacity(keyInitialCapacity, keyLoadFactor, false);
    }

    /**
     * 设置 key 初始化参数
     *
     * @param keyInitialCapacity 初始化空间
     * @param keyLoadFactor      负载因子
     * @param keyAccessOrder     排序模式
     */
    public void keyCapacity(int keyInitialCapacity, float keyLoadFactor, boolean keyAccessOrder) {
        this.keyInitialCapacity = keyInitialCapacity;
        this.keyLoadFactor = keyLoadFactor;
        this.keyAccessOrder = keyAccessOrder;
    }

    @Override
    public LinkedHashMap<K, V> computeSpace(E e) {
        return super.computeIfAbsent(e, k -> new LinkedHashMap<K, V>(keyInitialCapacity, keyLoadFactor, keyAccessOrder));
    }

}

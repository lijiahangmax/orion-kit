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
        super(Const.CAPACITY_16);
    }

    public MultiLinkedHashMap(int elementInitialCapacity) {
        this(elementInitialCapacity, DEFAULT_LOAD_FACTOR, false);
    }

    public MultiLinkedHashMap(int elementInitialCapacity, float loadFactor) {
        this(elementInitialCapacity, loadFactor, false);
    }

    public MultiLinkedHashMap(int elementInitialCapacity, float loadFactor, boolean accessOrder) {
        super(elementInitialCapacity, loadFactor, accessOrder);
        this.keyInitialCapacity = Const.CAPACITY_16;
        this.keyLoadFactor = DEFAULT_LOAD_FACTOR;
        this.keyAccessOrder = false;
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

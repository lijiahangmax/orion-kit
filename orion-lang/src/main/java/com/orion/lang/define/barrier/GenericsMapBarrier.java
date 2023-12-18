package com.orion.lang.define.barrier;

import com.orion.lang.utils.collect.Maps;

import java.util.Map;

/**
 * 标准 map 屏障
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/21 11:46
 */
public class GenericsMapBarrier<K, V> implements GenericsBarrier<Map<K, V>> {

    private final K barrierKey;

    private final V barrierValue;

    public GenericsMapBarrier(K barrierKey) {
        this(barrierKey, null);
    }

    public GenericsMapBarrier(K barrierKey, V barrierValue) {
        this.barrierKey = barrierKey;
        this.barrierValue = barrierValue;
    }

    /**
     * 创建屏障
     *
     * @param barrierKey barrierKey
     * @param <K>        K
     * @param <V>        V
     * @return barrier
     */
    public static <K, V> GenericsMapBarrier<K, V> create(K barrierKey) {
        return new GenericsMapBarrier<>(barrierKey);
    }

    /**
     * 创建屏障
     *
     * @param barrierKey   barrierKey
     * @param barrierValue barrierValue
     * @param <K>          K
     * @param <V>          V
     * @return barrier
     */
    public static <K, V> GenericsMapBarrier<K, V> create(K barrierKey, V barrierValue) {
        return new GenericsMapBarrier<>(barrierKey, barrierValue);
    }

    @Override
    public void check(Map<K, V> map) {
        if (map != null && map.isEmpty()) {
            // 添加屏障对象
            map.put(barrierKey, barrierValue);
        }
    }

    @Override
    public void remove(Map<K, V> map) {
        if (!Maps.isEmpty(map)) {
            map.remove(barrierKey);
        }
    }

}

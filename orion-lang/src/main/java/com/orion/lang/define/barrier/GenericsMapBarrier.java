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
public class GenericsMapBarrier<K> implements GenericsBarrier<Map<K, ?>> {

    private final K barrierKey;

    public GenericsMapBarrier(K barrierKey) {
        this.barrierKey = barrierKey;
    }

    @Override
    public void check(Map<K, ?> map) {
        if (map != null && map.isEmpty()) {
            // 添加屏障对象
            map.put(barrierKey, null);
        }
    }

    @Override
    public void remove(Map<K, ?> map) {
        if (!Maps.isEmpty(map)) {
            map.remove(barrierKey);
        }
    }

}

package com.orion.lang.define.barrier;

import com.orion.lang.utils.collect.Maps;

import java.util.Map;

/**
 * 匿名 map 屏障
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/21 11:46
 */
public class GenericsAnonymousMapBarrier implements GenericsBarrier<Map<?, ?>> {

    private final Object barrierKey;

    private final Object barrierValue;

    public GenericsAnonymousMapBarrier(Object barrierKey) {
        this(barrierKey, null);
    }

    public GenericsAnonymousMapBarrier(Object barrierKey, Object barrierValue) {
        this.barrierKey = barrierKey;
        this.barrierValue = barrierValue;
    }

    /**
     * 创建屏障
     *
     * @param barrierKey barrierKey
     * @return barrier
     */
    public static GenericsAnonymousMapBarrier create(Object barrierKey) {
        return new GenericsAnonymousMapBarrier(barrierKey);
    }

    /**
     * 创建屏障
     *
     * @param barrierKey   barrierKey
     * @param barrierValue barrierValue
     * @return barrier
     */
    public static GenericsAnonymousMapBarrier create(Object barrierKey, Object barrierValue) {
        return new GenericsAnonymousMapBarrier(barrierKey, barrierValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void check(Map<?, ?> map) {
        if (map != null && map.isEmpty()) {
            // 添加屏障对象
            ((Map<Object, Object>) map).put(barrierKey, barrierValue);
        }
    }

    @Override
    public void remove(Map<?, ?> map) {
        if (!Maps.isEmpty(map)) {
            map.remove(barrierKey);
        }
    }

}

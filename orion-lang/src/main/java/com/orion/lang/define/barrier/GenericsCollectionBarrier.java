package com.orion.lang.define.barrier;

import com.orion.lang.utils.collect.Lists;

import java.util.Collection;

/**
 * 标准 collection 屏障
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/21 11:46
 */
public class GenericsCollectionBarrier<T> implements GenericsBarrier<Collection<T>> {

    private final T barrierValue;

    public GenericsCollectionBarrier(T barrierValue) {
        this.barrierValue = barrierValue;
    }

    /**
     * 创建屏障
     *
     * @param barrierValue barrierValue
     * @param <T>          T
     * @return barrier
     */
    public static <T> GenericsCollectionBarrier<T> create(T barrierValue) {
        return new GenericsCollectionBarrier<>(barrierValue);
    }

    @Override
    public void check(Collection<T> list) {
        if (list != null && list.isEmpty()) {
            // 添加屏障对象
            list.add(barrierValue);
        }
    }

    @Override
    public void remove(Collection<T> list) {
        if (!Lists.isEmpty(list)) {
            list.removeIf(barrierValue::equals);
        }
    }

}

package com.orion.lang.define.barrier;

import com.orion.lang.utils.collect.Lists;

import java.util.Collection;

/**
 * 匿名 collection 屏障
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/21 11:46
 */
public class GenericsAnonymousCollectionBarrier implements GenericsBarrier<Collection<?>> {

    private final Object barrierValue;

    public GenericsAnonymousCollectionBarrier(Object barrierValue) {
        this.barrierValue = barrierValue;
    }

    /**
     * 创建屏障
     *
     * @param barrierValue barrierValue
     * @return barrier
     */
    public static GenericsAnonymousCollectionBarrier create(Object barrierValue) {
        return new GenericsAnonymousCollectionBarrier(barrierValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void check(Collection<?> list) {
        if (list != null && list.isEmpty()) {
            // 添加屏障对象
            ((Collection<Object>) list).add(barrierValue);
        }
    }

    @Override
    public void remove(Collection<?> list) {
        if (!Lists.isEmpty(list)) {
            list.removeIf(barrierValue::equals);
        }
    }

}

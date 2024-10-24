/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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

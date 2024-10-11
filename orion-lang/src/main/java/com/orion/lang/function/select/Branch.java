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
package com.orion.lang.function.select;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 分支条件
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/5/11 12:47
 */
public interface Branch<P, R> {

    /**
     * @return tester
     */
    Predicate<P> tester();

    /**
     * @return factory
     */
    Function<P, R> factory();

    /**
     * 分支方法
     *
     * @param tester  测试器
     * @param factory 工厂
     * @param <P>     P
     * @param <R>     R
     * @return {@link Branch}
     */
    static <P, R> Branch<P, R> of(Predicate<P> tester, Function<P, R> factory) {
        return new Branch<P, R>() {
            @Override
            public Predicate<P> tester() {
                return tester;
            }

            @Override
            public Function<P, R> factory() {
                return factory;
            }
        };
    }

}

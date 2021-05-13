package com.orion.function.select;

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

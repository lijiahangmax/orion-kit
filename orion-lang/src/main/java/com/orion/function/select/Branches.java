package com.orion.function.select;

import com.orion.utils.Arrays1;
import com.orion.utils.Objects1;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 分支构建器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/5/11 13:43
 */
@FunctionalInterface
public interface Branches<P, R> {

    /**
     * 构建分支
     *
     * @param factory factory
     * @return {@link Branch}
     */
    Branch<P, R> then(Function<P, R> factory);

    /**
     * 构建分支
     *
     * @param value 值
     * @return {@link Branch}
     */
    default Branch<P, R> then(R value) {
        return this.then(p -> value);
    }

    /**
     * 工厂函数
     *
     * @param tester 测试器
     * @param <P>    P
     * @param <R>    R
     * @return {@link Branches}
     */
    static <P, R> Branches<P, R> when(Predicate<P> tester) {
        return factory -> Branch.of(tester, factory);
    }

    /**
     * 工厂函数 相等
     *
     * @param v   value
     * @param <P> P
     * @param <R> R
     * @return {@link Branches}
     */
    static <P, R> Branches<P, R> eq(P v) {
        return factory -> Branch.of(p -> Objects1.eq(p, v), factory);
    }

    /**
     * 工厂函数 比较相等
     *
     * @param v   value
     * @param <P> P
     * @param <R> R
     * @return {@link Branches}
     */
    static <P extends Comparable<P>, R> Branches<P, R> compared(P v) {
        return factory -> Branch.of(p -> Objects1.compared(p, v), factory);
    }

    /**
     * 工厂函数 包含
     *
     * @param arr in
     * @param <P> P
     * @param <R> R
     * @return {@link Branches}
     */
    @SafeVarargs
    static <P, R> Branches<P, R> in(P... arr) {
        return factory -> Branch.of(p -> Arrays1.some(p, arr), factory);
    }

}

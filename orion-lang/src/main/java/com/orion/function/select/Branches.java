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
public class Branches<P> {

    private Predicate<P> tester;

    public Branches(Predicate<P> tester) {
        this.tester = tester;
    }

    /**
     * 构建分支
     *
     * @param factory factory
     * @return {@link Branch}
     */
    public <R> Branch<P, R> then(Function<P, R> factory) {
        return Branch.of(tester, factory);
    }

    /**
     * 构建分支
     *
     * @param r result
     * @return {@link Branch}
     */
    public <R> Branch<P, R> then(R r) {
        return Branch.of(tester, (p) -> r);
    }

    /**
     * 工厂函数
     *
     * @param tester 测试器
     * @param <P>    P
     * @return {@link Branches}
     */
    public static <P> Branches<P> when(Predicate<P> tester) {
        return new Branches<>(tester);
    }

    /**
     * 工厂函数 相等
     *
     * @param v   value
     * @param <P> P
     * @return {@link Branches}
     */
    public static <P> Branches<P> eq(P v) {
        return new Branches<>(p -> Objects1.eq(p, v));
    }

    /**
     * 工厂函数 比较相等
     *
     * @param v   value
     * @param <P> P
     * @return {@link Branches}
     */
    public static <P extends Comparable<P>> Branches<P> compared(P v) {
        return new Branches<>(p -> Objects1.compared(p, v));
    }

    /**
     * 工厂函数 包含
     *
     * @param arr in
     * @param <P> P
     * @return {@link Branches}
     */
    @SafeVarargs
    public static <P> Branches<P> in(P... arr) {
        return new Branches<>(p -> Arrays1.some(p, arr));
    }

}

package com.orion.function;

import java.util.function.Function;

/**
 * 转化接口
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/23 10:35
 */
@FunctionalInterface
public interface Conversion<T, R> {

    /**
     * 转化
     *
     * @param t t
     * @return R
     */
    R apply(T t);

    /**
     * Function -> Conversion
     *
     * @param f   Function
     * @param <T> T
     * @param <R> R
     * @return Conversion
     */
    static <T, R> Conversion<T, R> with(Function<T, R> f) {
        return f::apply;
    }

}

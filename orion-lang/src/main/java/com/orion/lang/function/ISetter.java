package com.orion.lang.function;

import java.io.Serializable;
import java.util.function.BiConsumer;

/**
 * setter 方法接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/10 10:58
 */
@FunctionalInterface
public interface ISetter<T, U> extends BiConsumer<T, U>, Serializable {

    /**
     * setter
     *
     * @param t t
     * @param u u
     */
    @Override
    void accept(T t, U u);

}
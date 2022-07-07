package com.orion.lang.function;

import java.util.Objects;

/**
 * boolean consumer
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/1/22 21:57
 */
@FunctionalInterface
public interface BooleanConsumer {

    /**
     * 执行
     *
     * @param b boolean
     */
    void accept(boolean b);

    /**
     * 链式执行
     *
     * @param after after
     * @return then
     */
    default BooleanConsumer andThen(BooleanConsumer after) {
        Objects.requireNonNull(after);
        return (boolean t) -> {
            accept(t);
            after.accept(t);
        };
    }

}

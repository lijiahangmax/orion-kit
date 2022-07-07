package com.orion.lang.function;

import java.util.Objects;

/**
 * float consumer
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/1/22 21:56
 */
@FunctionalInterface
public interface FloatConsumer {

    /**
     * 执行
     *
     * @param f float
     */
    void accept(float f);

    /**
     * 链式执行
     *
     * @param after after
     * @return then
     */
    default FloatConsumer andThen(FloatConsumer after) {
        Objects.requireNonNull(after);
        return (float t) -> {
            accept(t);
            after.accept(t);
        };
    }

}

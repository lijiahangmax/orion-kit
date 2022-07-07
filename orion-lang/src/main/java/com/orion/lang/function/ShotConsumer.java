package com.orion.lang.function;

import java.util.Objects;

/**
 * short consumer
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/1/22 21:54
 */
@FunctionalInterface
public interface ShotConsumer {

    /**
     * 执行
     *
     * @param s short
     */
    void accept(short s);

    /**
     * 链式执行
     *
     * @param after after
     * @return then
     */
    default ShotConsumer andThen(ShotConsumer after) {
        Objects.requireNonNull(after);
        return (short t) -> {
            accept(t);
            after.accept(t);
        };
    }

}

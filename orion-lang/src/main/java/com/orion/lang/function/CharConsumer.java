package com.orion.lang.function;

import java.util.Objects;

/**
 * char consumer
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/1/22 21:58
 */
@FunctionalInterface
public interface CharConsumer {

    /**
     * 执行
     *
     * @param c char
     */
    void accept(char c);

    /**
     * 链式执行
     *
     * @param after after
     * @return then
     */
    default CharConsumer andThen(CharConsumer after) {
        Objects.requireNonNull(after);
        return (char t) -> {
            accept(t);
            after.accept(t);
        };
    }

}

package com.orion.function;

/**
 * char supplier
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/1/22 22:00
 */
@FunctionalInterface
public interface CharSupplier {

    /**
     * 获取 char 值
     *
     * @return char
     */
    char getAsChar();

}

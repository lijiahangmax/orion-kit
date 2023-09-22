package com.orion.lang.function;

import com.orion.lang.define.Console;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;

/**
 * 函数常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/20 18:50
 */
@SuppressWarnings("unchecked")
public class Functions {

    public static final Consumer<Object> EMPTY_CONSUMER = t -> {
    };
    public static final BiConsumer<Object, Object> EMPTY_BI_CONSUMER = (t, u) -> {
    };
    public static final BinaryOperator<Object> MERGE_LEFT = (o1, o2) -> o1;
    public static final BinaryOperator<Object> MERGE_RIGHT = (o1, o2) -> o2;

    private Functions() {
    }

    /**
     * 空实现的 Consumer
     */
    public static <T> Consumer<T> emptyConsumer() {
        return (Consumer<T>) EMPTY_CONSUMER;
    }

    /**
     * 空实现 BiConsumer
     */
    public static <T, U> BiConsumer<T, U> emptyBiConsumer() {
        return (BiConsumer<T, U>) EMPTY_BI_CONSUMER;
    }

    /**
     * 打印的 Consumer
     */
    public static <T> Consumer<T> printConsumer() {
        return Console::trace;
    }

    /**
     * 合并 使用左侧
     *
     * @param <T> T
     * @return BinaryOperator
     */
    public static <T> BinaryOperator<T> left() {
        return (BinaryOperator<T>) MERGE_LEFT;
    }

    /**
     * 合并 使用右侧
     *
     * @param <T> T
     * @return BinaryOperator
     */
    public static <T> BinaryOperator<T> right() {
        return (BinaryOperator<T>) MERGE_RIGHT;
    }

}

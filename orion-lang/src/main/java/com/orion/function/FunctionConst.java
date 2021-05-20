package com.orion.function;

import com.orion.lang.Console;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 函数常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/20 18:50
 */
@SuppressWarnings("unchecked")
public class FunctionConst {

    private FunctionConst() {
    }

    /**
     * 空实现的 Consumer
     */
    public static final Consumer<?> EMPTY_CONSUMER = t -> {
    };

    /**
     * 打印的 Consumer
     */
    public static final Consumer<?> PRINT_CONSUMER = Console::trace;

    /**
     * 空实现 BiConsumer
     */
    public static final BiConsumer<?, ?> EMPTY_BI_CONSUMER = (t, u) -> {
    };

    /**
     * 打印的 BiConsumer
     */
    public static final BiConsumer<?, ?> PRINT_BI_CONSUMER = Console::trace;

    /**
     * 打印p1的 BiConsumer
     */
    public static final BiConsumer<?, ?> PRINT_1_BI_CONSUMER = (t, u) -> {
        Console.trace(t);
    };

    /**
     * 打印p2的 BiConsumer
     */
    public static final BiConsumer<?, ?> PRINT_2_BI_CONSUMER = (t, u) -> {
        Console.trace(u);
    };

    // -------------------- getter --------------------

    public static <T> Consumer<T> getEmptyConsumer() {
        return (Consumer<T>) EMPTY_CONSUMER;
    }

    public static <T> Consumer<T> getPrintConsumer() {
        return (Consumer<T>) PRINT_CONSUMER;
    }

    public static <T, U> BiConsumer<T, U> getEmptyBiConsumer() {
        return (BiConsumer<T, U>) EMPTY_BI_CONSUMER;
    }

    public static <T, U> BiConsumer<T, U> getPrintBiConsumer() {
        return (BiConsumer<T, U>) PRINT_BI_CONSUMER;
    }

    public static <T, U> BiConsumer<T, U> getPrint1BiConsumer() {
        return (BiConsumer<T, U>) PRINT_1_BI_CONSUMER;
    }

    public static <T, U> BiConsumer<T, U> getPrint2BiConsumer() {
        return (BiConsumer<T, U>) PRINT_2_BI_CONSUMER;
    }

}

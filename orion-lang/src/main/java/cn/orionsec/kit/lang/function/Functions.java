/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.function;

import cn.orionsec.kit.lang.define.Console;

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

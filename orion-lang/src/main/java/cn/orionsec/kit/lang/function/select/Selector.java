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
package cn.orionsec.kit.lang.function.select;

import cn.orionsec.kit.lang.utils.Exceptions;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 选择器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/5/11 12:00
 */
public class Selector<P, R> {

    /**
     * 参数
     */
    private final P param;

    /**
     * 是否命中
     */
    private boolean selected;

    /**
     * 转换器
     */
    private Function<P, R> factory;

    public Selector(P param) {
        this.param = param;
    }

    public Selector(Supplier<P> supplier) {
        this.param = supplier.get();
    }

    /**
     * 构造
     *
     * @param param param
     * @param <P>   P
     * @param <R>   R
     * @return {@link Selector}
     */
    public static <P, R> Selector<P, R> of(P param) {
        return new Selector<>(param);
    }

    /**
     * 构造
     *
     * @param supplier supplier
     * @param <P>      P
     * @param <R>      R
     * @return {@link Selector}
     */
    public static <P, R> Selector<P, R> of(Supplier<P> supplier) {
        return new Selector<>(supplier);
    }

    /**
     * case
     *
     * @param branch 分支
     * @return test
     */
    public Selector<P, R> test(Branch<P, R> branch) {
        if (!selected) {
            boolean miss = branch.tester().test(param);
            if (miss) {
                this.selected = true;
                this.factory = branch.factory();
            }
        }
        return this;
    }

    /**
     * 获取结果 未命中则抛出异常
     *
     * @return value
     */
    public R get() {
        if (!selected) {
            throw Exceptions.noSuchElement("missed branch");
        }
        return factory.apply(param);
    }

    /**
     * 获取结果 未命中则返回默认值
     *
     * @param function def function
     * @return value
     */
    public R or(Function<P, R> function) {
        return selected ? factory.apply(param) : function.apply(param);
    }

    /**
     * 获取结果 未命中则返回默认值
     *
     * @param supplier def supplier
     * @return value
     */
    public R orGet(Supplier<R> supplier) {
        return selected ? factory.apply(param) : supplier.get();
    }

    /**
     * 获取结果 未命中则返回默认值
     *
     * @param r def
     * @return value
     */
    public R orElse(R r) {
        return selected ? factory.apply(param) : r;
    }

    /**
     * 获取结果 未命中则返回抛出异常
     *
     * @param supplier exception supplier
     * @param <E>      E
     * @return value
     * @throws E exception
     */
    public <E extends Throwable> R orThrow(Supplier<? extends E> supplier) throws E {
        if (!selected) {
            throw supplier.get();
        }
        return factory.apply(param);
    }

    /**
     * 是否命中
     *
     * @return 是否命中
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * 如果命中则处理
     *
     * @param consumer consumer
     */
    public void ifPresent(Consumer<R> consumer) {
        if (selected) {
            consumer.accept(factory.apply(param));
        }
    }

}

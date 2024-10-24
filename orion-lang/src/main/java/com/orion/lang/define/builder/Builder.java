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
package com.orion.lang.define.builder;

import com.orion.lang.able.Buildable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 通用 builder
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/10 10:08
 */
public class Builder<T> implements Buildable<T> {

    /**
     * 初始化器
     */
    private final Supplier<T> initializer;

    /**
     * 修改器
     */
    private final List<Consumer<T>> modifiers;

    public Builder(Supplier<T> instance) {
        this.initializer = instance;
        this.modifiers = new ArrayList<>();
    }

    public static <T> Builder<T> of(Supplier<T> instance) {
        return new Builder<>(instance);
    }

    public <P1> Builder<T> with(Consumer1<T, P1> consumer, P1 p1) {
        Consumer<T> c = instance -> consumer.accept(instance, p1);
        modifiers.add(c);
        return this;
    }

    public <P1, P2> Builder<T> with(Consumer2<T, P1, P2> consumer, P1 p1, P2 p2) {
        Consumer<T> c = instance -> consumer.accept(instance, p1, p2);
        modifiers.add(c);
        return this;
    }

    public <P1, P2, P3> Builder<T> with(Consumer3<T, P1, P2, P3> consumer, P1 p1, P2 p2, P3 p3) {
        Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3);
        modifiers.add(c);
        return this;
    }

    public <P1, P2, P3, P4> Builder<T> with(Consumer4<T, P1, P2, P3, P4> consumer, P1 p1, P2 p2, P3 p3, P4 p4) {
        Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3, p4);
        modifiers.add(c);
        return this;
    }

    public <P1, P2, P3, P4, P5> Builder<T> with(Consumer5<T, P1, P2, P3, P4, P5> consumer, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5) {
        Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3, p4, p5);
        modifiers.add(c);
        return this;
    }

    @Override
    public T build() {
        T value = initializer.get();
        modifiers.forEach(modifier -> modifier.accept(value));
        modifiers.clear();
        return value;
    }

    @FunctionalInterface
    public interface Consumer1<T, P1> {

        /**
         * accept
         */
        void accept(T t, P1 p1);

    }

    @FunctionalInterface
    public interface Consumer2<T, P1, P2> {

        /**
         * accept
         */
        void accept(T t, P1 p1, P2 p2);

    }

    @FunctionalInterface
    public interface Consumer3<T, P1, P2, P3> {

        /**
         * accept
         */
        void accept(T t, P1 p1, P2 p2, P3 p3);

    }

    @FunctionalInterface
    public interface Consumer4<T, P1, P2, P3, P4> {

        /**
         * accept
         */
        void accept(T t, P1 p1, P2 p2, P3 p3, P4 p4);

    }

    @FunctionalInterface
    public interface Consumer5<T, P1, P2, P3, P4, P5> {

        /**
         * accept
         */
        void accept(T t, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5);

    }

}

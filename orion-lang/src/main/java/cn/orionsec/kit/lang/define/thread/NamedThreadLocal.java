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
package cn.orionsec.kit.lang.define.thread;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 带有名称的 ThreadLocal
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/15 17:10
 */
public class NamedThreadLocal<T> extends ThreadLocal<T> {

    private final String name;

    public NamedThreadLocal(String name) {
        this.name = name;
    }

    public static <S> NamedThreadLocal<S> withInitial(String name, Supplier<? extends S> supplier) {
        return new SuppliedThreadLocal<>(name, supplier);
    }

    @Override
    public String toString() {
        return this.name;
    }

    static final class SuppliedThreadLocal<T> extends NamedThreadLocal<T> {

        private final Supplier<? extends T> supplier;

        SuppliedThreadLocal(String name, Supplier<? extends T> supplier) {
            super(name);
            this.supplier = Objects.requireNonNull(supplier);
        }

        @Override
        protected T initialValue() {
            return supplier.get();
        }
    }

}

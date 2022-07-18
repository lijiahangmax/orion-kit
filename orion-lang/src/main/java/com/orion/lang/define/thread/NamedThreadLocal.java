package com.orion.lang.define.thread;

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

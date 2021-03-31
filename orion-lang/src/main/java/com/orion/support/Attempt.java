package com.orion.support;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * unchecked function
 * 将对象抛出的异常转交至函数
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/30 18:47
 */
public class Attempt {

    private Attempt() {
    }

    @FunctionalInterface
    public interface UncheckedConsumer<T, E extends Exception> {
        void accept(T t) throws E;
    }

    @FunctionalInterface
    public interface UncheckedBiConsumer<T, U, E extends Exception> {
        void accept(T t, U u) throws E;
    }

    @FunctionalInterface
    public interface UncheckedFunction<T, R, E extends Exception> {
        R apply(T t) throws E;
    }

    @FunctionalInterface
    public interface UncheckedSupplier<T, E extends Exception> {
        T get() throws E;
    }

    @FunctionalInterface
    public interface UncheckedRunnable<E extends Exception> {
        void run() throws E;
    }

    // -------------------- rethrows --------------------

    public static <T, E extends Exception> Consumer<T> rethrows(UncheckedConsumer<T, E> consumer) throws E {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception exception) {
                throwAsUnchecked(exception);
            }
        };
    }

    public static <T, U, E extends Exception> BiConsumer<T, U> rethrows(UncheckedBiConsumer<T, U, E> biConsumer) throws E {
        return (t, u) -> {
            try {
                biConsumer.accept(t, u);
            } catch (Exception exception) {
                throwAsUnchecked(exception);
            }
        };
    }

    public static <T, R, E extends Exception> Function<T, R> rethrows(UncheckedFunction<T, R, E> function) throws E {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception exception) {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    public static <T, E extends Exception> Supplier<T> rethrows(UncheckedSupplier<T, E> function) throws E {
        return () -> {
            try {
                return function.get();
            } catch (Exception exception) {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    public static <E extends Exception> Runnable rethrows(UncheckedRunnable<E> function) throws E {
        return () -> {
            try {
                function.run();
            } catch (Exception exception) {
                throwAsUnchecked(exception);
            }
        };
    }

    // -------------------- uncheck --------------------

    public static <T, E extends Exception> void uncheck(UncheckedConsumer<T, E> consumer, T t) {
        try {
            consumer.accept(t);
        } catch (Exception exception) {
            throwAsUnchecked(exception);
        }
    }

    public static <T, U, E extends Exception> void uncheck(UncheckedBiConsumer<T, U, E> biConsumer, T t, U u) {
        try {
            biConsumer.accept(t, u);
        } catch (Exception exception) {
            throwAsUnchecked(exception);
        }
    }

    public static <T, R, E extends Exception> R uncheck(UncheckedFunction<T, R, E> function, T t) {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throwAsUnchecked(e);
        }
        return null;
    }

    public static <R, E extends Exception> R uncheck(UncheckedSupplier<R, E> supplier) {
        try {
            return supplier.get();
        } catch (Exception exception) {
            throwAsUnchecked(exception);
        }
        return null;
    }

    public static <E extends Exception> void uncheck(UncheckedRunnable<E> t) {
        try {
            t.run();
        } catch (Exception exception) {
            throwAsUnchecked(exception);
        }
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwAsUnchecked(Exception e) throws E {
        throw (E) e;
    }

}

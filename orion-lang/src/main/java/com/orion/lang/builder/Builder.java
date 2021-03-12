package com.orion.lang.builder;

import com.orion.able.Buildable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 通用builder
 *
 * @author ljh15
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
    private List<Consumer<T>> modifiers = new ArrayList<>();

    public Builder(Supplier<T> instance) {
        this.initializer = instance;
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

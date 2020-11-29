package com.orion.lang.thread;

/**
 * 带有Name的ThreadLocal
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/15 17:10
 */
public class NamedThreadLocal<T> extends ThreadLocal<T> {

    private String name;

    public NamedThreadLocal(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}

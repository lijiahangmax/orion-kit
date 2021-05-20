package com.orion.utils;

import java.lang.ref.*;

/**
 * 引用工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/18 21:30
 */
public class References {

    private References() {
    }

    /**
     * 获得引用
     *
     * @param <T>      引用对象类型
     * @param type     引用类型
     * @param referent 引用对象
     * @return Reference
     */
    public static <T> Reference<T> create(ReferenceType type, T referent) {
        return create(type, referent, null);
    }

    /**
     * 获得引用
     *
     * @param <T>      引用对象类型
     * @param type     引用类型
     * @param referent 引用对象
     * @param queue    引用队列
     * @return Reference
     */
    public static <T> Reference<T> create(ReferenceType type, T referent, ReferenceQueue<T> queue) {
        switch (type) {
            case SOFT:
                return new SoftReference<>(referent);
            case WEAK:
                return new WeakReference<>(referent);
            case PHANTOM:
                return new PhantomReference<>(referent, queue);
            default:
                return null;
        }
    }

    /**
     * 引用类型
     */
    public enum ReferenceType {

        /**
         * 软引用
         */
        SOFT,

        /**
         * 弱引用
         */
        WEAK,

        /**
         * 虚引用
         */
        PHANTOM
    }

}

/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.lang.utils;

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

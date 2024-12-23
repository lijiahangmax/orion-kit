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
package cn.orionsec.kit.lang.utils.reflect;

import cn.orionsec.kit.lang.utils.Strings;

/**
 * 反射 包工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/15 13:19
 */
public class Packages {

    private Packages() {
    }

    /**
     * 获取包名
     *
     * @param o ignore
     * @return ignore
     */
    public static String getPackageName(Object o) {
        return o == null ? Strings.EMPTY : getPackageName(o.getClass().getName());
    }

    /**
     * 获取包名
     *
     * @param clazz ignore
     * @return ignore
     */
    public static String getPackageName(Class<?> clazz) {
        return getPackageName(clazz.getName());
    }

    /**
     * 获取包名
     *
     * @param className ignore
     * @return ignore
     */
    public static String getPackageName(String className) {
        int lastIndex = className.lastIndexOf(".");
        return (lastIndex != -1 ? className.substring(0, lastIndex) : Strings.EMPTY);
    }

    /**
     * 获取包
     *
     * @param o ignore
     * @return ignore
     */
    public static Package getPackage(Object o) {
        return o == null ? null : getPackage(getPackageName(o.getClass().getName()));
    }

    /**
     * 获取包
     *
     * @param clazz ignore
     * @return ignore
     */
    public static Package getPackage(Class<?> clazz) {
        return getPackage(getPackageName(clazz.getName()));
    }

    /**
     * 获取包
     *
     * @param packageName ignore
     * @return ignore
     */
    public static Package getPackage(String packageName) {
        return Package.getPackage(packageName);
    }

}

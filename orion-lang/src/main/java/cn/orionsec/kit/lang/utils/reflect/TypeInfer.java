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
package cn.orionsec.kit.lang.utils.reflect;

import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.convert.TypeStore;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 反射 类型推断
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/22 10:41
 */
public class TypeInfer {

    private TypeInfer() {
    }

    /**
     * 推断创建对象
     *
     * @param constructors 构造
     * @param params       参数
     * @return 对象
     */
    public static <T> T newInstanceInfer(List<Constructor<T>> constructors, Object[] params) {
        int index = -1;
        Class<?>[] sourceClasses = Arrays1.mapper(params, Class<?>[]::new, Object::getClass);
        List<Class<?>[]> targetClassesList = constructors.stream()
                .filter(m -> m.getParameterCount() == params.length)
                .peek(Constructors::setAccessible)
                .map(Constructor::getParameterTypes)
                .collect(Collectors.toList());
        for (int i = 0; i < 3; i++) {
            if (index == -1) {
                index = allTypeMatch(sourceClasses, targetClassesList, i + 1);
            }
        }
        if (index == -1) {
            throw Exceptions.invoke("could infer new instance, not found match constructor");
        }
        Class<?>[] targetClass = targetClassesList.get(index);
        Object[] inferParams = convertType(params, targetClass);
        return Constructors.newInstance(constructors.get(index), inferParams);
    }

    /**
     * 推断调用方法
     *
     * @param o       o
     * @param methods 方法
     * @param params  参数
     * @return 返回值
     */
    public static <T> T invokeInfer(Object o, List<Method> methods, Object[] params) {
        int index = -1;
        Class<?>[] sourceClasses = Arrays1.mapper(params, Class<?>[]::new, Object::getClass);
        List<Class<?>[]> targetClassesList = methods.stream()
                .filter(m -> m.getParameterCount() == params.length)
                .peek(Methods::setAccessible)
                .map(Method::getParameterTypes)
                .collect(Collectors.toList());
        for (int i = 0; i < 3; i++) {
            if (index == -1) {
                index = allTypeMatch(sourceClasses, targetClassesList, i + 1);
            }
        }
        if (index == -1) {
            throw Exceptions.invoke("could infer invoke method, not found match method");
        }
        Class<?>[] targetClass = targetClassesList.get(index);
        Object[] inferParams = convertType(params, targetClass);
        return Methods.invokeMethod(o, methods.get(index), inferParams);
    }

    /**
     * 类型是否全匹配
     *
     * @param sourceClasses     源class
     * @param targetClassesList 所有目标class
     * @param type              1严格匹配 2可直接转换 3可以转换
     * @return true 可以转换
     */
    public static int allTypeMatch(Class<?>[] sourceClasses, List<Class<?>[]> targetClassesList, int type) {
        for (int i = 0; i < targetClassesList.size(); i++) {
            Class<?>[] targetClasses = targetClassesList.get(i);
            if (targetClasses.length != sourceClasses.length) {
                continue;
            }
            if (allTypeMatch(sourceClasses, targetClasses, type)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 类型是否全匹配
     *
     * @param sourceClasses 源class
     * @param targetClasses 目标class
     * @param type          1严格匹配 2可直接转换 3可以转换
     * @return true 可以转换
     */
    private static boolean allTypeMatch(Class<?>[] sourceClasses, Class<?>[] targetClasses, int type) {
        for (int i = 0; i < sourceClasses.length; i++) {
            Class<?> sourceClass = sourceClasses[i];
            Class<?> targetClass = targetClasses[i];
            if (type == 1) {
                // 严格
                if (!sourceClass.equals(targetClass)) {
                    return false;
                }
            } else if (type == 2) {
                // 直接转换
                if (!TypeStore.canDirectConvert(sourceClass, targetClass)) {
                    return false;
                }
            } else if (type == 3) {
                // 类型转换
                if (!TypeStore.canConvert(sourceClass, targetClass)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 转换源数组类型
     *
     * @param sourceArray   源数组
     * @param targetClasses 目标类型
     * @return 目标类型
     */
    private static Object[] convertType(Object[] sourceArray, Class<?>[] targetClasses) {
        Object[] r = new Object[sourceArray.length];
        for (int i = 0; i < sourceArray.length; i++) {
            r[i] = TypeStore.STORE.to(sourceArray[i], targetClasses[i]);
        }
        return r;
    }

}

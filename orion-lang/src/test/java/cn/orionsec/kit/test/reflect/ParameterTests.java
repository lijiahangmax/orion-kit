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
package cn.orionsec.kit.test.reflect;

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.reflect.Constructors;
import cn.orionsec.kit.lang.utils.reflect.Methods;
import cn.orionsec.kit.test.reflect.value.User;
import cn.orionsec.kit.test.reflect.value.UserAnno1;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/4 18:03
 */
public class ParameterTests {

    @Test
    public void testParameter() {
        Method method = Methods.getAccessibleMethod(User.class, "say");
        method = Assert.notNull(method);
        Parameter[] parameters = method.getParameters();
        for (Parameter p : parameters) {
            System.out.println(p.getAnnotation(UserAnno1.class));
            System.out.println(p.getType());
            System.out.println(p.getParameterizedType());
        }
        System.out.println("---");
        Constructor<User> c = Constructors.getConstructor(User.class, 2);
        c = Assert.notNull(c);
        parameters = c.getParameters();
        for (Parameter p : parameters) {
            System.out.println(p.getAnnotation(UserAnno1.class));
            System.out.println(p.getType());
            System.out.println(p.getParameterizedType());
        }
    }

    @Test
    public void testParameter1() {
        Method method = Methods.getAccessibleMethod(User.class, "setId");
        method = Assert.notNull(method);
        for (Annotation[] a : method.getParameterAnnotations()) {
            System.out.println(a.length);
        }
    }

}

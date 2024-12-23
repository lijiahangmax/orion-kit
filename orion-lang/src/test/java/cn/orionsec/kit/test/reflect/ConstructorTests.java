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

import cn.orionsec.kit.lang.define.Console;
import cn.orionsec.kit.lang.utils.reflect.Constructors;
import cn.orionsec.kit.test.reflect.value.Shop;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/23 18:33
 */
public class ConstructorTests {

    @Test
    public void getConstructorTests() {
        Constructors.getConstructors(Shop.class).forEach(Console::trace);
        System.out.println("------");
        System.out.println(Constructors.getDefaultConstructor(Shop.class));
        System.out.println("------");
        Constructors.getConstructors(Shop.class, 0).forEach(Console::trace);
        System.out.println("------");
        Constructors.getConstructors(Shop.class, 1).forEach(Console::trace);
        System.out.println("------");
        Constructors.getConstructors(Shop.class, 2).forEach(Console::trace);
        System.out.println("------");
        Constructors.getConstructors(Shop.class, 3).forEach(Console::trace);
        System.out.println("------");
        System.out.println(Constructors.getConstructor(Shop.class, 0));
        System.out.println(Constructors.getConstructor(Shop.class, 1));
        System.out.println(Constructors.getConstructor(Shop.class, 2));
        System.out.println(Constructors.getConstructor(Shop.class, 3));
        System.out.println("------");
        System.out.println(Constructors.getConstructor(Shop.class));
        System.out.println(Constructors.getConstructor(Shop.class, Long.class));
        System.out.println(Constructors.getConstructor(Shop.class, Integer.class));
        System.out.println(Constructors.getConstructor(Shop.class, String.class));
        System.out.println(Constructors.getConstructor(Shop.class, String.class, Long.class));
    }

    @Test
    public void newInstanceTest() {
        System.out.println(Constructors.newInstance(Shop.class));
        System.out.println(Constructors.newInstance(Constructors.getDefaultConstructor(Shop.class)));
        System.out.println(Constructors.newInstance(Constructors.getDefaultConstructor(Shop.class)));
        System.out.println("------");
        System.out.println(Constructors.newInstance(Constructors.getConstructor(Shop.class, Long.class), 1L));
        System.out.println(Constructors.newInstance(Constructors.getConstructor(Shop.class, String.class), "ss"));
        System.out.println(Constructors.newInstance(Constructors.getConstructor(Shop.class, String.class, Long.class), "ss1", 2L));
        System.out.println("------");
        System.out.println(Constructors.newInstance(Shop.class, new Class[]{Long.class}, 1L));
        System.out.println(Constructors.newInstance(Shop.class, new Class[]{String.class}, "ss"));
        System.out.println(Constructors.newInstance(Shop.class, new Class[]{String.class, Long.class}, "ss1", 11L));
        System.out.println("------");
        System.out.println(Constructors.newInstanceInfer(Shop.class, 1L));
        System.out.println(Constructors.newInstanceInfer(Shop.class, "1L"));
        System.out.println(Constructors.newInstanceInfer(Shop.class, 123, "1"));
        System.out.println("------");
        System.out.println(Constructors.newInstanceInfer(Constructors.getConstructor(Shop.class, Long.class), "22"));
        System.out.println(Constructors.newInstanceInfer(Constructors.getConstructor(Shop.class, Long.class), 22));
        System.out.println(Constructors.newInstanceInfer(Constructors.getConstructor(Shop.class, String.class), 22.22));
        System.out.println(Constructors.newInstanceInfer(Constructors.getConstructor(Shop.class, String.class), 22));
        System.out.println(Constructors.newInstanceInfer(Constructors.getConstructor(Shop.class, String.class, Long.class), 22.22, 22.22));
    }

}

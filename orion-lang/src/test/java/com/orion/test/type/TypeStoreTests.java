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
package com.orion.test.type;

import com.orion.lang.utils.convert.TypeStore;
import org.junit.Test;

import java.io.Serializable;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/3 12:35
 */
public class TypeStoreTests {

    @Test
    public void test1() {
        System.out.println(TypeStore.STORE.to(new Integer[]{}, int[].class).getClass());
        System.out.println(TypeStore.STORE.to(new Integer[]{}, Integer[].class).getClass());
        System.out.println(TypeStore.STORE.to(new int[]{}, int[].class).getClass());
        System.out.println(TypeStore.STORE.to(new int[]{}, Integer[].class).getClass());
        System.out.println(TypeStore.STORE.to(new int[]{}, Long[].class).getClass());
    }

    @Test
    public void test2() {
        System.out.println(TypeStore.STORE.getAllSuitableClasses(Integer[].class));
        System.out.println(TypeStore.STORE.getAllSuitableClasses(Integer.class));
        System.out.println(TypeStore.STORE.getAllSuitableClasses(Serializable.class));
    }

    @Test
    public void test3() {
        System.out.println(TypeStore.STORE.to(new int[]{1, 2, 3}, Integer[].class).length);
        System.out.println(TypeStore.STORE.to(new int[]{1, 2, 3}, Long[].class).length);
    }

    @Test
    public void test4() {
        TypeStore.STORE.getConversionMapping().forEach((k, v) -> {
            v.forEach((v1, vv) -> {
                System.out.println(k + " " + v1);
            });
        });
    }

}

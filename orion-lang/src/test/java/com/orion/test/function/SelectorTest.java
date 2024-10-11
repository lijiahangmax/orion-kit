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
package com.orion.test.function;

import com.orion.lang.function.select.Branches;
import com.orion.lang.function.select.Selector;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/5/13 16:52
 */
public class SelectorTest {

    @Test
    public void test1() {
        String va = Selector.<Integer, String>of(4)
                .test(Branches.eq(1).then("一"))
                .test(Branches.eq(2).then("二"))
                .test(Branches.eq(3).then("三"))
                .orElse("def");
        System.out.println(va);
    }

    @Test
    public void test2() {
        String va = Selector.<Integer, String>of(6)
                .test(Branches.in(1, 4).then("一 or 四"))
                .test(Branches.compared(2).then(s -> (s + 10) + ""))
                .test(Branches.eq(3).then("三"))
                .test(Branches.eq(5).then(() -> "555"))
                .or(Object::toString);
        System.out.println(va);
    }

}

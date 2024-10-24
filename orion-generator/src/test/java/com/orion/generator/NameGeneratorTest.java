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
package com.orion.generator;

import com.orion.generator.name.NameGenerator;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/8 22:34
 */
public class NameGeneratorTest {

    @Test
    public void testBoy1() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NameGenerator.generatorName(true));
        }
    }

    @Test
    public void testBoy2() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NameGenerator.generatorName(true, true));
        }
    }

    @Test
    public void testBoy3() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NameGenerator.generatorName(true, false));
        }
    }

    @Test
    public void testGirl1() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NameGenerator.generatorName(false));
        }
    }

    @Test
    public void testGirl2() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NameGenerator.generatorName(false, true));
        }
    }

    @Test
    public void testGirl3() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NameGenerator.generatorName(false, false));
        }
    }

    @Test
    public void nextFirstNam() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NameGenerator.generatorFirstName());
        }
    }

}

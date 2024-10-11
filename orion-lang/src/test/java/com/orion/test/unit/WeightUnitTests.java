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
package com.orion.test.unit;

import com.orion.lang.utils.unit.WeightUnit;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/25 18:55
 */
public class WeightUnitTests {

    public static void main(String[] args) {
        System.out.println(WeightUnit.MG.toMilligram(1000L));
        System.out.println(WeightUnit.MG.toGram(1000L));
        System.out.println(WeightUnit.MG.toKilogram(1000L));
        System.out.println(WeightUnit.MG.toTon(1000L));
        System.out.println("------");
        System.out.println(WeightUnit.G.toMilligram(1000L));
        System.out.println(WeightUnit.G.toGram(1000L));
        System.out.println(WeightUnit.G.toKilogram(1000L));
        System.out.println(WeightUnit.G.toTon(1000L));
        System.out.println("------");
        System.out.println(WeightUnit.KG.toMilligram(1000L));
        System.out.println(WeightUnit.KG.toGram(1000L));
        System.out.println(WeightUnit.KG.toKilogram(1000L));
        System.out.println(WeightUnit.KG.toTon(1000L));
        System.out.println("------");
        System.out.println(WeightUnit.T.toMilligram(1000L));
        System.out.println(WeightUnit.T.toGram(1000L));
        System.out.println(WeightUnit.T.toKilogram(1000L));
        System.out.println(WeightUnit.T.toTon(1000L));
    }

}

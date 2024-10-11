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

import com.orion.lang.utils.unit.LengthUnit;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/26 11:27
 */
public class LengthUnitTests {

    public static void main(String[] args) {
        System.out.println(LengthUnit.MM.toMillimetre(1000L));
        System.out.println(LengthUnit.MM.toCentimeter(1000L));
        System.out.println(LengthUnit.MM.toDecimetre(1000L));
        System.out.println(LengthUnit.MM.toMetre(1000L));
        System.out.println(LengthUnit.MM.toKilometre(1000L));
        System.out.println("------");
        System.out.println(LengthUnit.CM.toMillimetre(1000L));
        System.out.println(LengthUnit.CM.toCentimeter(1000L));
        System.out.println(LengthUnit.CM.toDecimetre(1000L));
        System.out.println(LengthUnit.CM.toMetre(1000L));
        System.out.println(LengthUnit.CM.toKilometre(1000L));
        System.out.println("------");
        System.out.println(LengthUnit.DM.toMillimetre(1000L));
        System.out.println(LengthUnit.DM.toCentimeter(1000L));
        System.out.println(LengthUnit.DM.toDecimetre(1000L));
        System.out.println(LengthUnit.DM.toMetre(1000L));
        System.out.println(LengthUnit.DM.toKilometre(1000L));
        System.out.println("------");
        System.out.println(LengthUnit.M.toMillimetre(1000L));
        System.out.println(LengthUnit.M.toCentimeter(1000L));
        System.out.println(LengthUnit.M.toDecimetre(1000L));
        System.out.println(LengthUnit.M.toMetre(1000L));
        System.out.println(LengthUnit.M.toKilometre(1000L));
        System.out.println("------");
        System.out.println(LengthUnit.KM.toMillimetre(1000L));
        System.out.println(LengthUnit.KM.toCentimeter(1000L));
        System.out.println(LengthUnit.KM.toDecimetre(1000L));
        System.out.println(LengthUnit.KM.toMetre(1000L));
        System.out.println(LengthUnit.KM.toKilometre(1000L));
    }

}

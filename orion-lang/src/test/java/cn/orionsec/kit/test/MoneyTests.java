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
package cn.orionsec.kit.test;

import cn.orionsec.kit.lang.utils.Moneys;
import cn.orionsec.kit.lang.utils.random.Randoms;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.IntStream;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/7/13 18:39
 */
public class MoneyTests {

    public static void main(String[] args) {
        IntStream.range(0, 30000).forEach((i) -> {
            long l = Randoms.randomLong(-10000000000000L, 10000000000000L);
            BigDecimal d = BigDecimal.valueOf(l).setScale(2, RoundingMode.DOWN);
            String s = Moneys.toCurrency(d);
            BigDecimal d1 = Moneys.toDecimal(s);
            if (!d1.equals(d)) {
                System.out.println(s);
                System.out.println(d);
                System.out.println(d1);
                System.out.println("-----------------");
            }
        });
        IntStream.range(0, 30000).forEach((i) -> {
            double l = Randoms.randomDouble(-10000000000000.00, 10000000000000.00);
            BigDecimal d = BigDecimal.valueOf(l).setScale(2, RoundingMode.DOWN);
            String s = Moneys.toCurrency(d);
            BigDecimal d1 = Moneys.toDecimal(s);
            if (!d1.equals(d)) {
                System.out.println(s);
                System.out.println(d);
                System.out.println(d1);
                System.out.println("-----------------");
            }
        });
    }

}

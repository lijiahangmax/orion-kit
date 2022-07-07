package com.orion.test;

import com.orion.lang.utils.Moneys;
import com.orion.lang.utils.random.Randoms;

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

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

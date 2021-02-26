package com.orion.test.unit;

import com.orion.utils.unit.LengthUnit;

/**
 * @author ljh15
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

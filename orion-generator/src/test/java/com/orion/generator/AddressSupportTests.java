package com.orion.generator;

import com.orion.generator.addres.AddressGenerator;
import com.orion.generator.addres.Nationalities;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/8 15:41
 */
public class AddressSupportTests {

    @Test
    public void gen1() {
        for (int i = 0; i < 20; i++) {
            System.out.println(AddressGenerator.generatorAddress());
        }
    }

    @Test
    public void gen2() {
        for (int i = 0; i < 20; i++) {
            System.out.println(AddressGenerator.generatorCommunityAddress());
        }
    }

    @Test
    public void gen3() {
        for (int i = 0; i < 20; i++) {
            System.out.println(AddressGenerator.generatorStreetAddress());
        }
    }

    @Test
    public void gen4() {
        for (int i = 0; i < 20; i++) {
            System.out.println(AddressGenerator.generatorVillageAddress());
        }
    }

    @Test
    public void gen5() {
        for (int i = 0; i < 20; i++) {
            System.out.println(AddressGenerator.generatorIdCardAddress());
        }
    }

    @Test
    public void gen6() {
        for (int i = 0; i < 20; i++) {
            System.out.println(AddressGenerator.generatorName());
        }
    }

    @Test
    public void getNation() {
        int i = 0;
        for (int j = 0; j < 1000; j++) {
            String nation = Nationalities.getNation(11);
            if (!nation.equals("汉族")) {
                System.out.println(nation);
                i++;
            }
        }
        System.out.println(i);
    }

    @Test
    public void getMinority() {
        for (int j = 0; j < 30; j++) {
            System.out.println(Nationalities.getMinority());
        }
    }

}

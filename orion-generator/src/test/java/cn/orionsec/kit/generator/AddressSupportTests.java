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
package cn.orionsec.kit.generator;

import cn.orionsec.kit.generator.addres.AddressGenerator;
import cn.orionsec.kit.generator.addres.Nationalities;
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

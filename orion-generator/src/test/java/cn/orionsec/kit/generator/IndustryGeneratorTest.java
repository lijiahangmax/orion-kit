/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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

import cn.orionsec.kit.generator.industry.IndustryGenerator;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/19 16:44
 */
public class IndustryGeneratorTest {

    @Test
    public void gen0() {
        for (int i = 0; i < 30; i++) {
            System.out.println((i + 1) + " " + IndustryGenerator.generatorIndustry(i + 1));
            System.out.println((i + 1) + " " + IndustryGenerator.generatorIndustry(i + 1));
            System.out.println((i + 1) + " " + IndustryGenerator.generatorIndustry(i + 1));
            System.out.println((i + 1) + " " + IndustryGenerator.generatorIndustry(i + 1));
            System.out.println((i + 1) + " " + IndustryGenerator.generatorIndustry(i + 1));
        }
    }

    @Test
    public void gen1() {
        for (int i = 0; i < 20; i++) {
            System.out.println(IndustryGenerator.generatorIndustry());
        }
    }

    @Test
    public void gen2() {
        for (int i = 0; i < 20; i++) {
            System.out.println(IndustryGenerator.generatorManagementType());
        }
    }

    @Test
    public void gen3() {
        for (int i = 0; i < 30; i++) {
            System.out.println((i + 1) + " " + IndustryGenerator.generatorManagementType(i + 1));
            System.out.println((i + 1) + " " + IndustryGenerator.generatorManagementType(i + 1));
            System.out.println((i + 1) + " " + IndustryGenerator.generatorManagementType(i + 1));
            System.out.println((i + 1) + " " + IndustryGenerator.generatorManagementType(i + 1));
            System.out.println((i + 1) + " " + IndustryGenerator.generatorManagementType(i + 1));
        }
    }

    @Test
    public void gen4() {
        for (int i = 0; i < 20; i++) {
            System.out.println(IndustryGenerator.generatorManagementType(IndustryGenerator.generatorIndustry()));
        }
    }

}

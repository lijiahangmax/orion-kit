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

import cn.orionsec.kit.generator.education.EducationGenerator;
import cn.orionsec.kit.generator.education.UniversityGenerator;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/13 15:35
 */
public class UniversityGeneratorTest {

    @Test
    public void gen2() {
        for (int i = 0; i < 20; i++) {
            System.out.println(UniversityGenerator.generatorUniversity());
        }
    }

    @Test
    public void gen3() {
        for (int i = 0; i < 20; i++) {
            System.out.println(UniversityGenerator.generatorUniversityHigh());
        }
    }

    @Test
    public void gen4() {
        for (int i = 0; i < 20; i++) {
            System.out.println(UniversityGenerator.generatorUniversityLow());
        }
    }

    @Test
    public void gen5() {
        for (int i = 0; i < 30; i++) {
            String edu = EducationGenerator.generatorEducation();
            System.out.println(edu + " " + UniversityGenerator.generatorUniversity(edu));
        }
    }

}

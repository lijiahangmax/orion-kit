package com.orion.generator;

import com.orion.generator.education.EducationGenerator;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/13 17:16
 */
public class EducationGeneratorTest {

    @Test
    public void gen() {
        for (int i = 0; i < 30; i++) {
            System.out.println((i + 1) + " " + EducationGenerator.generatorEducation(i + 1));
            System.out.println((i + 1) + " " + EducationGenerator.generatorEducation(i + 1));
            System.out.println((i + 1) + " " + EducationGenerator.generatorEducation(i + 1));
            System.out.println((i + 1) + " " + EducationGenerator.generatorEducation(i + 1));
            System.out.println((i + 1) + " " + EducationGenerator.generatorEducation(i + 1));
        }
    }

    @Test
    public void gen1() {
        for (int i = 0; i < 20; i++) {
            System.out.println(EducationGenerator.generatorEducation());
        }
    }

}

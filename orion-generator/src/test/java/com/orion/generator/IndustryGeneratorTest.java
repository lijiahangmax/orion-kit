package com.orion.generator;

import com.orion.generator.industry.IndustryGenerator;
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

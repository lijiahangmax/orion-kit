package com.orion.generator;

import com.orion.generator.company.CompanyGenerator;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/19 17:44
 */
public class CompanyGeneratorTest {

    @Test
    public void gen1() {
        for (int i = 0; i < 20; i++) {
            System.out.println(CompanyGenerator.generatorCompanyName());
        }
    }

    @Test
    public void gen2() {
        for (int i = 0; i < 20; i++) {
            System.out.println(CompanyGenerator.generatorCompanyName(11));
        }
    }

    @Test
    public void gen3() {
        for (int i = 0; i < 20; i++) {
            System.out.println(CompanyGenerator.generatorCompanyName("服务"));
        }
    }

    @Test
    public void gen4() {
        for (int i = 0; i < 20; i++) {
            System.out.println(CompanyGenerator.generatorCompanyName(15, "服务"));
        }
    }

}

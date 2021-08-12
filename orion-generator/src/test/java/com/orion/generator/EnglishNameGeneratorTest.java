package com.orion.generator;

import com.orion.generator.name.EnglishNameGenerator;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/11 12:55
 */
public class EnglishNameGeneratorTest {

    @Test
    public void gen() {
        for (int i = 0; i < 100; i++) {
            System.out.println(EnglishNameGenerator.generatorName());
        }
    }

    @Test
    public void gen1() {
        for (int i = 0; i < 100; i++) {
            System.out.println(EnglishNameGenerator.generatorName1());
        }
    }

    @Test
    public void gen2() {
        for (int i = 0; i < 100; i++) {
            System.out.println(EnglishNameGenerator.generatorName2());
        }
    }

}

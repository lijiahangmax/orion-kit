package com.orion.generator;

import com.orion.generator.email.EmailGenerator;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/12 12:41
 */
public class EmailGeneratorTest {

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            System.out.println(EmailGenerator.generatorEmail());
        }
    }

    @Test
    public void test1() {
        for (int i = 0; i < 10; i++) {
            System.out.println(EmailGenerator.generatorRealEmail());
        }
    }

    @Test
    public void test2() {
        for (int i = 0; i < 10; i++) {
            System.out.println(EmailGenerator.generatorRandomEmail());
        }
    }

}

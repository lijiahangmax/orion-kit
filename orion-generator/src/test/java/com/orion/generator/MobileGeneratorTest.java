package com.orion.generator;

import com.orion.generator.mobile.MobileGenerator;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/11 12:33
 */
public class MobileGeneratorTest {

    @Test
    public void getPrefix() {
        for (int i = 0; i < 100; i++) {
            System.out.println(MobileGenerator.getMobilePrefix());
        }
    }

    @Test
    public void gen() {
        for (int i = 0; i < 100; i++) {
            System.out.println(MobileGenerator.generateMobile());
        }
    }

}

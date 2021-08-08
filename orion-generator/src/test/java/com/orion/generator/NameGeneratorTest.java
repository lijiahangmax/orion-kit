package com.orion.generator;

import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/8 22:34
 */
public class NameGeneratorTest {

    @Test
    public void testBoy1() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NameGenerator.nextName(true));
        }
    }

    @Test
    public void testBoy2() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NameGenerator.nextName(true, true));
        }
    }

    @Test
    public void testBoy3() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NameGenerator.nextName(true, false));
        }
    }

    @Test
    public void testGirl1() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NameGenerator.nextName(false));
        }
    }

    @Test
    public void testGirl2() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NameGenerator.nextName(false, true));
        }
    }

    @Test
    public void testGirl3() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NameGenerator.nextName(false, false));
        }
    }

    @Test
    public void nextFirstNam() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NameGenerator.nextFirstName());
        }
    }

}

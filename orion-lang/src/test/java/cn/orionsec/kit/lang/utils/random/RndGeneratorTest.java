package cn.orionsec.kit.lang.utils.random;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * RndGenerator 伪随机数发生器测试
 */
public class RndGeneratorTest {

    @Test
    public void testDefaultConstructor() {
        RndGenerator gen = new RndGenerator();
        long val = gen.next();
        assertTrue(val > 0);
    }

    @Test
    public void testWithSeed() {
        RndGenerator gen = new RndGenerator(123456L);
        long val = gen.next();
        assertTrue(val > 0);
    }

    @Test
    public void testWithSeedAndBit() {
        RndGenerator gen = new RndGenerator(123456L, 4);
        long val = gen.next();
        // 4 bits means result should be <= 9999
        assertTrue(val >= 0 && val <= 9999);
    }

    @Test
    public void testMultipleNext() {
        RndGenerator gen = new RndGenerator(987654L, 4);
        Set<Long> results = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            long val = gen.next();
            results.add(val);
            assertTrue(val >= 0 && val <= 9999);
        }
        // Should produce some different values
        assertTrue(results.size() > 1);
    }

    @Test
    public void testDeterministic() {
        RndGenerator gen1 = new RndGenerator(555555L, 4);
        RndGenerator gen2 = new RndGenerator(555555L, 4);
        // Same seed should produce same sequence
        for (int i = 0; i < 5; i++) {
            assertEquals(gen1.next(), gen2.next());
        }
    }

    @Test
    public void testDifferentSeeds() {
        RndGenerator gen1 = new RndGenerator(111111L, 4);
        RndGenerator gen2 = new RndGenerator(999999L, 4);
        // Different seeds should likely produce different first values
        long v1 = gen1.next();
        long v2 = gen2.next();
        // This is probabilistic but with very different seeds, likely different
        assertNotEquals(v1, v2);
    }
}

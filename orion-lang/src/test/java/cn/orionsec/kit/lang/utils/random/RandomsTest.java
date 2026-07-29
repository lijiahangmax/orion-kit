package cn.orionsec.kit.lang.utils.random;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Randoms 工具类测试
 */
public class RandomsTest {

    @Test
    public void testRandomInt() {
        int r = Randoms.randomInt();
        assertTrue(r >= 0);
    }

    @Test
    public void testRandomIntMax() {
        for (int i = 0; i < 100; i++) {
            int r = Randoms.randomInt(10);
            assertTrue(r >= 0 && r < 10);
        }
    }

    @Test
    public void testRandomIntRange() {
        for (int i = 0; i < 100; i++) {
            int r = Randoms.randomInt(5, 10);
            assertTrue(r >= 5 && r < 10);
        }
    }

    @Test
    public void testRandomDouble() {
        double r = Randoms.randomDouble();
        assertTrue(r >= 0);
    }

    @Test
    public void testRandomDoubleRange() {
        for (int i = 0; i < 100; i++) {
            double r = Randoms.randomDouble(1.0, 5.0);
            assertTrue(r >= 1.0 && r < 5.0);
        }
    }

    @Test
    public void testRandomLong() {
        long r = Randoms.randomLong();
        assertTrue(r >= 0);
    }

    @Test
    public void testRandomLongRange() {
        for (int i = 0; i < 100; i++) {
            long r = Randoms.randomLong(100, 200);
            assertTrue(r >= 100 && r < 200);
        }
    }

    @Test
    public void testRandomBoolean() {
        // 多次调用, 应该有 true 也有 false
        int trueCount = 0;
        for (int i = 0; i < 100; i++) {
            if (Randoms.randomBoolean()) {
                trueCount++;
            }
        }
        assertTrue(trueCount > 0 && trueCount < 100);
    }

    @Test
    public void testRandomAscii() {
        String result = Randoms.randomAscii(10);
        assertNotNull(result);
        assertEquals(10, result.length());
        // All chars should be alphanumeric
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomLetter() {
        String result = Randoms.randomLetter(8);
        assertNotNull(result);
        assertEquals(8, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomNumber() {
        String result = Randoms.randomNumber(6);
        assertNotNull(result);
        assertEquals(6, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomArray() {
        Integer[] array = {1, 2, 3, 4, 5};
        Randoms.ArrayRandom<Integer> ar = Randoms.randomArray(array);
        List<Integer> results = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Integer next = ar.next();
            assertNotNull(next);
            results.add(next);
        }
        // All elements should be consumed
        assertNull(ar.next());
        assertEquals(5, results.size());
    }
}

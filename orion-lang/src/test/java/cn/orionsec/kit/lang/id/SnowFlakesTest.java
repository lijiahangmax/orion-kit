package cn.orionsec.kit.lang.id;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * SnowFlakes 单元测试
 */
public class SnowFlakesTest {

    @Test
    public void testNextIdNotNull() {
        Long id = SnowFlakes.nextId();
        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    public void testNextIdUnique() {
        Set<Long> ids = new HashSet<>();
        int count = 10000;
        for (int i = 0; i < count; i++) {
            ids.add(SnowFlakes.nextId());
        }
        assertEquals(count, ids.size());
    }

    @Test
    public void testNextIdPositive() {
        for (int i = 0; i < 100; i++) {
            assertTrue(SnowFlakes.nextId() > 0);
        }
    }

}

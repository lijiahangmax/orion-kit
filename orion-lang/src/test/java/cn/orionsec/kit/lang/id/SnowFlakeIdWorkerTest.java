package cn.orionsec.kit.lang.id;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * SnowFlakeIdWorker 单元测试
 */
public class SnowFlakeIdWorkerTest {

    @Test
    public void testNextIdNotNull() {
        SnowFlakeIdWorker worker = new SnowFlakeIdWorker(1, 1);
        Long id = worker.nextId();
        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    public void testNextIdUnique() {
        SnowFlakeIdWorker worker = new SnowFlakeIdWorker(1, 1);
        Set<Long> ids = new HashSet<>();
        int count = 10000;
        for (int i = 0; i < count; i++) {
            ids.add(worker.nextId());
        }
        assertEquals(count, ids.size());
    }

    @Test
    public void testNextIdIncreasing() {
        SnowFlakeIdWorker worker = new SnowFlakeIdWorker(1, 1);
        long prev = worker.nextId();
        for (int i = 0; i < 100; i++) {
            long next = worker.nextId();
            assertTrue(next > prev);
            prev = next;
        }
    }

    @Test
    public void testDifferentWorkerIds() {
        SnowFlakeIdWorker worker1 = new SnowFlakeIdWorker(1, 1);
        SnowFlakeIdWorker worker2 = new SnowFlakeIdWorker(2, 1);
        Long id1 = worker1.nextId();
        Long id2 = worker2.nextId();
        assertNotEquals(id1, id2);
    }

    @Test
    public void testBoundaryWorkerIds() {
        // workerId 和 dataCenterId 范围是 0~31
        SnowFlakeIdWorker worker0 = new SnowFlakeIdWorker(0, 0);
        assertNotNull(worker0.nextId());

        SnowFlakeIdWorker worker31 = new SnowFlakeIdWorker(31, 31);
        assertNotNull(worker31.nextId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWorkerIdTooLarge() {
        new SnowFlakeIdWorker(32, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWorkerIdNegative() {
        new SnowFlakeIdWorker(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataCenterIdTooLarge() {
        new SnowFlakeIdWorker(1, 32);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataCenterIdNegative() {
        new SnowFlakeIdWorker(1, -1);
    }

}

package cn.orionsec.kit.lang.id;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * SequenceIdWorker / Sequences 单元测试
 */
public class SequencesTest {

    @Test
    public void testSequencesNextIdNotNull() {
        Long id = Sequences.nextId();
        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    public void testSequencesNextIdUnique() {
        Set<Long> ids = new HashSet<>();
        int count = 10000;
        for (int i = 0; i < count; i++) {
            ids.add(Sequences.nextId());
        }
        assertEquals(count, ids.size());
    }

    @Test
    public void testSequenceIdWorkerBasic() {
        SequenceIdWorker worker = new SequenceIdWorker(0, 0);
        Long id = worker.nextId();
        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    public void testSequenceIdWorkerUnique() {
        SequenceIdWorker worker = new SequenceIdWorker(1, 1);
        Set<Long> ids = new HashSet<>();
        int count = 10000;
        for (int i = 0; i < count; i++) {
            ids.add(worker.nextId());
        }
        assertEquals(count, ids.size());
    }

    @Test
    public void testSequenceIdWorkerWithClock() {
        SequenceIdWorker worker = new SequenceIdWorker(0, 0, true, false);
        Long id = worker.nextId();
        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    public void testSequenceIdWorkerWithRandomSequence() {
        SequenceIdWorker worker = new SequenceIdWorker(0, 0, false, true);
        Long id = worker.nextId();
        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequenceIdWorkerInvalidDataCenterId() {
        new SequenceIdWorker(4, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequenceIdWorkerInvalidWorkerId() {
        new SequenceIdWorker(0, 256);
    }

    @Test
    public void testSequenceIdWorkerBoundary() {
        // dataCenterId 范围 0~3, workerId 范围 0~255
        SequenceIdWorker worker = new SequenceIdWorker(3, 255);
        Long id = worker.nextId();
        assertNotNull(id);
        assertTrue(id > 0);
    }

}

package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import static org.junit.Assert.*;

public class FixedQueueTest {

    @Test
    public void testCreate() {
        FixedQueue<String> queue = FixedQueue.create(3);
        assertNotNull(queue);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testOffer() {
        FixedQueue<Integer> queue = FixedQueue.create(3);
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        assertEquals(3, queue.size());
        // add beyond capacity, first element should be removed
        queue.offer(4);
        assertEquals(3, queue.size());
        assertEquals(Integer.valueOf(2), queue.peek());
    }

    @Test
    public void testIsFull() {
        FixedQueue<String> queue = FixedQueue.create(2);
        assertFalse(queue.isFull());
        queue.offer("a");
        assertFalse(queue.isFull());
        queue.offer("b");
        assertTrue(queue.isFull());
    }

    @Test
    public void testOverflowMultipleTimes() {
        FixedQueue<Integer> queue = FixedQueue.create(2);
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        assertEquals(2, queue.size());
        assertEquals(Integer.valueOf(3), queue.peek());
    }

    @Test
    public void testPoll() {
        FixedQueue<String> queue = FixedQueue.create(3);
        queue.offer("a");
        queue.offer("b");
        assertEquals("a", queue.poll());
        assertEquals(1, queue.size());
    }
}

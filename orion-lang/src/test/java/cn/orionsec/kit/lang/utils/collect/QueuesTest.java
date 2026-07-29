package cn.orionsec.kit.lang.utils.collect;

import org.junit.Test;

import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;

/**
 * Queues 工具类测试
 */
public class QueuesTest {

    @Test
    public void testNewQueue() {
        Queue<String> queue = Queues.newQueue();
        assertNotNull(queue);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testNewDeque() {
        Deque<String> deque = Queues.newDeque();
        assertNotNull(deque);
        assertTrue(deque.isEmpty());
    }

    @Test
    public void testOf() {
        Queue<Integer> queue = Queues.of(1, 2, 3);
        assertEquals(3, queue.size());
        assertEquals(Integer.valueOf(1), queue.poll());
        assertEquals(Integer.valueOf(2), queue.poll());
        assertEquals(Integer.valueOf(3), queue.poll());
    }

    @Test
    public void testOfd() {
        Deque<Integer> deque = Queues.ofd(1, 2, 3);
        assertEquals(3, deque.size());
        assertEquals(Integer.valueOf(1), deque.pollFirst());
        assertEquals(Integer.valueOf(3), deque.pollLast());
    }

    @Test
    public void testDefQueue() {
        Queue<String> result = Queues.def((Queue<String>) null);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        Queue<String> existing = Queues.of("x");
        assertSame(existing, Queues.def(existing));
    }

    @Test
    public void testDefDeque() {
        Deque<String> result = Queues.def((Deque<String>) null);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        Deque<String> existing = Queues.ofd("x");
        assertSame(existing, Queues.def(existing));
    }

    @Test
    public void testMapQueue() {
        Queue<Integer> queue = Queues.of(1, 2, 3);
        Queue<String> mapped = Queues.map(queue, String::valueOf);
        assertEquals(3, mapped.size());
        assertEquals("1", mapped.poll());
    }

    @Test
    public void testAs() {
        List<String> list = Arrays.asList("a", "b", "c");
        Queue<String> queue = Queues.as(list.iterator());
        assertEquals(3, queue.size());
        assertEquals("a", queue.poll());
    }

    @Test
    public void testNewQueueWithCollection() {
        List<Integer> list = Arrays.asList(10, 20, 30);
        Queue<Integer> queue = Queues.newQueue(list);
        assertEquals(3, queue.size());
    }

    @Test
    public void testNewLimitQueue() {
        Queue<String> queue = Queues.newLimitQueue(3);
        queue.add("a");
        queue.add("b");
        queue.add("c");
        queue.add("d");
        // Limit queue should keep max 3
        assertEquals(3, queue.size());
    }
}

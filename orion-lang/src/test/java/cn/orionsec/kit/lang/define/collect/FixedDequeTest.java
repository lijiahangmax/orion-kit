package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import static org.junit.Assert.*;

public class FixedDequeTest {

    @Test
    public void testCreate() {
        FixedDeque<String> deque = FixedDeque.create(3);
        assertNotNull(deque);
        assertTrue(deque.isEmpty());
    }

    @Test
    public void testOfferLast() {
        FixedDeque<Integer> deque = FixedDeque.create(3);
        deque.offerLast(1);
        deque.offerLast(2);
        deque.offerLast(3);
        assertEquals(3, deque.size());
        // add beyond capacity, first element should be removed
        deque.offerLast(4);
        assertEquals(3, deque.size());
        assertEquals(Integer.valueOf(2), deque.peekFirst());
        assertEquals(Integer.valueOf(4), deque.peekLast());
    }

    @Test
    public void testOfferFirst() {
        FixedDeque<Integer> deque = FixedDeque.create(3);
        deque.offerFirst(1);
        deque.offerFirst(2);
        deque.offerFirst(3);
        assertEquals(3, deque.size());
        // add beyond capacity, last element should be removed
        deque.offerFirst(4);
        assertEquals(3, deque.size());
        assertEquals(Integer.valueOf(4), deque.peekFirst());
        assertEquals(Integer.valueOf(2), deque.peekLast());
    }

    @Test
    public void testIsFull() {
        FixedDeque<String> deque = FixedDeque.create(2);
        assertFalse(deque.isFull());
        deque.offerLast("a");
        assertFalse(deque.isFull());
        deque.offerLast("b");
        assertTrue(deque.isFull());
    }

    @Test
    public void testMixedOperations() {
        FixedDeque<Integer> deque = FixedDeque.create(3);
        deque.offerLast(1);
        deque.offerLast(2);
        deque.offerFirst(0);
        assertTrue(deque.isFull());
        // offerFirst when full => removes last
        deque.offerFirst(-1);
        assertEquals(Integer.valueOf(-1), deque.peekFirst());
        assertEquals(Integer.valueOf(1), deque.peekLast());
    }
}

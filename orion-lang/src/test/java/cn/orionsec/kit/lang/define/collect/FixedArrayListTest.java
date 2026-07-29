package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class FixedArrayListTest {

    @Test
    public void testCreate() {
        FixedArrayList<String> list = FixedArrayList.create(3);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testAddWithinCapacity() {
        FixedArrayList<Integer> list = FixedArrayList.create(3);
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(3, list.size());
        assertEquals(Integer.valueOf(1), list.get(0));
    }

    @Test
    public void testAddExceedCapacity() {
        FixedArrayList<Integer> list = FixedArrayList.create(3);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        assertEquals(3, list.size());
        // first element removed
        assertEquals(Integer.valueOf(2), list.get(0));
        assertEquals(Integer.valueOf(4), list.get(2));
    }

    @Test
    public void testAddAtIndex() {
        FixedArrayList<Integer> list = FixedArrayList.create(3);
        list.add(1);
        list.add(2);
        list.add(3);
        // adding at index when full should remove head
        list.add(0, 4);
        assertEquals(3, list.size());
    }

    @Test
    public void testAddAll() {
        FixedArrayList<Integer> list = FixedArrayList.create(3);
        list.add(1);
        list.addAll(Arrays.asList(2, 3, 4));
        assertEquals(3, list.size());
        assertEquals(Integer.valueOf(2), list.get(0));
    }

    @Test
    public void testAddAllAtIndex() {
        FixedArrayList<Integer> list = FixedArrayList.create(4);
        list.add(1);
        list.add(2);
        list.addAll(0, Arrays.asList(3, 4));
        assertEquals(4, list.size());
    }

    @Test
    public void testMultipleOverflows() {
        FixedArrayList<Integer> list = FixedArrayList.create(2);
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        assertEquals(2, list.size());
        assertEquals(Integer.valueOf(8), list.get(0));
        assertEquals(Integer.valueOf(9), list.get(1));
    }
}

package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class LimitListTest {

    @Test
    public void testCreate() {
        LimitList<Integer> list = LimitList.create(10);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testCreateWithCollection() {
        LimitList<Integer> list = LimitList.create(Arrays.asList(1, 2, 3, 4, 5), 2);
        assertEquals(5, list.size());
    }

    @Test
    public void testPage() {
        LimitList<Integer> list = LimitList.create(5);
        for (int i = 1; i <= 12; i++) {
            list.add(i);
        }
        List<Integer> page1 = list.page(1);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), page1);

        List<Integer> page2 = list.page(2);
        assertEquals(Arrays.asList(6, 7, 8, 9, 10), page2);

        List<Integer> page3 = list.page(3);
        assertEquals(Arrays.asList(11, 12), page3);
    }

    @Test
    public void testGetPages() {
        LimitList<Integer> list = LimitList.create(5);
        for (int i = 1; i <= 12; i++) {
            list.add(i);
        }
        assertEquals(3, list.getPages());
    }

    @Test
    public void testGetPagesExactDivision() {
        LimitList<Integer> list = LimitList.create(5);
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        assertEquals(2, list.getPages());
    }

    @Test
    public void testGetTotal() {
        LimitList<Integer> list = LimitList.create(3);
        list.add(1);
        list.add(2);
        assertEquals(2, list.getTotal());
    }

    @Test
    public void testGetLimit() {
        LimitList<Integer> list = LimitList.create(7);
        assertEquals(7, list.getLimit());
    }

    @Test
    public void testLimit() {
        LimitList<Integer> list = LimitList.create(5);
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        list.limit(3);
        assertEquals(3, list.getLimit());
        List<Integer> page1 = list.page(1);
        assertEquals(3, page1.size());
    }

    @Test
    public void testPageBeyondSize() {
        LimitList<Integer> list = LimitList.create(5);
        list.add(1);
        list.add(2);
        List<Integer> page2 = list.page(2);
        assertTrue(page2.isEmpty());
    }
}

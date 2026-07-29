package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class PartitionSetTest {

    @Test
    public void testCreate() {
        Set<Integer> source = new LinkedHashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        PartitionSet<Integer> partitions = PartitionSet.create(source, 2);
        assertNotNull(partitions);
    }

    @Test
    public void testSize() {
        Set<Integer> source = new LinkedHashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        PartitionSet<Integer> partitions = PartitionSet.create(source, 2);
        assertEquals(3, partitions.size());
    }

    @Test
    public void testIsEmpty() {
        Set<Integer> empty = new HashSet<>();
        PartitionSet<Integer> partitions = PartitionSet.create(empty, 3);
        assertTrue(partitions.isEmpty());
    }

    @Test
    public void testIterate() {
        Set<Integer> source = new LinkedHashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        PartitionSet<Integer> partitions = PartitionSet.create(source, 2);
        int count = 0;
        Set<Integer> all = new HashSet<>();
        for (Set<Integer> part : partitions) {
            assertNotNull(part);
            assertTrue(part.size() <= 2);
            all.addAll(part);
            count++;
        }
        assertEquals(3, count);
        assertEquals(5, all.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextBeyondEnd() {
        Set<Integer> source = new LinkedHashSet<>(Arrays.asList(1));
        PartitionSet<Integer> partitions = PartitionSet.create(source, 2);
        partitions.next(); // first
        partitions.next(); // should throw
    }
}

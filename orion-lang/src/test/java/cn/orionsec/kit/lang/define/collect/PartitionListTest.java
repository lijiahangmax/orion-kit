package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PartitionListTest {

    @Test
    public void testCreate() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        PartitionList<Integer> partitions = PartitionList.create(source, 2);
        assertNotNull(partitions);
    }

    @Test
    public void testSize() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        PartitionList<Integer> partitions = PartitionList.create(source, 2);
        assertEquals(3, partitions.size());
    }

    @Test
    public void testSizeExactDivision() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5, 6);
        PartitionList<Integer> partitions = PartitionList.create(source, 3);
        assertEquals(2, partitions.size());
    }

    @Test
    public void testGet() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        PartitionList<Integer> partitions = PartitionList.create(source, 2);
        assertEquals(Arrays.asList(1, 2), partitions.get(0));
        assertEquals(Arrays.asList(3, 4), partitions.get(1));
        assertEquals(Arrays.asList(5), partitions.get(2));
    }

    @Test
    public void testIsEmpty() {
        List<Integer> empty = Arrays.asList();
        PartitionList<Integer> partitions = PartitionList.create(empty, 3);
        assertTrue(partitions.isEmpty());
    }

    @Test
    public void testSinglePartition() {
        List<Integer> source = Arrays.asList(1, 2);
        PartitionList<Integer> partitions = PartitionList.create(source, 5);
        assertEquals(1, partitions.size());
        assertEquals(Arrays.asList(1, 2), partitions.get(0));
    }

    @Test
    public void testIterate() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        PartitionList<Integer> partitions = PartitionList.create(source, 3);
        int count = 0;
        for (List<Integer> part : partitions) {
            assertNotNull(part);
            assertTrue(part.size() <= 3);
            count++;
        }
        assertEquals(3, count);
    }
}

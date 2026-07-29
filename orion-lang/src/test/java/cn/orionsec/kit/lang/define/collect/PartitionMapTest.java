package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class PartitionMapTest {

    @Test
    public void testCreate() {
        Map<String, Integer> source = new LinkedHashMap<>();
        source.put("a", 1);
        source.put("b", 2);
        source.put("c", 3);
        PartitionMap<String, Integer> partitions = PartitionMap.create(source, 2);
        assertNotNull(partitions);
    }

    @Test
    public void testSize() {
        Map<String, Integer> source = new LinkedHashMap<>();
        source.put("a", 1);
        source.put("b", 2);
        source.put("c", 3);
        source.put("d", 4);
        source.put("e", 5);
        PartitionMap<String, Integer> partitions = PartitionMap.create(source, 2);
        assertEquals(3, partitions.size());
    }

    @Test
    public void testIsEmpty() {
        Map<String, Integer> empty = new HashMap<>();
        PartitionMap<String, Integer> partitions = PartitionMap.create(empty, 3);
        assertTrue(partitions.isEmpty());
    }

    @Test
    public void testIterate() {
        Map<String, Integer> source = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            source.put("k" + i, i);
        }
        PartitionMap<String, Integer> partitions = PartitionMap.create(source, 3);
        int count = 0;
        Map<String, Integer> all = new HashMap<>();
        for (Map<String, Integer> part : partitions) {
            assertNotNull(part);
            assertTrue(part.size() <= 3);
            all.putAll(part);
            count++;
        }
        assertEquals(3, count);
        assertEquals(7, all.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextBeyondEnd() {
        Map<String, Integer> source = new LinkedHashMap<>();
        source.put("a", 1);
        PartitionMap<String, Integer> partitions = PartitionMap.create(source, 2);
        partitions.next(); // first
        partitions.next(); // should throw
    }
}

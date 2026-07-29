package cn.orionsec.kit.lang.define.barrier;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * barrier 包单元测试
 */
public class BarrierPackageTest {

    // ==================== GenericsCollectionBarrier ====================

    @Test
    public void testCollectionBarrierCheckEmpty() {
        GenericsCollectionBarrier<String> barrier = GenericsCollectionBarrier.create("BARRIER");
        List<String> list = new ArrayList<>();
        barrier.check(list);
        assertEquals(1, list.size());
        assertEquals("BARRIER", list.get(0));
    }

    @Test
    public void testCollectionBarrierCheckNonEmpty() {
        GenericsCollectionBarrier<String> barrier = GenericsCollectionBarrier.create("BARRIER");
        List<String> list = new ArrayList<>();
        list.add("item");
        barrier.check(list);
        assertEquals(1, list.size());
        assertEquals("item", list.get(0));
    }

    @Test
    public void testCollectionBarrierCheckNull() {
        GenericsCollectionBarrier<String> barrier = GenericsCollectionBarrier.create("BARRIER");
        barrier.check(null); // should not throw
    }

    @Test
    public void testCollectionBarrierRemove() {
        GenericsCollectionBarrier<String> barrier = GenericsCollectionBarrier.create("BARRIER");
        List<String> list = new ArrayList<>();
        list.add("BARRIER");
        list.add("real");
        barrier.remove(list);
        assertEquals(1, list.size());
        assertEquals("real", list.get(0));
    }

    @Test
    public void testCollectionBarrierRemoveEmpty() {
        GenericsCollectionBarrier<String> barrier = GenericsCollectionBarrier.create("BARRIER");
        barrier.remove(null); // should not throw
        barrier.remove(new ArrayList<>()); // should not throw
    }

    // ==================== GenericsMapBarrier ====================

    @Test
    public void testMapBarrierCheckEmpty() {
        GenericsMapBarrier<String, String> barrier = GenericsMapBarrier.create("KEY", "VALUE");
        Map<String, String> map = new HashMap<>();
        barrier.check(map);
        assertEquals(1, map.size());
        assertEquals("VALUE", map.get("KEY"));
    }

    @Test
    public void testMapBarrierCheckNonEmpty() {
        GenericsMapBarrier<String, String> barrier = GenericsMapBarrier.create("KEY", "VALUE");
        Map<String, String> map = new HashMap<>();
        map.put("existing", "val");
        barrier.check(map);
        assertEquals(1, map.size());
        assertNull(map.get("KEY"));
    }

    @Test
    public void testMapBarrierRemove() {
        GenericsMapBarrier<String, String> barrier = GenericsMapBarrier.create("KEY", "VALUE");
        Map<String, String> map = new HashMap<>();
        map.put("KEY", "VALUE");
        map.put("other", "data");
        barrier.remove(map);
        assertEquals(1, map.size());
        assertEquals("data", map.get("other"));
    }

    @Test
    public void testMapBarrierCreateKeyOnly() {
        GenericsMapBarrier<String, String> barrier = GenericsMapBarrier.create("KEY");
        Map<String, String> map = new HashMap<>();
        barrier.check(map);
        assertTrue(map.containsKey("KEY"));
        assertNull(map.get("KEY"));
    }

    // ==================== GenericsAnonymousCollectionBarrier ====================

    @Test
    public void testAnonymousCollectionBarrierCheck() {
        GenericsAnonymousCollectionBarrier barrier = GenericsAnonymousCollectionBarrier.create("BARRIER");
        List<String> list = new ArrayList<>();
        barrier.check(list);
        assertEquals(1, list.size());
    }

    @Test
    public void testAnonymousCollectionBarrierRemove() {
        GenericsAnonymousCollectionBarrier barrier = GenericsAnonymousCollectionBarrier.create("BARRIER");
        List<Object> list = new ArrayList<>();
        list.add("BARRIER");
        list.add("keep");
        barrier.remove(list);
        assertEquals(1, list.size());
        assertEquals("keep", list.get(0));
    }

    @Test
    public void testAnonymousCollectionBarrierCheckNonEmpty() {
        GenericsAnonymousCollectionBarrier barrier = GenericsAnonymousCollectionBarrier.create("BARRIER");
        List<Object> list = new ArrayList<>();
        list.add("existing");
        barrier.check(list);
        assertEquals(1, list.size());
    }

    // ==================== GenericsAnonymousMapBarrier ====================

    @Test
    public void testAnonymousMapBarrierCheck() {
        GenericsAnonymousMapBarrier barrier = GenericsAnonymousMapBarrier.create("KEY", "VALUE");
        Map<String, String> map = new HashMap<>();
        barrier.check(map);
        assertEquals(1, map.size());
        assertEquals("VALUE", map.get("KEY"));
    }

    @Test
    public void testAnonymousMapBarrierRemove() {
        GenericsAnonymousMapBarrier barrier = GenericsAnonymousMapBarrier.create("KEY", "VALUE");
        Map<String, String> map = new HashMap<>();
        map.put("KEY", "VALUE");
        map.put("other", "data");
        barrier.remove(map);
        assertEquals(1, map.size());
        assertEquals("data", map.get("other"));
    }

    @Test
    public void testAnonymousMapBarrierKeyOnly() {
        GenericsAnonymousMapBarrier barrier = GenericsAnonymousMapBarrier.create("KEY");
        Map<String, String> map = new HashMap<>();
        barrier.check(map);
        assertTrue(map.containsKey("KEY"));
    }

    @Test
    public void testAnonymousMapBarrierCheckNonEmpty() {
        GenericsAnonymousMapBarrier barrier = GenericsAnonymousMapBarrier.create("KEY");
        Map<String, String> map = new HashMap<>();
        map.put("exist", "val");
        barrier.check(map);
        assertEquals(1, map.size());
        assertFalse(map.containsKey("KEY"));
    }
}

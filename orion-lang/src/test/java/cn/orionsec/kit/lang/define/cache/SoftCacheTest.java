package cn.orionsec.kit.lang.define.cache;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * SoftCache 单元测试
 */
public class SoftCacheTest {

    @Test
    public void testCreateDefault() {
        SoftCache<String, String> cache = new SoftCache<>();
        assertNotNull(cache);
    }

    @Test
    public void testCreateWithHit() {
        SoftCache<String, String> cache = new SoftCache<>(true);
        assertNotNull(cache);
    }

    @Test
    public void testCreateStaticMethod() {
        SoftCache<String, String> cache = SoftCache.create();
        assertNotNull(cache);
    }

    @Test
    public void testCreateStaticMethodWithHit() {
        SoftCache<String, String> cache = SoftCache.create(true);
        assertNotNull(cache);
    }

    @Test
    public void testCreateWithMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        SoftCache<String, String> cache = SoftCache.create(map);
        assertNotNull(cache);
        assertEquals("value1", cache.get("key1"));
        assertEquals("value2", cache.get("key2"));
    }

    @Test
    public void testCreateWithMapAndHit() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        SoftCache<String, String> cache = SoftCache.create(map, true);
        assertNotNull(cache);
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    public void testPutAndGet() {
        SoftCache<String, String> cache = new SoftCache<>();
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    public void testGetWithDefault() {
        SoftCache<String, String> cache = new SoftCache<>();
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1", "default"));
        assertEquals("default", cache.get("nonexistent", "default"));
    }

    @Test
    public void testRemove() {
        SoftCache<String, String> cache = new SoftCache<>();
        cache.put("key1", "value1");
        assertEquals("value1", cache.remove("key1"));
        assertNull(cache.get("key1"));
    }

    @Test
    public void testClear() {
        SoftCache<String, String> cache = new SoftCache<>();
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.clear();
        assertNull(cache.get("key1"));
        assertNull(cache.get("key2"));
    }

    @Test
    public void testGetNonExistent() {
        SoftCache<String, String> cache = new SoftCache<>();
        assertNull(cache.get("nonexistent"));
    }

    @Test
    public void testHitCountingDisabled() {
        SoftCache<String, String> cache = new SoftCache<>(false);
        cache.put("key1", "value1");
        cache.get("key1");
        cache.get("key1");
        assertEquals(0, cache.getCounts());
        assertEquals(0, cache.getHits());
        assertEquals(0.0, cache.getHitsRate(), 0.001);
    }

    @Test
    public void testHitCountingEnabled() {
        SoftCache<String, String> cache = new SoftCache<>(true);
        cache.put("key1", "value1");

        // 命中查询
        cache.get("key1");
        assertEquals(1, cache.getCounts());
        assertEquals(1, cache.getHits());

        // 未命中查询
        cache.get("nonexistent");
        assertEquals(2, cache.getCounts());
        assertEquals(1, cache.getHits());

        // 命中率 50%
        assertEquals(0.5, cache.getHitsRate(), 0.001);
    }

    @Test
    public void testHitRateMultipleQueries() {
        SoftCache<String, String> cache = new SoftCache<>(true);
        cache.put("a", "1");
        cache.put("b", "2");

        // 3 次命中
        cache.get("a");
        cache.get("b");
        cache.get("a");
        // 1 次未命中
        cache.get("c");

        assertEquals(4, cache.getCounts());
        assertEquals(3, cache.getHits());
        assertEquals(0.75, cache.getHitsRate(), 0.001);
    }

    @Test
    public void testIterator() {
        SoftCache<String, String> cache = new SoftCache<>();
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        int count = 0;
        for (Map.Entry<String, String> entry : cache) {
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testForEach() {
        SoftCache<String, String> cache = new SoftCache<>();
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        int[] count = {0};
        cache.forEach(entry -> {
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
            count[0]++;
        });
        assertEquals(2, count[0]);
    }

    @Test
    public void testPutOverwriteExisting() {
        SoftCache<String, String> cache = new SoftCache<>();
        cache.put("key1", "value1");
        cache.put("key1", "value2");
        assertEquals("value2", cache.get("key1"));
    }

    @Test
    public void testMultiplePutAndGet() {
        SoftCache<Integer, String> cache = new SoftCache<>();
        for (int i = 0; i < 100; i++) {
            cache.put(i, "value" + i);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals("value" + i, cache.get(i));
        }
    }
}

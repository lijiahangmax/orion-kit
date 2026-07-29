package cn.orionsec.kit.lang.define.cache;

import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * TimedCache 单元测试
 */
public class TimedCacheTest {

    private TimedCache<String> cache;

    @After
    public void tearDown() throws IOException {
        if (cache != null) {
            cache.close();
        }
    }

    @Test
    public void testPutAndGet() {
        cache = TimedCacheBuilder.create(5000, 100);
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    public void testPutIfAbsent() {
        cache = TimedCacheBuilder.create(5000, 100);
        cache.put("key1", "value1");
        cache.putIfAbsent("key1", "value2");
        assertEquals("value1", cache.get("key1"));

        cache.putIfAbsent("key2", "value2");
        assertEquals("value2", cache.get("key2"));
    }

    @Test
    public void testPutIfAbsentWithExpire() {
        cache = TimedCacheBuilder.create(5000, 100);
        cache.put("key1", "value1");
        cache.putIfAbsent("key1", "value2", 3000);
        assertEquals("value1", cache.get("key1"));

        cache.putIfAbsent("key2", "value2", 3000);
        assertEquals("value2", cache.get("key2"));
    }

    @Test
    public void testRemove() {
        cache = TimedCacheBuilder.create(5000, 100);
        cache.put("key1", "value1");
        assertEquals("value1", cache.remove("key1"));
        assertNull(cache.get("key1"));
    }

    @Test
    public void testSize() {
        cache = TimedCacheBuilder.create(5000, 100);
        assertEquals(0, cache.size());
        cache.put("key1", "value1");
        assertEquals(1, cache.size());
        cache.put("key2", "value2");
        assertEquals(2, cache.size());
    }

    @Test
    public void testIsEmpty() {
        cache = TimedCacheBuilder.create(5000, 100);
        assertTrue(cache.isEmpty());
        cache.put("key1", "value1");
        assertFalse(cache.isEmpty());
    }

    @Test
    public void testContainsKey() {
        cache = TimedCacheBuilder.create(5000, 100);
        cache.put("key1", "value1");
        assertTrue(cache.containsKey("key1"));
        assertFalse(cache.containsKey("key2"));
    }

    @Test
    public void testContainsValue() {
        cache = TimedCacheBuilder.create(5000, 100);
        cache.put("key1", "value1");
        assertTrue(cache.containsValue("value1"));
        assertFalse(cache.containsValue("value2"));
    }

    @Test
    public void testClear() {
        cache = TimedCacheBuilder.create(5000, 100);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.clear();
        assertEquals(0, cache.size());
        assertTrue(cache.isEmpty());
    }

    @Test
    public void testKeySet() {
        cache = TimedCacheBuilder.create(5000, 100);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        assertTrue(cache.keySet().contains("key1"));
        assertTrue(cache.keySet().contains("key2"));
        assertEquals(2, cache.keySet().size());
    }

    @Test
    public void testValues() {
        cache = TimedCacheBuilder.create(5000, 100);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        assertTrue(cache.values().contains("value1"));
        assertTrue(cache.values().contains("value2"));
        assertEquals(2, cache.values().size());
    }

    @Test
    public void testEntrySet() {
        cache = TimedCacheBuilder.create(5000, 100);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        assertEquals(2, cache.entrySet().size());
    }

    @Test
    public void testPutAll() {
        cache = TimedCacheBuilder.create(5000, 100);
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        cache.putAll(map);
        assertEquals("value1", cache.get("key1"));
        assertEquals("value2", cache.get("key2"));
    }

    @Test
    public void testGetOrDefault() {
        cache = TimedCacheBuilder.create(5000, 100);
        cache.put("key1", "value1");
        assertEquals("value1", cache.getOrDefault("key1", "default"));
        assertEquals("default", cache.getOrDefault("nonexistent", "default"));
    }

    @Test
    public void testGetStore() {
        cache = TimedCacheBuilder.create(5000, 100);
        cache.put("key1", "value1");
        Map<String, TimedCacheValue<String>> store = cache.getStore();
        assertNotNull(store);
        assertTrue(store.containsKey("key1"));
    }

    @Test
    public void testExpiration() throws InterruptedException {
        // 使用 100ms 过期时间和 50ms 检查间隔
        cache = TimedCacheBuilder.<String>create()
                .expireAfter(100)
                .checkInterval(50)
                .build();
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));

        // 等待过期
        Thread.sleep(250);
        assertNull(cache.get("key1"));
    }

    @Test
    public void testCustomExpireTime() throws InterruptedException {
        // 默认过期时间 5000ms，但 key1 使用 100ms
        cache = TimedCacheBuilder.<String>create()
                .expireAfter(5000)
                .checkInterval(50)
                .build();
        cache.put("key1", "value1", 100);
        cache.put("key2", "value2");

        assertEquals("value1", cache.get("key1"));
        assertEquals("value2", cache.get("key2"));

        // 等待 key1 过期
        Thread.sleep(250);
        assertNull(cache.get("key1"));
        // key2 还应该存在
        assertEquals("value2", cache.get("key2"));
    }

    @Test
    public void testExpiredListener() throws InterruptedException {
        List<String> expiredKeys = new ArrayList<>();
        AtomicInteger expiredCount = new AtomicInteger(0);

        cache = TimedCacheBuilder.<String>create()
                .expireAfter(100)
                .checkInterval(50)
                .expiredListener((key, value) -> {
                    expiredKeys.add(key);
                    expiredCount.incrementAndGet();
                })
                .build();

        cache.put("key1", "value1");
        cache.put("key2", "value2");

        // 等待过期
        Thread.sleep(300);
        assertEquals(2, expiredCount.get());
        assertTrue(expiredKeys.contains("key1"));
        assertTrue(expiredKeys.contains("key2"));
    }

    @Test
    public void testBuilderCreate() {
        cache = TimedCacheBuilder.create(5000);
        assertNotNull(cache);
        cache.put("test", "value");
        assertEquals("value", cache.get("test"));
    }

    @Test
    public void testBuilderCreateWithInterval() {
        cache = TimedCacheBuilder.create(5000, 1000);
        assertNotNull(cache);
        cache.put("test", "value");
        assertEquals("value", cache.get("test"));
    }

    @Test
    public void testClose() throws IOException {
        cache = TimedCacheBuilder.create(5000, 100);
        cache.put("key1", "value1");
        cache.close();
        // close 后 store 被清空
        assertEquals(0, cache.size());
        cache = null; // 避免 tearDown 再次 close
    }

    @Test
    public void testPutReturnsPreviousValue() {
        cache = TimedCacheBuilder.create(5000, 100);
        assertNull(cache.put("key1", "value1"));
        assertEquals("value1", cache.put("key1", "value2"));
    }

    @Test
    public void testRemoveNonExistent() {
        cache = TimedCacheBuilder.create(5000, 100);
        assertNull(cache.remove("nonexistent"));
    }
}

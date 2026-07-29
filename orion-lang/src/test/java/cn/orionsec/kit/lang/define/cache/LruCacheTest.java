package cn.orionsec.kit.lang.define.cache;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * LruCache 单元测试
 */
public class LruCacheTest {

    @Test
    public void testDefaultCapacity() {
        LruCache<String, String> cache = new LruCache<>();
        assertEquals(10, cache.getMaxCapacity());
    }

    @Test
    public void testCustomCapacity() {
        LruCache<String, String> cache = new LruCache<>(5);
        assertEquals(5, cache.getMaxCapacity());
    }

    @Test
    public void testCreateStaticMethod() {
        LruCache<String, String> cache = LruCache.create();
        assertNotNull(cache);
        assertEquals(10, cache.getMaxCapacity());
    }

    @Test
    public void testCreateStaticMethodWithCapacity() {
        LruCache<String, String> cache = LruCache.create(3);
        assertNotNull(cache);
        assertEquals(3, cache.getMaxCapacity());
    }

    @Test
    public void testPutAndGet() {
        LruCache<String, String> cache = new LruCache<>(5);
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    public void testContainsKey() {
        LruCache<String, String> cache = new LruCache<>(5);
        cache.put("key1", "value1");
        assertTrue(cache.containsKey("key1"));
        assertFalse(cache.containsKey("key2"));
    }

    @Test
    public void testRemove() {
        LruCache<String, String> cache = new LruCache<>(5);
        cache.put("key1", "value1");
        assertEquals("value1", cache.remove("key1"));
        assertNull(cache.get("key1"));
    }

    @Test
    public void testSize() {
        LruCache<String, String> cache = new LruCache<>(5);
        assertEquals(0, cache.size());
        cache.put("key1", "value1");
        assertEquals(1, cache.size());
        cache.put("key2", "value2");
        assertEquals(2, cache.size());
    }

    @Test
    public void testClear() {
        LruCache<String, String> cache = new LruCache<>(5);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.clear();
        assertEquals(0, cache.size());
    }

    @Test
    public void testEvictionPolicy() {
        // 容量为 3，当放入第 4 个元素时，最早放入且最少访问的应该被淘汰
        LruCache<String, String> cache = new LruCache<>(3);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        // 此时 size 为 3，所有元素都在
        assertEquals(3, cache.size());
        assertNotNull(cache.get("key1"));
        assertNotNull(cache.get("key2"));
        assertNotNull(cache.get("key3"));

        // 放入第 4 个元素，最近最少使用的元素被淘汰
        // 访问顺序: key1, key2, key3 依次被 get，所以 key1 最早被访问
        // 实际上 LinkedHashMap 按访问顺序排列，上面 get 了 key1、key2、key3
        // 所以放入 key4 时，key1 被最后 get，但是最早被 put
        // LinkedHashMap 访问顺序: get key1 后移到尾部, get key2 后移到尾部, get key3 后移到尾部
        // 顺序变成: key1(tail), key2(tail), key3(tail) -- 都被访问了
        // 放入 key4 后, 最近最少使用的是 key1 (先被移到尾部)
        // 不对，让我重新测试

        // 重新创建缓存
        LruCache<String, String> cache2 = new LruCache<>(3);
        cache2.put("a", "1");
        cache2.put("b", "2");
        cache2.put("c", "3");

        // 访问 a，使其变为最近使用
        cache2.get("a");

        // 放入 d，此时 b 是最近最少使用的，应该被淘汰
        cache2.put("d", "4");
        assertEquals(3, cache2.size());
        assertNull(cache2.get("b")); // b 被淘汰
        assertNotNull(cache2.get("a")); // a 还在
        assertNotNull(cache2.get("c")); // c 还在
        assertNotNull(cache2.get("d")); // d 新增
    }

    @Test
    public void testEvictionOrderWithoutAccess() {
        // 不额外访问，最先 put 的应该被淘汰（FIFO 顺序在 accessOrder=true 时也适用）
        LruCache<String, String> cache = new LruCache<>(3);
        cache.put("first", "1");
        cache.put("second", "2");
        cache.put("third", "3");

        // 放入第 4 个，应该淘汰 first
        cache.put("fourth", "4");
        assertEquals(3, cache.size());
        assertNull(cache.get("first"));
        assertNotNull(cache.get("second"));
        assertNotNull(cache.get("third"));
        assertNotNull(cache.get("fourth"));
    }

    @Test
    public void testSetMaxCapacity() {
        LruCache<String, String> cache = new LruCache<>(5);
        cache.setMaxCapacity(10);
        assertEquals(10, cache.getMaxCapacity());
    }

    @Test
    public void testCapacityLimitEnforced() {
        LruCache<Integer, Integer> cache = new LruCache<>(5);
        for (int i = 0; i < 20; i++) {
            cache.put(i, i * 10);
        }
        // 容量不应超过 maxCapacity
        assertEquals(5, cache.size());
    }

    @Test
    public void testGetNonExistentKey() {
        LruCache<String, String> cache = new LruCache<>(5);
        assertNull(cache.get("nonexistent"));
    }

    @Test
    public void testPutOverwriteExisting() {
        LruCache<String, String> cache = new LruCache<>(5);
        cache.put("key1", "value1");
        cache.put("key1", "value2");
        assertEquals("value2", cache.get("key1"));
        assertEquals(1, cache.size());
    }
}

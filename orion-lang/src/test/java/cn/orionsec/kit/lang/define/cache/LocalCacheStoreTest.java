package cn.orionsec.kit.lang.define.cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * LocalCacheStore 单元测试
 */
public class LocalCacheStoreTest {

    private LocalCacheStore store;
    private File testFile;

    @Before
    public void setUp() {
        testFile = new File(System.getProperty("java.io.tmpdir"), "test_cache_store_" + System.nanoTime() + ".map");
        store = new LocalCacheStore(testFile);
    }

    @After
    public void tearDown() {
        if (store != null) {
            store.clear();
            store.deleteFile();
        }
    }

    @Test
    public void testPutAndGet() {
        store.put("key1", "value1");
        assertEquals("value1", store.get("key1"));
    }

    @Test
    public void testGetNonExistent() {
        assertNull(store.get("nonexistent"));
    }

    @Test
    public void testRemove() {
        store.put("key1", "value1");
        store.remove("key1");
        assertNull(store.get("key1"));
    }

    @Test
    public void testSize() {
        assertEquals(0, store.size());
        store.put("key1", "value1");
        assertEquals(1, store.size());
        store.put("key2", "value2");
        assertEquals(2, store.size());
    }

    @Test
    public void testClear() {
        store.put("key1", "value1");
        store.put("key2", "value2");
        store.clear();
        assertEquals(0, store.size());
    }

    @Test
    public void testPutAll() {
        java.util.Map<String, String> map = new java.util.HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        store.putAll(map);
        assertEquals("value1", store.get("key1"));
        assertEquals("value2", store.get("key2"));
        assertEquals(2, store.size());
    }

    @Test
    public void testGetMap() {
        store.put("key1", "value1");
        java.util.Map<Object, Object> map = store.getMap();
        assertNotNull(map);
        assertEquals("value1", map.get("key1"));
    }

    @Test
    public void testDeleteFile() {
        store.put("key1", "value1");
        assertTrue(testFile.exists());
        assertTrue(store.deleteFile());
        assertFalse(testFile.exists());
        // 重新创建以供 tearDown 使用
        store = new LocalCacheStore(testFile);
    }

    @Test
    public void testPersistence() {
        store.put("key1", "value1");
        store.put("key2", "value2");

        // 重新加载同一文件
        LocalCacheStore store2 = new LocalCacheStore(testFile);
        assertEquals("value1", store2.get("key1"));
        assertEquals("value2", store2.get("key2"));
    }

    @Test
    public void testSave() {
        store.put("key1", "value1");
        store.save();
        // 应该仍然可以正常读取
        assertEquals("value1", store.get("key1"));
    }

    @Test
    public void testForceClean() {
        store.put("key1", "value1");
        store.forceClean();
        // forceClean 只清空文件，不清空内存
        // 重新加载文件，应该为空
        LocalCacheStore store2 = new LocalCacheStore(testFile);
        assertNull(store2.get("key1"));
        assertEquals(0, store2.size());
    }

    @Test
    public void testToString() {
        store.put("key1", "value1");
        String str = store.toString();
        assertNotNull(str);
        assertTrue(str.contains("key1"));
        assertTrue(str.contains("value1"));
    }

    @Test
    public void testPutOverwrite() {
        store.put("key1", "value1");
        store.put("key1", "value2");
        assertEquals("value2", store.get("key1"));
        assertEquals(1, store.size());
    }

    @Test
    public void testIntegerKeyValue() {
        store.put(1, 100);
        assertEquals(100, store.get(1));
    }

    @Test
    public void testMultipleOperations() {
        for (int i = 0; i < 10; i++) {
            store.put("key" + i, "value" + i);
        }
        assertEquals(10, store.size());

        store.remove("key5");
        assertEquals(9, store.size());
        assertNull(store.get("key5"));

        store.put("key5", "newValue5");
        assertEquals("newValue5", store.get("key5"));
    }
}

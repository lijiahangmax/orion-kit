package cn.orionsec.kit.lang.config;

import cn.orionsec.kit.lang.utils.Threads;
import org.junit.After;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * KitConfig 单元测试
 */
public class KitConfigTest {

    @After
    public void cleanup() {
        KitConfig.remove("test.key");
        KitConfig.remove("test.override");
        KitConfig.remove("test.init");
    }

    @Test
    public void testOverride() {
        KitConfig.override("test.key", "value1");
        assertEquals("value1", KitConfig.get("test.key"));
    }

    @Test
    public void testOverrideReplace() {
        KitConfig.override("test.override", "old");
        KitConfig.override("test.override", "new");
        assertEquals("new", KitConfig.get("test.override"));
    }

    @Test
    public void testInit() {
        KitConfig.init("test.init", "value1");
        assertEquals("value1", KitConfig.get("test.init"));
    }

    @Test
    public void testInitNotOverride() {
        KitConfig.init("test.init", "first");
        KitConfig.init("test.init", "second");
        // init 不会覆盖
        assertEquals("first", KitConfig.get("test.init"));
    }

    @Test
    public void testGetNull() {
        Object result = KitConfig.get("non.existent.key");
        assertNull(result);
    }

    @Test
    public void testGetOrDefault() {
        String result = KitConfig.getOrDefault("non.existent.key", "defaultVal");
        assertEquals("defaultVal", result);
    }

    @Test
    public void testGetOrDefaultWithValue() {
        KitConfig.override("test.key", "realVal");
        String result = KitConfig.getOrDefault("test.key", "defaultVal");
        assertEquals("realVal", result);
    }

    @Test
    public void testRemove() {
        KitConfig.override("test.key", "val");
        assertNotNull(KitConfig.get("test.key"));
        KitConfig.remove("test.key");
        assertNull(KitConfig.get("test.key"));
    }

    @Test
    public void testGetConfig() {
        Map<String, Object> config = KitConfig.getConfig();
        assertNotNull(config);
    }

    @Test
    public void testDifferentValueTypes() {
        KitConfig.override("test.key", 123);
        Integer val = KitConfig.get("test.key");
        assertEquals(Integer.valueOf(123), val);
    }

    @Test
    public void testConcurrentInitAndGet() throws Exception {
        int threads = 16;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            final int index = i;
            Threads.startVirtual(() -> {
                try {
                    start.await();
                    for (int j = 0; j < 200; j++) {
                        KitConfig.init("test.concurrent." + index, index);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }
        start.countDown();
        assertTrue(done.await(10, TimeUnit.SECONDS));
        for (int i = 0; i < threads; i++) {
            Integer value = KitConfig.get("test.concurrent." + i);
            assertEquals(Integer.valueOf(i), value);
            KitConfig.remove("test.concurrent." + i);
        }
    }

    @Test
    public void testConcurrentOverrideVisible() throws Exception {
        int threads = 8;
        CountDownLatch done = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            final int index = i;
            Threads.startVirtual(() -> {
                try {
                    KitConfig.override("test.concurrent.override", index);
                } finally {
                    done.countDown();
                }
            });
        }
        assertTrue(done.await(10, TimeUnit.SECONDS));
        assertNotNull(KitConfig.get("test.concurrent.override"));
        KitConfig.remove("test.concurrent.override");
    }

}

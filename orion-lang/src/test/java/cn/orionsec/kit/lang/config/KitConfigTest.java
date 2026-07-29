package cn.orionsec.kit.lang.config;

import org.junit.After;
import org.junit.Test;

import java.util.Map;

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

}

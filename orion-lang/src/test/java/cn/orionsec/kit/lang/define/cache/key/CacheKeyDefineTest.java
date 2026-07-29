package cn.orionsec.kit.lang.define.cache.key;

import cn.orionsec.kit.lang.define.cache.key.struct.RedisCacheStruct;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * CacheKeyDefine 单元测试
 */
public class CacheKeyDefineTest {

    @Before
    public void setUp() {
        // 清除全局前缀
        CacheKeyDefine.setGlobalPrefix(null);
    }

    @After
    public void tearDown() {
        // 清除全局前缀
        CacheKeyDefine.setGlobalPrefix(null);
    }

    @Test
    public void testConstructorWithKey() {
        CacheKeyDefine define = new CacheKeyDefine("user:{}");
        assertEquals("user:{}", define.getKey());
    }

    @Test
    public void testConstructorWithKeyAndPrefix() {
        CacheKeyDefine define = new CacheKeyDefine("user:{}", "app:");
        assertEquals("app:user:{}", define.getKey());
    }

    @Test
    public void testFullConstructor() {
        CacheKeyDefine define = new CacheKeyDefine(
                "user:{}",
                "app:",
                "用户缓存",
                String.class,
                RedisCacheStruct.STRING,
                3600,
                TimeUnit.SECONDS
        );
        assertEquals("app:user:{}", define.getKey());
        assertEquals("用户缓存", define.getDesc());
        assertEquals(String.class, define.getType());
        assertEquals(RedisCacheStruct.STRING, define.getStruct());
        assertEquals(3600, define.getTimeout());
        assertEquals(TimeUnit.SECONDS, define.getUnit());
    }

    @Test
    public void testGetKeyWithoutPrefix() {
        CacheKeyDefine define = new CacheKeyDefine("user:{}");
        assertEquals("user:{}", define.getKey());
    }

    @Test
    public void testGetKeyWithPrefix() {
        CacheKeyDefine define = new CacheKeyDefine("user:{}", "myapp:");
        assertEquals("myapp:user:{}", define.getKey());
    }

    @Test
    public void testGetKeyWithGlobalPrefix() {
        CacheKeyDefine.setGlobalPrefix("global:");
        CacheKeyDefine define = new CacheKeyDefine("user:{}");
        assertEquals("global:user:{}", define.getKey());
    }

    @Test
    public void testPrefixOverridesGlobalPrefix() {
        CacheKeyDefine.setGlobalPrefix("global:");
        CacheKeyDefine define = new CacheKeyDefine("user:{}", "local:");
        // prefix 不为 null 时优先使用 prefix
        assertEquals("local:user:{}", define.getKey());
    }

    @Test
    public void testFormatWithParams() {
        CacheKeyDefine define = new CacheKeyDefine("user:{}", "app:");
        String formatted = define.format(123);
        assertEquals("app:user:123", formatted);
    }

    @Test
    public void testFormatWithMultipleParams() {
        CacheKeyDefine define = new CacheKeyDefine("user:{}:role:{}", "app:");
        String formatted = define.format(123, "admin");
        assertEquals("app:user:123:role:admin", formatted);
    }

    @Test
    public void testCopy() {
        CacheKeyDefine define = new CacheKeyDefine(
                "user:{}",
                "app:",
                "用户缓存",
                String.class,
                RedisCacheStruct.HASH,
                1800,
                TimeUnit.SECONDS
        );
        CacheKeyDefine copy = define.copy();
        assertEquals(define.getKey(), copy.getKey());
        assertEquals(define.getDesc(), copy.getDesc());
        assertEquals(define.getType(), copy.getType());
        assertEquals(define.getStruct(), copy.getStruct());
        assertEquals(define.getTimeout(), copy.getTimeout());
        assertEquals(define.getUnit(), copy.getUnit());
    }

    @Test
    public void testSetTimeout() {
        CacheKeyDefine define = new CacheKeyDefine("user:{}");
        define.setTimeout(1000);
        assertEquals(1000, define.getTimeout());
    }

    @Test
    public void testSetUnit() {
        CacheKeyDefine define = new CacheKeyDefine("user:{}");
        define.setUnit(TimeUnit.HOURS);
        assertEquals(TimeUnit.HOURS, define.getUnit());
    }

    @Test
    public void testSetGlobalPrefix() {
        assertNull(CacheKeyDefine.getGlobalPrefix());
        CacheKeyDefine.setGlobalPrefix("test:");
        assertEquals("test:", CacheKeyDefine.getGlobalPrefix());
    }

    @Test
    public void testToString() {
        CacheKeyDefine define = new CacheKeyDefine(
                "user:{}",
                "app:",
                "用户缓存",
                String.class,
                RedisCacheStruct.STRING,
                3600,
                TimeUnit.SECONDS
        );
        String str = define.toString();
        assertNotNull(str);
        assertTrue(str.contains("app:user:{}"));
        assertTrue(str.contains("用户缓存"));
        assertTrue(str.contains("String"));
    }

    @Test
    public void testFormatWithMap() {
        CacheKeyDefine define = new CacheKeyDefine("user:${id}:name:${name}", "app:");
        Map<String, Object> map = new HashMap<>();
        map.put("id", 123);
        map.put("name", "test");
        String formatted = define.format(map);
        assertEquals("app:user:123:name:test", formatted);
    }
}

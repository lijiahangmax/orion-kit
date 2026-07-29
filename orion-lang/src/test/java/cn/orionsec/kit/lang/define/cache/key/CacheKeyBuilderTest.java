package cn.orionsec.kit.lang.define.cache.key;

import cn.orionsec.kit.lang.define.cache.key.struct.RedisCacheStruct;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * CacheKeyBuilder 单元测试
 */
public class CacheKeyBuilderTest {

    @Test
    public void testCreate() {
        CacheKeyBuilder builder = CacheKeyBuilder.create();
        assertNotNull(builder);
    }

    @Test
    public void testBuildWithKey() {
        CacheKeyDefine define = CacheKeyBuilder.create()
                .key("user:{}")
                .build();
        assertNotNull(define);
        assertEquals("user:{}", define.getKey());
    }

    @Test
    public void testBuildWithPrefix() {
        CacheKeyDefine define = CacheKeyBuilder.create()
                .key("user:{}")
                .prefix("app:")
                .build();
        assertEquals("app:user:{}", define.getKey());
    }

    @Test
    public void testBuildWithNoPrefix() {
        CacheKeyDefine define = CacheKeyBuilder.create()
                .key("user:{}")
                .noPrefix()
                .build();
        // noPrefix 设置空字符串前缀
        assertEquals("user:{}", define.getKey());
    }

    @Test
    public void testBuildWithDesc() {
        CacheKeyDefine define = CacheKeyBuilder.create()
                .key("user:{}")
                .desc("用户缓存")
                .build();
        assertEquals("用户缓存", define.getDesc());
    }

    @Test
    public void testBuildWithType() {
        CacheKeyDefine define = CacheKeyBuilder.create()
                .key("user:{}")
                .type(String.class)
                .build();
        assertEquals(String.class, define.getType());
    }

    @Test
    public void testBuildWithStruct() {
        CacheKeyDefine define = CacheKeyBuilder.create()
                .key("user:{}")
                .struct(RedisCacheStruct.HASH)
                .build();
        assertEquals(RedisCacheStruct.HASH, define.getStruct());
    }

    @Test
    public void testBuildWithTimeout() {
        CacheKeyDefine define = CacheKeyBuilder.create()
                .key("user:{}")
                .timeout(3600)
                .build();
        assertEquals(3600, define.getTimeout());
    }

    @Test
    public void testBuildWithTimeoutAndUnit() {
        CacheKeyDefine define = CacheKeyBuilder.create()
                .key("user:{}")
                .timeout(1, TimeUnit.HOURS)
                .build();
        assertEquals(1, define.getTimeout());
        assertEquals(TimeUnit.HOURS, define.getUnit());
    }

    @Test
    public void testBuildWithUnit() {
        CacheKeyDefine define = CacheKeyBuilder.create()
                .key("user:{}")
                .unit(TimeUnit.MINUTES)
                .build();
        assertEquals(TimeUnit.MINUTES, define.getUnit());
    }

    @Test
    public void testBuildComplete() {
        CacheKeyDefine define = CacheKeyBuilder.create()
                .key("user:{}:info")
                .prefix("myapp:")
                .desc("用户信息缓存")
                .type(String.class)
                .struct(RedisCacheStruct.HASH)
                .timeout(30, TimeUnit.MINUTES)
                .build();

        assertEquals("myapp:user:{}:info", define.getKey());
        assertEquals("用户信息缓存", define.getDesc());
        assertEquals(String.class, define.getType());
        assertEquals(RedisCacheStruct.HASH, define.getStruct());
        assertEquals(30, define.getTimeout());
        assertEquals(TimeUnit.MINUTES, define.getUnit());
    }

    @Test
    public void testDefaultValues() {
        CacheKeyDefine define = CacheKeyBuilder.create()
                .key("test")
                .build();
        // 默认值
        assertEquals(RedisCacheStruct.STRING, define.getStruct());
        assertEquals(0L, define.getTimeout());
        assertEquals(TimeUnit.MILLISECONDS, define.getUnit());
    }

    @Test
    public void testChainedCalls() {
        // 验证链式调用返回同一 builder
        CacheKeyBuilder builder = CacheKeyBuilder.create();
        CacheKeyBuilder result = builder
                .key("test")
                .prefix("p:")
                .desc("d")
                .type(Integer.class)
                .struct(RedisCacheStruct.LIST)
                .timeout(100)
                .unit(TimeUnit.SECONDS);
        assertSame(builder, result);
    }
}

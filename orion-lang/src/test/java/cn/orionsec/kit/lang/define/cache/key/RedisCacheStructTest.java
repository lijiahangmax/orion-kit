package cn.orionsec.kit.lang.define.cache.key;

import cn.orionsec.kit.lang.define.cache.key.struct.RedisCacheStruct;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * RedisCacheStruct 单元测试
 */
public class RedisCacheStructTest {

    @Test
    public void testStringStruct() {
        assertEquals("STRING", RedisCacheStruct.STRING.getStruct());
    }

    @Test
    public void testListStruct() {
        assertEquals("LIST", RedisCacheStruct.LIST.getStruct());
    }

    @Test
    public void testHashStruct() {
        assertEquals("HASH", RedisCacheStruct.HASH.getStruct());
    }

    @Test
    public void testSetStruct() {
        assertEquals("SET", RedisCacheStruct.SET.getStruct());
    }

    @Test
    public void testZSetStruct() {
        assertEquals("Z_SET", RedisCacheStruct.Z_SET.getStruct());
    }

    @Test
    public void testBitStruct() {
        assertEquals("BIT", RedisCacheStruct.BIT.getStruct());
    }

    @Test
    public void testGeoStruct() {
        assertEquals("GEO", RedisCacheStruct.GEO.getStruct());
    }

    @Test
    public void testHyperLogLogStruct() {
        assertEquals("HYPER_LOG_LOG", RedisCacheStruct.HYPER_LOG_LOG.getStruct());
    }

    @Test
    public void testValuesCount() {
        assertEquals(8, RedisCacheStruct.values().length);
    }

    @Test
    public void testValueOf() {
        assertEquals(RedisCacheStruct.STRING, RedisCacheStruct.valueOf("STRING"));
        assertEquals(RedisCacheStruct.HASH, RedisCacheStruct.valueOf("HASH"));
        assertEquals(RedisCacheStruct.LIST, RedisCacheStruct.valueOf("LIST"));
    }
}

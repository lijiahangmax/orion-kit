package cn.orionsec.kit.ext.location;

import cn.orionsec.kit.ext.location.region.block.DataBlock;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * DataBlock 单元测试
 */
public class DataBlockTest {

    @Test
    public void testConstructorWithThreeParams() {
        DataBlock block = new DataBlock(100, "中国|华东|上海|上海|电信", 2048);
        assertEquals(100, block.getCityId());
        assertEquals("中国|华东|上海|上海|电信", block.getRegion());
        assertEquals(2048, block.getDataPtr());
    }

    @Test
    public void testConstructorWithTwoParams() {
        DataBlock block = new DataBlock(200, "中国|华北|北京|北京|联通");
        assertEquals(200, block.getCityId());
        assertEquals("中国|华北|北京|北京|联通", block.getRegion());
        assertEquals(0, block.getDataPtr());
    }

    @Test
    public void testSetters() {
        DataBlock block = new DataBlock(0, "");
        block.setCityId(300);
        block.setRegion("美国|北美|加州|洛杉矶|谷歌");
        block.setDataPtr(4096);
        assertEquals(300, block.getCityId());
        assertEquals("美国|北美|加州|洛杉矶|谷歌", block.getRegion());
        assertEquals(4096, block.getDataPtr());
    }

    @Test
    public void testToString() {
        DataBlock block = new DataBlock(100, "中国|华东|上海|上海|电信", 2048);
        String str = block.toString();
        assertTrue(str.contains("100"));
        assertTrue(str.contains("中国|华东|上海|上海|电信"));
        assertTrue(str.contains("2048"));
    }

    @Test
    public void testNullRegion() {
        DataBlock block = new DataBlock(0, null, 0);
        assertNull(block.getRegion());
    }
}

package cn.orionsec.kit.ext.location;

import cn.orionsec.kit.ext.location.region.config.DbConfig;
import cn.orionsec.kit.lang.constant.Const;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * DbConfig 单元测试
 */
public class DbConfigTest {

    @Test
    public void testDefaultConstructor() {
        DbConfig config = new DbConfig();
        assertEquals(Const.BUFFER_KB_16, config.getTotalHeaderSize());
        assertEquals(Const.BUFFER_KB_8, config.getIndexBlockSize());
    }

    @Test
    public void testConstructorWithValidSize() {
        // 8 is a valid totalHeaderSize (divisible by 8)
        DbConfig config = new DbConfig(8);
        assertEquals(8, config.getTotalHeaderSize());
        assertEquals(Const.BUFFER_KB_8, config.getIndexBlockSize());
    }

    @Test
    public void testConstructorWithInvalidSize() {
        // 7 is not divisible by 8, should fallback to BUFFER_KB_16
        DbConfig config = new DbConfig(7);
        assertEquals(Const.BUFFER_KB_16, config.getTotalHeaderSize());
    }

    @Test
    public void testConstructorWithLargeValidSize() {
        DbConfig config = new DbConfig(4096);
        assertEquals(4096, config.getTotalHeaderSize());
    }

    @Test
    public void testSetTotalHeaderSize() {
        DbConfig config = new DbConfig();
        config.setTotalHeaderSize(2048);
        assertEquals(2048, config.getTotalHeaderSize());
    }

    @Test
    public void testSetIndexBlockSize() {
        DbConfig config = new DbConfig();
        config.setIndexBlockSize(4096);
        assertEquals(4096, config.getIndexBlockSize());
    }
}

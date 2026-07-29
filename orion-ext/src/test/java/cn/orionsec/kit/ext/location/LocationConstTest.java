package cn.orionsec.kit.ext.location;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * LocationConst 单元测试
 */
public class LocationConstTest {

    @Test
    public void testCz88NetConstant() {
        assertNotNull(LocationConst.CZ88_NET);
        assertEquals("CZ88.NET", LocationConst.CZ88_NET);
    }

    @Test
    public void testUnknownConstant() {
        assertNotNull(LocationConst.UNKNOWN);
        // 根据 KitExtConfiguration 配置, 默认值为 "未知"
        assertFalse(LocationConst.UNKNOWN.isEmpty());
    }
}

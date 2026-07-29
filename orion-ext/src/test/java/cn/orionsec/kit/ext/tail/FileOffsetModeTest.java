package cn.orionsec.kit.ext.tail;

import cn.orionsec.kit.ext.tail.mode.FileOffsetMode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * FileOffsetMode 枚举测试
 */
public class FileOffsetModeTest {

    @Test
    public void testEnumValues() {
        FileOffsetMode[] values = FileOffsetMode.values();
        assertEquals(2, values.length);
    }

    @Test
    public void testEnumConstants() {
        assertNotNull(FileOffsetMode.BYTE);
        assertNotNull(FileOffsetMode.LINE);
    }

    @Test
    public void testValueOf() {
        assertEquals(FileOffsetMode.BYTE, FileOffsetMode.valueOf("BYTE"));
        assertEquals(FileOffsetMode.LINE, FileOffsetMode.valueOf("LINE"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfInvalid() {
        FileOffsetMode.valueOf("INVALID");
    }

    @Test
    public void testOrdinal() {
        assertEquals(0, FileOffsetMode.BYTE.ordinal());
        assertEquals(1, FileOffsetMode.LINE.ordinal());
    }

}

package cn.orionsec.kit.ext.tail;

import cn.orionsec.kit.ext.tail.mode.FileNotFoundMode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * FileNotFoundMode 枚举测试
 */
public class FileNotFoundModeTest {

    @Test
    public void testEnumValues() {
        FileNotFoundMode[] values = FileNotFoundMode.values();
        assertEquals(5, values.length);
    }

    @Test
    public void testEnumConstants() {
        assertNotNull(FileNotFoundMode.CLOSE);
        assertNotNull(FileNotFoundMode.WAIT);
        assertNotNull(FileNotFoundMode.WAIT_TIMES);
        assertNotNull(FileNotFoundMode.WAIT_COUNT);
        assertNotNull(FileNotFoundMode.THROWS);
    }

    @Test
    public void testValueOf() {
        assertEquals(FileNotFoundMode.CLOSE, FileNotFoundMode.valueOf("CLOSE"));
        assertEquals(FileNotFoundMode.WAIT, FileNotFoundMode.valueOf("WAIT"));
        assertEquals(FileNotFoundMode.WAIT_TIMES, FileNotFoundMode.valueOf("WAIT_TIMES"));
        assertEquals(FileNotFoundMode.WAIT_COUNT, FileNotFoundMode.valueOf("WAIT_COUNT"));
        assertEquals(FileNotFoundMode.THROWS, FileNotFoundMode.valueOf("THROWS"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfInvalid() {
        FileNotFoundMode.valueOf("INVALID");
    }

    @Test
    public void testOrdinal() {
        assertEquals(0, FileNotFoundMode.CLOSE.ordinal());
        assertEquals(1, FileNotFoundMode.WAIT.ordinal());
        assertEquals(2, FileNotFoundMode.WAIT_TIMES.ordinal());
        assertEquals(3, FileNotFoundMode.WAIT_COUNT.ordinal());
        assertEquals(4, FileNotFoundMode.THROWS.ordinal());
    }

}

package cn.orionsec.kit.ext.tail;

import cn.orionsec.kit.ext.tail.mode.FileMinusMode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * FileMinusMode 枚举测试
 */
public class FileMinusModeTest {

    @Test
    public void testEnumValues() {
        FileMinusMode[] values = FileMinusMode.values();
        assertEquals(5, values.length);
    }

    @Test
    public void testEnumConstants() {
        assertNotNull(FileMinusMode.CLOSE);
        assertNotNull(FileMinusMode.CURRENT);
        assertNotNull(FileMinusMode.RESUME);
        assertNotNull(FileMinusMode.OFFSET);
        assertNotNull(FileMinusMode.THROWS);
    }

    @Test
    public void testValueOf() {
        assertEquals(FileMinusMode.CLOSE, FileMinusMode.valueOf("CLOSE"));
        assertEquals(FileMinusMode.CURRENT, FileMinusMode.valueOf("CURRENT"));
        assertEquals(FileMinusMode.RESUME, FileMinusMode.valueOf("RESUME"));
        assertEquals(FileMinusMode.OFFSET, FileMinusMode.valueOf("OFFSET"));
        assertEquals(FileMinusMode.THROWS, FileMinusMode.valueOf("THROWS"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfInvalid() {
        FileMinusMode.valueOf("INVALID");
    }

    @Test
    public void testOrdinal() {
        assertEquals(0, FileMinusMode.CLOSE.ordinal());
        assertEquals(1, FileMinusMode.CURRENT.ordinal());
        assertEquals(2, FileMinusMode.RESUME.ordinal());
        assertEquals(3, FileMinusMode.OFFSET.ordinal());
        assertEquals(4, FileMinusMode.THROWS.ordinal());
    }

}

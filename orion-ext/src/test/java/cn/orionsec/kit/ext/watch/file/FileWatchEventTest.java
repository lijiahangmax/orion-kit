package cn.orionsec.kit.ext.watch.file;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * FileWatchEvent 枚举测试
 */
public class FileWatchEventTest {

    @Test
    public void testEnumValues() {
        FileWatchEvent[] values = FileWatchEvent.values();
        assertEquals(4, values.length);
        assertEquals(FileWatchEvent.ACCESS, values[0]);
        assertEquals(FileWatchEvent.MODIFIED, values[1]);
        assertEquals(FileWatchEvent.CREATE, values[2]);
        assertEquals(FileWatchEvent.DELETE, values[3]);
    }

    @Test
    public void testValueOf() {
        assertEquals(FileWatchEvent.ACCESS, FileWatchEvent.valueOf("ACCESS"));
        assertEquals(FileWatchEvent.MODIFIED, FileWatchEvent.valueOf("MODIFIED"));
        assertEquals(FileWatchEvent.CREATE, FileWatchEvent.valueOf("CREATE"));
        assertEquals(FileWatchEvent.DELETE, FileWatchEvent.valueOf("DELETE"));
    }

    @Test
    public void testAllArray() {
        FileWatchEvent[] all = FileWatchEvent.ALL;
        assertNotNull(all);
        assertEquals(4, all.length);
        assertEquals(FileWatchEvent.ACCESS, all[0]);
        assertEquals(FileWatchEvent.MODIFIED, all[1]);
        assertEquals(FileWatchEvent.CREATE, all[2]);
        assertEquals(FileWatchEvent.DELETE, all[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfInvalid() {
        FileWatchEvent.valueOf("INVALID");
    }

}

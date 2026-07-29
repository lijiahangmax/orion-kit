package cn.orionsec.kit.ext.watch.folder;

import org.junit.Test;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * WatchEventKind 枚举测试
 */
public class WatchEventKindTest {

    @Test
    public void testEnumValues() {
        WatchEventKind[] values = WatchEventKind.values();
        assertEquals(4, values.length);
        assertEquals(WatchEventKind.OVERFLOW, values[0]);
        assertEquals(WatchEventKind.MODIFY, values[1]);
        assertEquals(WatchEventKind.CREATE, values[2]);
        assertEquals(WatchEventKind.DELETE, values[3]);
    }

    @Test
    public void testValueOf() {
        assertEquals(WatchEventKind.OVERFLOW, WatchEventKind.valueOf("OVERFLOW"));
        assertEquals(WatchEventKind.MODIFY, WatchEventKind.valueOf("MODIFY"));
        assertEquals(WatchEventKind.CREATE, WatchEventKind.valueOf("CREATE"));
        assertEquals(WatchEventKind.DELETE, WatchEventKind.valueOf("DELETE"));
    }

    @Test
    public void testGetValueOverflow() {
        WatchEvent.Kind<?> kind = WatchEventKind.OVERFLOW.getValue();
        assertNotNull(kind);
        assertEquals(StandardWatchEventKinds.OVERFLOW, kind);
    }

    @Test
    public void testGetValueModify() {
        WatchEvent.Kind<?> kind = WatchEventKind.MODIFY.getValue();
        assertNotNull(kind);
        assertEquals(StandardWatchEventKinds.ENTRY_MODIFY, kind);
    }

    @Test
    public void testGetValueCreate() {
        WatchEvent.Kind<?> kind = WatchEventKind.CREATE.getValue();
        assertNotNull(kind);
        assertEquals(StandardWatchEventKinds.ENTRY_CREATE, kind);
    }

    @Test
    public void testGetValueDelete() {
        WatchEvent.Kind<?> kind = WatchEventKind.DELETE.getValue();
        assertNotNull(kind);
        assertEquals(StandardWatchEventKinds.ENTRY_DELETE, kind);
    }

    @Test
    public void testAllArray() {
        WatchEvent.Kind<?>[] all = WatchEventKind.ALL;
        assertNotNull(all);
        assertEquals(4, all.length);
        assertEquals(StandardWatchEventKinds.OVERFLOW, all[0]);
        assertEquals(StandardWatchEventKinds.ENTRY_MODIFY, all[1]);
        assertEquals(StandardWatchEventKinds.ENTRY_CREATE, all[2]);
        assertEquals(StandardWatchEventKinds.ENTRY_DELETE, all[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfInvalid() {
        WatchEventKind.valueOf("INVALID");
    }

}

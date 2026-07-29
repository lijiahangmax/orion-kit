package cn.orionsec.kit.lang.utils.io;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * FileType 枚举测试
 */
public class FileTypeTest {

    @Test
    public void testRegularFileMatch() {
        assertTrue(FileType.REGULAR_FILE.isMatch("-rwxr-xr-x"));
    }

    @Test
    public void testDirectoryMatch() {
        assertTrue(FileType.DIRECTORY.isMatch("drwxr-xr-x"));
    }

    @Test
    public void testSymlinkMatch() {
        assertTrue(FileType.SYMLINK.isMatch("lrwxrwxrwx"));
    }

    @Test
    public void testNotMatch() {
        assertFalse(FileType.DIRECTORY.isMatch("-rwxr-xr-x"));
    }

    @Test
    public void testNullMatch() {
        assertFalse(FileType.REGULAR_FILE.isMatch(null));
    }

    @Test
    public void testOf() {
        assertEquals(FileType.DIRECTORY, FileType.of("drwxr-xr-x"));
        assertEquals(FileType.REGULAR_FILE, FileType.of("-rw-r--r--"));
        assertNull(FileType.of(null));
    }

    @Test
    public void testGetSymbol() {
        assertEquals("-", FileType.REGULAR_FILE.getSymbol());
        assertEquals("d", FileType.DIRECTORY.getSymbol());
        assertEquals("l", FileType.SYMLINK.getSymbol());
    }
}

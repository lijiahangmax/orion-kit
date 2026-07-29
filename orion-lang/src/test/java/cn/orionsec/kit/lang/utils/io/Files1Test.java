package cn.orionsec.kit.lang.utils.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Files1 工具类测试
 */
public class Files1Test {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testTouch() throws Exception {
        File file = new File(tempFolder.getRoot(), "touchtest.txt");
        assertFalse(file.exists());
        Files1.touch(file);
        assertTrue(file.exists());
    }

    @Test
    public void testMkdirs() throws Exception {
        File dir = new File(tempFolder.getRoot(), "a/b/c");
        assertFalse(dir.exists());
        Files1.mkdirs(dir);
        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());
    }

    @Test
    public void testIsFile() throws Exception {
        File file = tempFolder.newFile("test.txt");
        assertTrue(Files1.isFile(file));
        assertFalse(Files1.isFile(tempFolder.getRoot()));
    }

    @Test
    public void testIsDirectory() throws Exception {
        File dir = tempFolder.newFolder("testDir");
        assertTrue(Files1.isDirectory(dir));
        File file = tempFolder.newFile("test.txt");
        assertFalse(Files1.isDirectory(file));
    }

    @Test
    public void testGetPath() {
        String path = Files1.getPath("/a/b/c");
        assertNotNull(path);
        // should normalize separators
        assertFalse(path.contains("\\\\"));
    }

    @Test
    public void testGetFileName() {
        String name = Files1.getFileName("/path/to/file.txt");
        assertEquals("file.txt", name);
    }

    @Test
    public void testGetAttribute() throws Exception {
        File file = tempFolder.newFile("attr.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("content".getBytes());
        }
        FileAttribute attr = Files1.getAttribute(file);
        assertNotNull(attr);
        assertTrue(attr.isRegularFile());
        assertFalse(attr.isDirectory());
        assertTrue(attr.getSize() > 0);
    }

    @Test
    public void testExists() throws Exception {
        File file = tempFolder.newFile("exists.txt");
        assertTrue(Files1.exists(file));
        assertFalse(Files1.exists(new File(tempFolder.getRoot(), "noexist.txt")));
    }

    @Test
    public void testListFiles() throws Exception {
        File dir = tempFolder.newFolder("listDir");
        new File(dir, "a.txt").createNewFile();
        new File(dir, "b.txt").createNewFile();
        List<File> files = Files1.listFiles(dir, false);
        assertEquals(2, files.size());
    }
}

package cn.orionsec.kit.lang.utils.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * FileEncodes 工具类测试
 */
public class FileEncodesTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testGetEncodingUtf8WithBom() throws Exception {
        File file = tempFolder.newFile("utf8bom.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // UTF-8 BOM
            fos.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            fos.write("hello".getBytes("UTF-8"));
        }
        String encoding = FileEncodes.getEncoding(file);
        assertEquals("UTF-8", encoding);
    }

    @Test
    public void testGetEncodingUtf16LE() throws Exception {
        File file = tempFolder.newFile("utf16le.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // UTF-16LE BOM
            fos.write(new byte[]{(byte) 0xFF, (byte) 0xFE});
            fos.write("hi".getBytes("UTF-16LE"));
        }
        String encoding = FileEncodes.getEncoding(file);
        assertEquals("UTF-16LE", encoding);
    }

    @Test
    public void testGetEncodingUtf16BE() throws Exception {
        File file = tempFolder.newFile("utf16be.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // UTF-16BE BOM
            fos.write(new byte[]{(byte) 0xFE, (byte) 0xFF});
            fos.write("hi".getBytes("UTF-16BE"));
        }
        String encoding = FileEncodes.getEncoding(file);
        assertEquals("UTF-16BE", encoding);
    }

    @Test
    public void testGetEncodingDefault() throws Exception {
        File file = tempFolder.newFile("default.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("hello ascii".getBytes());
        }
        String encoding = FileEncodes.getEncoding(file);
        assertNotNull(encoding);
    }

    @Test
    public void testGetEncodingByPath() throws Exception {
        File file = tempFolder.newFile("path_test.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            fos.write("test".getBytes("UTF-8"));
        }
        String encoding = FileEncodes.getEncoding(file.getAbsolutePath());
        assertEquals("UTF-8", encoding);
    }
}

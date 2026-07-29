package cn.orionsec.kit.lang.utils.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * FileWriters 工具类测试
 */
public class FileWritersTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testWriteBytes() throws Exception {
        File file = tempFolder.newFile("write.txt");
        byte[] data = "hello write".getBytes();
        FileWriters.write(file, data);
        byte[] content = Files.readAllBytes(file.toPath());
        assertArrayEquals(data, content);
    }

    @Test
    public void testAppendBytes() throws Exception {
        File file = tempFolder.newFile("append.txt");
        FileWriters.write(file, "hello".getBytes());
        FileWriters.append(file, " world".getBytes());
        byte[] content = Files.readAllBytes(file.toPath());
        assertEquals("hello world", new String(content));
    }

    @Test
    public void testAppendString() throws Exception {
        File file = tempFolder.newFile("appendstr.txt");
        FileWriters.write(file, "first".getBytes());
        FileWriters.append(file, "_second");
        byte[] content = Files.readAllBytes(file.toPath());
        assertEquals("first_second", new String(content));
    }

    @Test
    public void testWriteString() throws Exception {
        File file = tempFolder.newFile("writestr.txt");
        FileWriters.write(file, "test content".getBytes());
        String content = new String(Files.readAllBytes(file.toPath()));
        assertEquals("test content", content);
    }

    @Test
    public void testAppendLines() throws Exception {
        File file = tempFolder.newFile("appendlines.txt");
        FileWriters.write(file, "header\n".getBytes());
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileWriters.appendLines(file, file.length(), lines, null);
        byte[] content = Files.readAllBytes(file.toPath());
        String text = new String(content);
        assertTrue(text.contains("line1"));
        assertTrue(text.contains("line2"));
        assertTrue(text.contains("line3"));
    }
}

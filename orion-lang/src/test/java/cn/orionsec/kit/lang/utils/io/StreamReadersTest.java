package cn.orionsec.kit.lang.utils.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * StreamReaders 工具类测试
 */
public class StreamReadersTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = "hello world".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        byte[] buffer = new byte[5];
        int read = StreamReaders.read(in, buffer);
        assertEquals(5, read);
        assertEquals('h', buffer[0]);
        assertEquals('e', buffer[1]);
    }

    @Test
    public void testReadWithSkip() throws IOException {
        byte[] data = "hello world".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        byte[] buffer = new byte[5];
        int read = StreamReaders.read(in, buffer, 6);
        assertEquals(5, read);
        assertEquals('w', buffer[0]);
    }

    @Test
    public void testReadAllBytes() throws IOException {
        byte[] data = "test data".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        byte[] result = StreamReaders.readAllBytes(in);
        assertArrayEquals(data, result);
    }

    @Test
    public void testReadLine() throws IOException {
        StringReader reader = new StringReader("first line\nsecond line\nthird line");
        String line = StreamReaders.readLine(reader);
        assertEquals("first line", line);
    }

    @Test
    public void testReadLineWithSkipLine() throws IOException {
        StringReader reader = new StringReader("first line\nsecond line\nthird line");
        String line = StreamReaders.readLine(reader, 1);
        assertEquals("second line", line);
    }

    @Test
    public void testReadLines() throws IOException {
        String content = "line1\nline2\nline3\nline4";
        ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
        List<String> lines = StreamReaders.readLines(in);
        assertEquals(4, lines.size());
        assertEquals("line1", lines.get(0));
        assertEquals("line4", lines.get(3));
    }

    @Test
    public void testReadLinesWithLimit() throws IOException {
        String content = "line1\nline2\nline3\nline4";
        ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
        List<String> lines = StreamReaders.readLines(in, 2);
        assertEquals(2, lines.size());
        assertEquals("line1", lines.get(0));
        assertEquals("line2", lines.get(1));
    }
}

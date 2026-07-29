package cn.orionsec.kit.lang.utils.io;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * StreamWriters 工具类测试
 */
public class StreamWritersTest {

    @Test
    public void testWriteBytesToOutputStream() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] data = "hello".getBytes();
        StreamWriters.write(out, data, 0, data.length);
        assertArrayEquals(data, out.toByteArray());
    }

    @Test
    public void testWriteStringToOutputStream() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamWriters.write(out, "world");
        assertEquals("world", out.toString());
    }

    @Test
    public void testWriteStringToOutputStreamWithCharset() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamWriters.write(out, "test", "UTF-8");
        assertEquals("test", out.toString("UTF-8"));
    }

    @Test
    public void testWriteBytesToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        byte[] data = "writer test".getBytes();
        StreamWriters.write(writer, data);
        assertEquals("writer test", writer.toString());
    }

    @Test
    public void testWriteStringToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        StreamWriters.write(writer, "hello writer");
        assertEquals("hello writer", writer.toString());
    }
}

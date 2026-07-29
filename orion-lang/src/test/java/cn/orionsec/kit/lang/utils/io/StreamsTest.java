package cn.orionsec.kit.lang.utils.io;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Streams 工具类测试
 */
public class StreamsTest {

    @Test
    public void testClose() {
        // should not throw
        Streams.close((AutoCloseable) null);
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Streams.close(in);
    }

    @Test
    public void testFlush() {
        // should not throw
        Streams.flush(null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Streams.flush(out);
    }

    @Test
    public void testTransfer() throws IOException {
        byte[] data = "hello world".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int count = Streams.transfer(in, out);
        assertEquals(data.length, count);
        assertArrayEquals(data, out.toByteArray());
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] data = "test data".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        byte[] result = Streams.toByteArray(in);
        assertArrayEquals(data, result);
    }

    @Test
    public void testToString() throws IOException {
        String text = "hello streams";
        ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes());
        String result = Streams.toString(in);
        assertEquals(text, result);
    }

    @Test
    public void testToInputStream() {
        String text = "input stream test";
        InputStream in = Streams.toInputStream(text);
        assertNotNull(in);
    }

    @Test
    public void testToOutputStream() {
        byte[] data = "output".getBytes();
        OutputStream out = Streams.toOutputStream(data);
        assertNotNull(out);
        assertTrue(out instanceof ByteArrayOutputStream);
        assertEquals(data.length, ((ByteArrayOutputStream) out).size());
    }

    @Test
    public void testLineConsumer() throws IOException {
        String content = "line1\nline2\nline3";
        ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
        List<String> lines = new ArrayList<>();
        Streams.lineConsumer(in, lines::add);
        assertEquals(3, lines.size());
        assertEquals("line1", lines.get(0));
        assertEquals("line2", lines.get(1));
        assertEquals("line3", lines.get(2));
    }

    @Test
    public void testMd5() {
        String content = "test content for md5";
        ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
        String md5 = Streams.md5(in);
        assertNotNull(md5);
        assertEquals(32, md5.length());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = new byte[100];
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        long skipped = Streams.skip(in, 50);
        assertEquals(50, skipped);
    }
}

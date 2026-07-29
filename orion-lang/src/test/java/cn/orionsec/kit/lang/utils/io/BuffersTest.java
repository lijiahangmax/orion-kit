package cn.orionsec.kit.lang.utils.io;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

/**
 * Buffers 工具类测试
 */
public class BuffersTest {

    @Test
    public void testCreateFromBytes() {
        byte[] data = {1, 2, 3, 4, 5};
        ByteBuffer buffer = Buffers.create(data);
        assertNotNull(buffer);
        assertEquals(5, buffer.remaining());
    }

    @Test
    public void testCreateFromString() {
        ByteBuffer buffer = Buffers.create("hello");
        assertNotNull(buffer);
        assertEquals(5, buffer.remaining());
    }

    @Test
    public void testReadStr() {
        ByteBuffer buffer = Buffers.create("hello world");
        String result = Buffers.readStr(buffer);
        assertEquals("hello world", result);
    }

    @Test
    public void testReadBytes() {
        byte[] data = {10, 20, 30, 40, 50};
        ByteBuffer buffer = Buffers.create(data);
        byte[] result = Buffers.readBytes(buffer);
        assertArrayEquals(data, result);
    }

    @Test
    public void testReadBytesWithMaxLength() {
        byte[] data = {10, 20, 30, 40, 50};
        ByteBuffer buffer = Buffers.create(data);
        byte[] result = Buffers.readBytes(buffer, 3);
        assertEquals(3, result.length);
        assertEquals(10, result[0]);
        assertEquals(20, result[1]);
        assertEquals(30, result[2]);
    }

    @Test
    public void testCopy() {
        ByteBuffer src = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5});
        ByteBuffer dest = ByteBuffer.allocate(5);
        Buffers.copy(src, dest);
        // copy uses System.arraycopy, dest array should have the data
        byte[] destArray = dest.array();
        assertEquals(1, destArray[0]);
        assertEquals(2, destArray[1]);
        assertEquals(3, destArray[2]);
    }

    @Test
    public void testReadLine() {
        ByteBuffer buffer = Buffers.create("line1\nline2\n");
        String line = Buffers.readLine(buffer);
        assertEquals("line1", line);
    }

    @Test
    public void testLineEnd() {
        ByteBuffer buffer = Buffers.create("hello\nworld");
        int end = Buffers.lineEnd(buffer);
        assertEquals(5, end);
    }
}

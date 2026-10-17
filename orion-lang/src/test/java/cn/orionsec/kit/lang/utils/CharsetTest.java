package cn.orionsec.kit.lang.utils;

import cn.orionsec.kit.lang.utils.io.Buffers;
import cn.orionsec.kit.lang.utils.io.Streams;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * 默认编码测试
 */
public class CharsetTest {

    private static final String TEXT = "中文编码";

    @Test
    public void testStringsBytes() {
        assertArrayEquals(TEXT.getBytes(StandardCharsets.UTF_8), Strings.bytes(TEXT));
    }

    @Test
    public void testStreams() throws IOException {
        byte[] bytes = TEXT.getBytes(StandardCharsets.UTF_8);
        assertEquals(TEXT, Streams.toString(bytes));
        assertEquals(TEXT, Streams.toString(new ByteArrayInputStream(bytes)));
    }

    @Test
    public void testBuffers() {
        ByteBuffer buffer = ByteBuffer.wrap(TEXT.getBytes(StandardCharsets.UTF_8));
        assertEquals(TEXT, Buffers.readStr(buffer));
    }

}

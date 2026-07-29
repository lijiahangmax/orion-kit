package cn.orionsec.kit.lang.utils.codec;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Base32s 单元测试
 */
public class Base32sTest {

    @Test
    public void testEncodeString() {
        String encoded = Base32s.encode("hello");
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
    }

    @Test
    public void testEncodeDecodeRoundTrip() {
        String original = "Hello, World!";
        String encoded = Base32s.encode(original);
        byte[] decoded = Base32s.decode(encoded);
        assertEquals(original, new String(decoded));
    }

    @Test
    public void testEncodeBytes() {
        byte[] data = {1, 2, 3, 4, 5};
        String encoded = Base32s.encode(data);
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
    }

    @Test
    public void testEncodeEmpty() {
        String encoded = Base32s.encode("");
        assertEquals("", encoded);
    }

    @Test
    public void testEncodeDecodeSpecialChars() {
        String original = "Test with special chars: @#$%";
        String encoded = Base32s.encode(original);
        byte[] decoded = Base32s.decode(encoded);
        assertEquals(original, new String(decoded));
    }

    @Test
    public void testEncodeDecodeChinese() {
        String original = "中文测试";
        String encoded = Base32s.encode(original);
        byte[] decoded = Base32s.decode(encoded);
        assertEquals(original, new String(decoded));
    }
}

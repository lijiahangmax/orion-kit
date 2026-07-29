package cn.orionsec.kit.lang.utils.codec;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Base64s 单元测试
 */
public class Base64sTest {

    @Test
    public void testEncodeString() {
        String encoded = Base64s.encode("hello");
        assertEquals("aGVsbG8=", encoded);
    }

    @Test
    public void testDecodeString() {
        String decoded = Base64s.decode("aGVsbG8=");
        assertEquals("hello", decoded);
    }

    @Test
    public void testEncodeDecodeRoundTrip() {
        String original = "Hello, World! 中文测试";
        String encoded = Base64s.encode(original);
        String decoded = Base64s.decode(encoded);
        assertEquals(original, decoded);
    }

    @Test
    public void testEncodeToBytes() {
        byte[] encoded = Base64s.encodeToBytes("test");
        assertNotNull(encoded);
        assertTrue(encoded.length > 0);
    }

    @Test
    public void testEncodeBytes() {
        byte[] data = {1, 2, 3, 4, 5};
        byte[] encoded = Base64s.encode(data);
        byte[] decoded = Base64s.decode(encoded);
        assertArrayEquals(data, decoded);
    }

    @Test
    public void testEncodeToString() {
        byte[] data = "test".getBytes();
        String encoded = Base64s.encodeToString(data);
        assertEquals("dGVzdA==", encoded);
    }

    @Test
    public void testDecodeToBytes() {
        byte[] decoded = Base64s.decodeToBytes("dGVzdA==");
        assertEquals("test", new String(decoded));
    }

    @Test
    public void testDecodeToString() {
        byte[] encoded = "dGVzdA==".getBytes();
        String decoded = Base64s.decodeToString(encoded);
        assertEquals("test", decoded);
    }

    @Test
    public void testUrl64EncodeDecodeRoundTrip() {
        String original = "test+url/safe=data";
        String encoded = Base64s.url64Encode(original);
        String decoded = Base64s.url64Decode(encoded);
        assertEquals(original, decoded);
    }

    @Test
    public void testUrl64EncodeToBytes() {
        byte[] encoded = Base64s.url64EncodeToBytes("test");
        assertNotNull(encoded);
    }

    @Test
    public void testUrl64DecodeToBytes() {
        String encoded = Base64s.url64Encode("test");
        byte[] decoded = Base64s.url64DecodeToBytes(encoded);
        assertEquals("test", new String(decoded));
    }

    @Test
    public void testUrl64EncodeBytes() {
        byte[] data = {1, 2, 3, 4, 5};
        byte[] encoded = Base64s.url64Encode(data);
        byte[] decoded = Base64s.url64Decode(encoded);
        assertArrayEquals(data, decoded);
    }

    @Test
    public void testMimeTypeEncode() {
        String result = Base64s.mimeTypeEncode("test", "text/plain");
        assertTrue(result.startsWith("data:text/plain;base64,"));
    }

    @Test
    public void testMimeTypeDecode() {
        String base64 = "data:text/plain;base64,dGVzdA==";
        byte[] decoded = Base64s.mimeTypeDecode(base64);
        assertEquals("test", new String(decoded));
    }

    @Test
    public void testGetMimeType() {
        String base64 = "data:image/png;base64,abc";
        String type = Base64s.getMimeType(base64);
        assertEquals("data:image/png", type);
    }

    @Test
    public void testImgEncode() {
        byte[] data = {1, 2, 3};
        String result = Base64s.imgEncode(data);
        assertTrue(result.startsWith("data:image/png;base64,"));
    }

    @Test
    public void testImgEncodeWithType() {
        byte[] data = {1, 2, 3};
        String result = Base64s.imgEncode(data, "jpeg");
        assertTrue(result.startsWith("data:image/jpeg;base64,"));
    }
}

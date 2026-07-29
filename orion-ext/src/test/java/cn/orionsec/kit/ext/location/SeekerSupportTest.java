package cn.orionsec.kit.ext.location;

import cn.orionsec.kit.ext.location.ext.core.SeekerSupport;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * SeekerSupport 单元测试
 */
public class SeekerSupportTest {

    @Test
    public void testGetIpByteArrayFromString() {
        byte[] result = SeekerSupport.getIpByteArrayFromString("192.168.1.1");
        assertEquals(4, result.length);
        assertEquals((byte) 192, result[0]);
        assertEquals((byte) 168, result[1]);
        assertEquals((byte) 1, result[2]);
        assertEquals((byte) 1, result[3]);
    }

    @Test
    public void testGetIpByteArrayFromStringZero() {
        byte[] result = SeekerSupport.getIpByteArrayFromString("0.0.0.0");
        assertEquals(4, result.length);
        assertEquals((byte) 0, result[0]);
        assertEquals((byte) 0, result[1]);
        assertEquals((byte) 0, result[2]);
        assertEquals((byte) 0, result[3]);
    }

    @Test
    public void testGetIpByteArrayFromStringMax() {
        byte[] result = SeekerSupport.getIpByteArrayFromString("255.255.255.255");
        assertEquals(4, result.length);
        assertEquals((byte) 255, result[0]);
        assertEquals((byte) 255, result[1]);
        assertEquals((byte) 255, result[2]);
        assertEquals((byte) 255, result[3]);
    }

    @Test
    public void testGetIpStringFromBytes() {
        byte[] ip = new byte[]{(byte) 192, (byte) 168, (byte) 1, (byte) 1};
        String result = SeekerSupport.getIpStringFromBytes(ip);
        assertEquals("192.168.1.1", result);
    }

    @Test
    public void testGetIpStringFromBytesZero() {
        byte[] ip = new byte[]{0, 0, 0, 0};
        String result = SeekerSupport.getIpStringFromBytes(ip);
        assertEquals("0.0.0.0", result);
    }

    @Test
    public void testGetIpStringFromBytesMax() {
        byte[] ip = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255};
        String result = SeekerSupport.getIpStringFromBytes(ip);
        assertEquals("255.255.255.255", result);
    }

    @Test
    public void testRoundTrip() {
        String originalIp = "10.20.30.40";
        byte[] bytes = SeekerSupport.getIpByteArrayFromString(originalIp);
        String result = SeekerSupport.getIpStringFromBytes(bytes);
        assertEquals(originalIp, result);
    }

    @Test
    public void testGetStringWithEncoding() {
        byte[] b = "hello".getBytes();
        String result = SeekerSupport.getString(b, "UTF-8");
        assertEquals("hello", result);
    }

    @Test
    public void testGetStringWithInvalidEncoding() {
        byte[] b = "hello".getBytes();
        // invalid encoding should return default string
        String result = SeekerSupport.getString(b, "INVALID_ENCODING");
        assertEquals("hello", result);
    }

    @Test
    public void testGetStringWithOffsetAndLength() {
        byte[] b = "hello world".getBytes();
        String result = SeekerSupport.getString(b, 6, 5, "UTF-8");
        assertEquals("world", result);
    }

    @Test
    public void testGetStringWithOffsetAndLengthInvalidEncoding() {
        byte[] b = "hello world".getBytes();
        String result = SeekerSupport.getString(b, 0, 5, "INVALID_ENCODING");
        assertEquals("hello", result);
    }

    @Test
    public void testGetStringEncodingConversion() {
        String original = "test";
        String result = SeekerSupport.getString(original, "UTF-8", "UTF-8");
        assertEquals(original, result);
    }

    @Test
    public void testGetStringEncodingConversionInvalid() {
        String original = "test";
        // with invalid encoding, should return original
        String result = SeekerSupport.getString(original, "UTF-8", "INVALID");
        assertEquals(original, result);
    }
}

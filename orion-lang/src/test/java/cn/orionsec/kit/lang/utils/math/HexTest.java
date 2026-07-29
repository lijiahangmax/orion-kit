package cn.orionsec.kit.lang.utils.math;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Hex 工具类测试
 */
public class HexTest {

    @Test
    public void testByteToHex() {
        assertEquals("00", Hex.byteToHex((byte) 0));
        assertEquals("0a", Hex.byteToHex((byte) 10));
        assertEquals("ff", Hex.byteToHex((byte) -1));
        assertEquals("7f", Hex.byteToHex((byte) 127));
    }

    @Test
    public void testBytesToHex() {
        byte[] bytes = {0x01, 0x0A, (byte) 0xFF};
        assertEquals("010aff", Hex.bytesToHex(bytes));
    }

    @Test
    public void testHexToByte() {
        assertEquals((byte) 0, Hex.hexToByte("00"));
        assertEquals((byte) 10, Hex.hexToByte("0a"));
        assertEquals((byte) -1, Hex.hexToByte("ff"));
    }

    @Test
    public void testHexToBytes() {
        byte[] result = Hex.hexToBytes("010aff");
        assertEquals(3, result.length);
        assertEquals((byte) 0x01, result[0]);
        assertEquals((byte) 0x0A, result[1]);
        assertEquals((byte) 0xFF, result[2]);
    }

    @Test
    public void testHexToBytesOddLength() {
        // Odd-length hex string should be padded with 0
        byte[] result = Hex.hexToBytes("fff");
        assertEquals(2, result.length);
        assertEquals((byte) 0x0F, result[0]);
        assertEquals((byte) 0xFF, result[1]);
    }

    @Test
    public void testEncode() {
        String encoded = Hex.encode("ABC");
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
    }

    @Test
    public void testDecode() {
        String encoded = Hex.encode("Hello");
        String decoded = Hex.decode(encoded);
        assertEquals("Hello", decoded);
    }

    @Test
    public void testRoundTrip() {
        String original = "Test String 123!";
        String encoded = Hex.encode(original);
        String decoded = Hex.decode(encoded);
        assertEquals(original, decoded);
    }

    @Test
    public void testBytesRoundTrip() {
        byte[] original = {0x00, 0x7F, (byte) 0x80, (byte) 0xFF};
        String hex = Hex.bytesToHex(original);
        byte[] result = Hex.hexToBytes(hex);
        assertArrayEquals(original, result);
    }
}

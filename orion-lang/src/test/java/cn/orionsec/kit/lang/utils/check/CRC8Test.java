package cn.orionsec.kit.lang.utils.check;

import org.junit.Test;

import static org.junit.Assert.*;

public class CRC8Test {

    @Test
    public void testDefaultConstructor() {
        CRC8 crc = new CRC8(0x07);
        assertEquals(0, crc.getValue());
    }

    @Test
    public void testConstructorWithInit() {
        CRC8 crc = new CRC8(0x07, (short) 0xFF);
        assertEquals(0xFF, crc.getValue());
    }

    @Test
    public void testUpdateSingleByte() {
        CRC8 crc = new CRC8(0x07);
        crc.update(0x01);
        long value = crc.getValue();
        assertTrue(value >= 0 && value <= 255);
    }

    @Test
    public void testUpdateByteArray() {
        CRC8 crc = new CRC8(0x07);
        byte[] data = {0x01, 0x02, 0x03, 0x04};
        crc.update(data);
        long value = crc.getValue();
        assertTrue(value >= 0 && value <= 255);
    }

    @Test
    public void testUpdateByteArrayWithOffset() {
        CRC8 crc = new CRC8(0x07);
        byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05};
        crc.update(data, 1, 3);
        long value1 = crc.getValue();

        CRC8 crc2 = new CRC8(0x07);
        byte[] data2 = {0x02, 0x03, 0x04};
        crc2.update(data2);
        long value2 = crc2.getValue();

        assertEquals(value1, value2);
    }

    @Test
    public void testReset() {
        CRC8 crc = new CRC8(0x07);
        byte[] data = {0x01, 0x02, 0x03};
        crc.update(data);
        crc.reset();
        assertEquals(0, crc.getValue());
    }

    @Test
    public void testResetWithInit() {
        CRC8 crc = new CRC8(0x07, (short) 0x55);
        byte[] data = {0x01, 0x02, 0x03};
        crc.update(data);
        crc.reset();
        assertEquals(0x55, crc.getValue());
    }

    @Test
    public void testConsistency() {
        CRC8 crc1 = new CRC8(0x07);
        CRC8 crc2 = new CRC8(0x07);
        byte[] data = "Test".getBytes();
        crc1.update(data);
        crc2.update(data);
        assertEquals(crc1.getValue(), crc2.getValue());
    }

    @Test
    public void testDifferentDataDifferentCrc() {
        CRC8 crc1 = new CRC8(0x07);
        CRC8 crc2 = new CRC8(0x07);
        crc1.update("ABC".getBytes());
        crc2.update("XYZ".getBytes());
        assertNotEquals(crc1.getValue(), crc2.getValue());
    }

    @Test
    public void testValueInRange() {
        CRC8 crc = new CRC8(0x31);
        crc.update("Hello World".getBytes());
        long value = crc.getValue();
        assertTrue(value >= 0 && value <= 255);
    }
}

package cn.orionsec.kit.lang.utils.check;

import org.junit.Test;

import static org.junit.Assert.*;

public class CRC16Test {

    @Test
    public void testDefaultConstructor() {
        CRC16 crc = new CRC16();
        assertEquals(0, crc.getValue());
    }

    @Test
    public void testConstructorWithInit() {
        CRC16 crc = new CRC16(0xFFFF);
        // value starts at 0, init is used only for reset
        assertEquals(0, crc.getValue());
        byte[] data = {0x01, 0x02};
        crc.update(data);
        crc.reset();
        // after reset, value should be init value
        assertEquals(0xFFFF, crc.getValue());
    }

    @Test
    public void testUpdateSingleByte() {
        CRC16 crc = new CRC16();
        crc.update(0x01);
        long value = crc.getValue();
        assertTrue(value != 0);
    }

    @Test
    public void testUpdateByteArray() {
        CRC16 crc = new CRC16();
        byte[] data = {0x01, 0x02, 0x03, 0x04};
        crc.update(data);
        long value = crc.getValue();
        assertTrue(value != 0);
    }

    @Test
    public void testUpdateByteArrayOffsetLen() {
        CRC16 crc = new CRC16();
        byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05};
        crc.update(data, 1, 3);
        long value1 = crc.getValue();

        CRC16 crc2 = new CRC16();
        byte[] data2 = {0x02, 0x03, 0x04};
        crc2.update(data2);
        long value2 = crc2.getValue();

        assertEquals(value1, value2);
    }

    @Test
    public void testReset() {
        CRC16 crc = new CRC16();
        byte[] data = {0x01, 0x02, 0x03};
        crc.update(data);
        assertTrue(crc.getValue() != 0);
        crc.reset();
        assertEquals(0, crc.getValue());
    }

    @Test
    public void testResetWithInit() {
        CRC16 crc = new CRC16(0x1234);
        byte[] data = {0x01, 0x02, 0x03};
        crc.update(data);
        crc.reset();
        assertEquals(0x1234, crc.getValue());
    }

    @Test
    public void testConsistency() {
        CRC16 crc1 = new CRC16();
        CRC16 crc2 = new CRC16();
        byte[] data = "Hello World".getBytes();
        crc1.update(data);
        crc2.update(data);
        assertEquals(crc1.getValue(), crc2.getValue());
    }

    @Test
    public void testDifferentDataDifferentCrc() {
        CRC16 crc1 = new CRC16();
        CRC16 crc2 = new CRC16();
        crc1.update("Hello".getBytes());
        crc2.update("World".getBytes());
        assertNotEquals(crc1.getValue(), crc2.getValue());
    }
}

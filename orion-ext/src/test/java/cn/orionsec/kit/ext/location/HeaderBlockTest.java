package cn.orionsec.kit.ext.location;

import cn.orionsec.kit.ext.location.region.block.HeaderBlock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * HeaderBlock 单元测试
 */
public class HeaderBlockTest {

    @Test
    public void testConstructor() {
        HeaderBlock block = new HeaderBlock(3232235520L, 1024);
        assertEquals(3232235520L, block.getIndexStartIp());
        assertEquals(1024, block.getIndexPtr());
    }

    @Test
    public void testSetters() {
        HeaderBlock block = new HeaderBlock(0, 0);
        block.setIndexStartIp(167772160L);
        block.setIndexPtr(2048);
        assertEquals(167772160L, block.getIndexStartIp());
        assertEquals(2048, block.getIndexPtr());
    }

    @Test
    public void testGetBytes() {
        HeaderBlock block = new HeaderBlock(167772160L, 512);
        byte[] bytes = block.getBytes();
        assertNotNull(bytes);
        assertEquals(8, bytes.length);
    }

    @Test
    public void testGetBytesConsistency() {
        long startIp = 3232235520L; // 192.168.0.0
        int ptr = 4096;
        HeaderBlock block = new HeaderBlock(startIp, ptr);
        byte[] bytes = block.getBytes();
        // verify little-endian encoding of startIp (first 4 bytes)
        long reconstructedIp = (bytes[0] & 0xFFL)
                | ((bytes[1] & 0xFFL) << 8)
                | ((bytes[2] & 0xFFL) << 16)
                | ((bytes[3] & 0xFFL) << 24);
        assertEquals(startIp, reconstructedIp);
        // verify little-endian encoding of ptr (next 4 bytes)
        long reconstructedPtr = (bytes[4] & 0xFFL)
                | ((bytes[5] & 0xFFL) << 8)
                | ((bytes[6] & 0xFFL) << 16)
                | ((bytes[7] & 0xFFL) << 24);
        assertEquals(ptr, reconstructedPtr);
    }

    @Test
    public void testZeroValues() {
        HeaderBlock block = new HeaderBlock(0L, 0);
        assertEquals(0L, block.getIndexStartIp());
        assertEquals(0, block.getIndexPtr());
        byte[] bytes = block.getBytes();
        for (byte b : bytes) {
            assertEquals(0, b);
        }
    }
}

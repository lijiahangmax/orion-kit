package cn.orionsec.kit.ext.location;

import cn.orionsec.kit.ext.location.region.block.IndexBlock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * IndexBlock 单元测试
 */
public class IndexBlockTest {

    @Test
    public void testConstructor() {
        IndexBlock block = new IndexBlock(167772160L, 167772415L, 1024, 32);
        assertEquals(167772160L, block.getStartIp());
        assertEquals(167772415L, block.getEndIp());
        assertEquals(1024, block.getDataPtr());
        assertEquals(32, block.getDataLen());
    }

    @Test
    public void testSetters() {
        IndexBlock block = new IndexBlock(0, 0, 0, 0);
        block.setStartIp(3232235520L);
        block.setEndIp(3232235775L);
        block.setDataPtr(2048);
        block.setDataLen(64);
        assertEquals(3232235520L, block.getStartIp());
        assertEquals(3232235775L, block.getEndIp());
        assertEquals(2048, block.getDataPtr());
        assertEquals(64, block.getDataLen());
    }

    @Test
    public void testGetIndexBlockLength() {
        assertEquals(12, IndexBlock.getIndexBlockLength());
    }

    @Test
    public void testGetBytes() {
        IndexBlock block = new IndexBlock(167772160L, 167772415L, 1024, 32);
        byte[] bytes = block.getBytes();
        assertNotNull(bytes);
        assertEquals(12, bytes.length);
    }

    @Test
    public void testGetBytesStartIp() {
        long startIp = 167772160L; // 10.0.0.0
        long endIp = 167772415L;   // 10.0.0.255
        int dataPtr = 1024;
        int dataLen = 32;
        IndexBlock block = new IndexBlock(startIp, endIp, dataPtr, dataLen);
        byte[] bytes = block.getBytes();
        // verify start ip (first 4 bytes, little-endian)
        long reconstructedStartIp = (bytes[0] & 0xFFL)
                | ((bytes[1] & 0xFFL) << 8)
                | ((bytes[2] & 0xFFL) << 16)
                | ((bytes[3] & 0xFFL) << 24);
        assertEquals(startIp, reconstructedStartIp);
        // verify end ip (next 4 bytes, little-endian)
        long reconstructedEndIp = (bytes[4] & 0xFFL)
                | ((bytes[5] & 0xFFL) << 8)
                | ((bytes[6] & 0xFFL) << 16)
                | ((bytes[7] & 0xFFL) << 24);
        assertEquals(endIp, reconstructedEndIp);
    }

    @Test
    public void testGetBytesDataPtrAndLen() {
        int dataPtr = 1024;
        int dataLen = 32;
        IndexBlock block = new IndexBlock(0L, 0L, dataPtr, dataLen);
        byte[] bytes = block.getBytes();
        // The mix = dataPtr | ((dataLen << 24) & 0xFF000000L)
        long mix = dataPtr | ((((long) dataLen) << 24) & 0xFF000000L);
        long reconstructedMix = (bytes[8] & 0xFFL)
                | ((bytes[9] & 0xFFL) << 8)
                | ((bytes[10] & 0xFFL) << 16)
                | ((bytes[11] & 0xFFL) << 24);
        assertEquals(mix, reconstructedMix);
    }
}

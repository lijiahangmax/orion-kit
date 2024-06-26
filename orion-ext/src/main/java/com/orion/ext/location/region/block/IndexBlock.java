package com.orion.ext.location.region.block;

import com.orion.ext.location.region.core.RegionSupport;

/**
 * @author Jiahang Li
 */
public class IndexBlock {

    private static final int LENGTH = 12;

    /**
     * start ip address
     */
    private long startIp;

    /**
     * end ip address
     */
    private long endIp;

    /**
     * data ptr and data length
     */
    private int dataPtr;

    /**
     * data length
     */
    private int dataLen;

    public IndexBlock(long startIp, long endIp, int dataPtr, int dataLen) {
        this.startIp = startIp;
        this.endIp = endIp;
        this.dataPtr = dataPtr;
        this.dataLen = dataLen;
    }

    public void setStartIp(long startIp) {
        this.startIp = startIp;
    }

    public void setEndIp(long endIp) {
        this.endIp = endIp;
    }

    public void setDataPtr(int dataPtr) {
        this.dataPtr = dataPtr;
    }

    public void setDataLen(int dataLen) {
        this.dataLen = dataLen;
    }

    public long getStartIp() {
        return startIp;
    }

    public long getEndIp() {
        return endIp;
    }

    public int getDataPtr() {
        return dataPtr;
    }

    public int getDataLen() {
        return dataLen;
    }

    public static int getIndexBlockLength() {
        return LENGTH;
    }

    /**
     * get the bytes for storage
     *
     * @return byte[]
     */
    public byte[] getBytes() {
        /*
         * +------------+-----------+-----------+
         * | 4bytes     | 4bytes    | 4bytes    |
         * +------------+-----------+-----------+
         *  start ip      end ip      data ptr + len
         */
        byte[] b = new byte[12];
        // start ip
        RegionSupport.writeIntLong(b, 0, startIp);
        // end ip
        RegionSupport.writeIntLong(b, 4, endIp);
        // write the data ptr and the length
        long mix = dataPtr | ((dataLen << 24) & 0xFF000000L);
        RegionSupport.writeIntLong(b, 8, mix);
        return b;
    }

}

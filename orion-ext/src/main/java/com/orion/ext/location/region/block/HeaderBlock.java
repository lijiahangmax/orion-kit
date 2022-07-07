package com.orion.ext.location.region.block;

import com.orion.ext.location.region.core.RegionSupport;

/**
 * @author li
 */
public class HeaderBlock {

    /**
     * index block start ip address
     */
    private long indexStartIp;

    /**
     * ip address
     */
    private int indexPtr;

    public HeaderBlock(long indexStartIp, int indexPtr) {
        this.indexStartIp = indexStartIp;
        this.indexPtr = indexPtr;
    }

    public void setIndexStartIp(long indexStartIp) {
        this.indexStartIp = indexStartIp;
    }

    public void setIndexPtr(int indexPtr) {
        this.indexPtr = indexPtr;
    }

    public long getIndexStartIp() {
        return indexStartIp;
    }

    public int getIndexPtr() {
        return indexPtr;
    }

    /**
     * get the bytes for db storage
     *
     * @return byte[]
     */
    public byte[] getBytes() {
        /*
         * +------------+-----------+
         * | 4bytes     | 4bytes    |
         * +------------+-----------+
         *  start ip      index ptr
         */
        byte[] b = new byte[8];
        RegionSupport.writeIntLong(b, 0, indexStartIp);
        RegionSupport.writeIntLong(b, 4, indexPtr);
        return b;
    }
}

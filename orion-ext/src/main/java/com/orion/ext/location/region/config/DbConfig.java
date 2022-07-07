package com.orion.ext.location.region.config;

import com.orion.lang.constant.Const;

import java.io.Serializable;

/**
 * @author li
 */
public class DbConfig implements Serializable {

    /**
     * total header data block size
     */
    private int totalHeaderSize;

    /**
     * max index data block size
     * u should always choice the fastest read block size
     */
    private int indexBlockSize;

    public DbConfig(int totalHeaderSize) {
        if ((totalHeaderSize % 8) != 0) {
            totalHeaderSize = Const.BUFFER_KB_16;
        }
        this.totalHeaderSize = totalHeaderSize;
        this.indexBlockSize = Const.BUFFER_KB_8;
    }

    public DbConfig() {
        this(Const.BUFFER_KB_16);
    }

    public void setTotalHeaderSize(int totalHeaderSize) {
        this.totalHeaderSize = totalHeaderSize;
    }

    public void setIndexBlockSize(int dataBlockSize) {
        this.indexBlockSize = dataBlockSize;
    }

    public int getTotalHeaderSize() {
        return totalHeaderSize;
    }

    public int getIndexBlockSize() {
        return indexBlockSize;
    }

}

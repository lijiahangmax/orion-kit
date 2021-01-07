package com.orion.location.region.config;

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
            totalHeaderSize = 8 * 2048;
        }
        this.totalHeaderSize = totalHeaderSize;
        // 4 * 2048
        this.indexBlockSize = 8192;
    }

    public DbConfig() {
        this(8 * 2048);
    }

    public int getTotalHeaderSize() {
        return totalHeaderSize;
    }

    public DbConfig setTotalHeaderSize(int totalHeaderSize) {
        this.totalHeaderSize = totalHeaderSize;
        return this;
    }

    public int getIndexBlockSize() {
        return indexBlockSize;
    }

    public DbConfig setIndexBlockSize(int dataBlockSize) {
        this.indexBlockSize = dataBlockSize;
        return this;
    }

}

package com.orion.ext.location.region.core;

import com.orion.ext.location.region.block.DataBlock;
import com.orion.ext.location.region.block.IndexBlock;
import com.orion.ext.location.region.config.DbConfig;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.io.Files1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * @author li
 */
public class DbSearcher {

    /**
     * B+树
     */
    public static final int BTREE_ALGORITHM = 1;

    /**
     * 二进制
     */
    public static final int BINARY_ALGORITHM = 2;

    /**
     * 内存
     */
    public static final int MEMORY_ALGORITHM = 3;

    /**
     * db配置项
     */
    private final DbConfig dbConfig;

    /**
     * db随机读写
     */
    private final RandomAccessFile raf;

    /**
     * header blocks buffer
     */
    private long[] headerSip = null;
    private int[] headerPtr = null;
    private int headerLength;

    /**
     * super blocks info
     */
    private long firstIndexPtr = 0;
    private long lastIndexPtr = 0;
    private int totalIndexBlocks = 0;

    /**
     * for memory mode
     * the original db binary string
     */
    private byte[] dbBinStr = null;

    public DbSearcher(DbConfig dbConfig, String dbFile) {
        this.dbConfig = dbConfig;
        raf = Files1.openRandomAccessSafe(dbFile, Const.ACCESS_R);
    }

    public DbSearcher(DbConfig dbConfig, File dbFile) {
        this.dbConfig = dbConfig;
        raf = Files1.openRandomAccessSafe(dbFile, Const.ACCESS_R);
    }

    public DbSearcher(DbConfig dbConfig, RandomAccessFile raf) {
        this.dbConfig = dbConfig;
        this.raf = raf;
    }

    /**
     * 使用内存二进制算法获取IP区域
     *
     * @param ip ip
     * @return DataBlock ignore
     * @throws IOException ignore
     */
    public DataBlock memorySearch(long ip) throws IOException {
        int blen = IndexBlock.getIndexBlockLength();
        if (dbBinStr == null) {
            dbBinStr = new byte[(int) raf.length()];
            raf.seek(0L);
            raf.readFully(dbBinStr, 0, dbBinStr.length);
            // 初始化
            firstIndexPtr = RegionSupport.getIntLong(dbBinStr, 0);
            lastIndexPtr = RegionSupport.getIntLong(dbBinStr, 4);
            totalIndexBlocks = (int) ((lastIndexPtr - firstIndexPtr) / blen) + 1;
        }
        // 搜索索引块以定义数据
        int l = 0, h = totalIndexBlocks;
        long sip, eip, dataptr = 0;
        while (l <= h) {
            int m = (l + h) >> 1;
            int p = (int) (firstIndexPtr + m * blen);
            sip = RegionSupport.getIntLong(dbBinStr, p);
            if (ip < sip) {
                h = m - 1;
            } else {
                eip = RegionSupport.getIntLong(dbBinStr, p + 4);
                if (ip > eip) {
                    l = m + 1;
                } else {
                    dataptr = RegionSupport.getIntLong(dbBinStr, p + 8);
                    break;
                }
            }
        }
        // 没查询到
        if (dataptr == 0) {
            return null;
        }
        // 地理信息
        int dataLen = (int) ((dataptr >> 24) & 0xFF);
        int dataPtr = (int) ((dataptr & 0x00FFFFFF));
        return new DataBlock((int) RegionSupport.getIntLong(dbBinStr, dataPtr), new String(dbBinStr, dataPtr + 4, dataLen - 4, StandardCharsets.UTF_8), dataPtr);
    }

    /**
     * 使用内存二进制算法获取IP区域
     *
     * @param ip ip
     * @return DataBlock ignore
     * @throws IOException ignore
     */
    public DataBlock memorySearch(String ip) throws IOException {
        return memorySearch(RegionSupport.ip2long(ip));
    }

    /**
     * 通过索引获取 ptr
     *
     * @param ptr ptr
     * @throws IOException ignore
     */
    public DataBlock getByIndexPtr(long ptr) throws IOException {
        raf.seek(ptr);
        byte[] buffer = new byte[12];
        raf.readFully(buffer, 0, buffer.length);
        // long startIp = Util.getIntLong(buffer, 0);
        // long endIp = Util.getIntLong(buffer, 4);
        long extra = RegionSupport.getIntLong(buffer, 8);
        int dataLen = (int) ((extra >> 24) & 0xFF);
        int dataPtr = (int) ((extra & 0x00FFFFFF));
        raf.seek(dataPtr);
        byte[] data = new byte[dataLen];
        raf.readFully(data, 0, data.length);
        int cityId = (int) RegionSupport.getIntLong(data, 0);
        return new DataBlock(cityId, new String(data, 4, data.length - 4, StandardCharsets.UTF_8), dataPtr);
    }

    /**
     * 使用B+TREE算法获取IP区域
     *
     * @param ip ip
     * @return DataBlock ignore
     * @throws IOException ignore
     */
    public DataBlock btreeSearch(long ip) throws IOException {
        // check and load the header
        if (headerSip == null) {
            // pass the super block
            raf.seek(8L);
            // byte[] b = new byte[dbConfig.getTotalHeaderSize()];
            byte[] b = new byte[4096];
            raf.readFully(b, 0, b.length);
            // fill the header
            // b.lenght / 8
            int len = b.length >> 3, idx = 0;
            headerSip = new long[len];
            headerPtr = new int[len];
            long startIp, dataPtr;
            for (int i = 0; i < b.length; i += 8) {
                startIp = RegionSupport.getIntLong(b, i);
                dataPtr = RegionSupport.getIntLong(b, i + 4);
                if (dataPtr == 0) {
                    break;
                }
                headerSip[idx] = startIp;
                headerPtr[idx] = (int) dataPtr;
                idx++;
            }
            headerLength = idx;
        }
        // 1. define the index block with the binary search
        if (ip == headerSip[0]) {
            return getByIndexPtr(headerPtr[0]);
        } else if (ip == headerSip[headerLength - 1]) {
            return getByIndexPtr(headerPtr[headerLength - 1]);
        }
        int l = 0, h = headerLength, sptr = 0, eptr = 0;
        while (l <= h) {
            int m = (l + h) >> 1;
            // perfetc matched, just return it
            if (ip == headerSip[m]) {
                if (m > 0) {
                    sptr = headerPtr[m - 1];
                    eptr = headerPtr[m];
                } else {
                    sptr = headerPtr[m];
                    eptr = headerPtr[m + 1];
                }
                break;
            }
            // less then the middle value
            if (ip < headerSip[m]) {
                if (m == 0) {
                    sptr = headerPtr[m];
                    eptr = headerPtr[m + 1];
                    break;
                } else if (ip > headerSip[m - 1]) {
                    sptr = headerPtr[m - 1];
                    eptr = headerPtr[m];
                    break;
                }
                h = m - 1;
            } else {
                if (m == headerLength - 1) {
                    sptr = headerPtr[m - 1];
                    eptr = headerPtr[m];
                    break;
                } else if (ip <= headerSip[m + 1]) {
                    sptr = headerPtr[m];
                    eptr = headerPtr[m + 1];
                    break;
                }
                l = m + 1;
            }
        }
        // match nothing just stop it
        if (sptr == 0) {
            return null;
        }
        // 2. search the index blocks to define the data
        int blockLen = eptr - sptr, blen = IndexBlock.getIndexBlockLength();
        // include the right border block
        byte[] iBuffer = new byte[blockLen + blen];
        raf.seek(sptr);
        raf.readFully(iBuffer, 0, iBuffer.length);
        l = 0;
        h = blockLen / blen;
        long sip, eip, dataptr = 0;
        while (l <= h) {
            int m = (l + h) >> 1;
            int p = m * blen;
            sip = RegionSupport.getIntLong(iBuffer, p);
            if (ip < sip) {
                h = m - 1;
            } else {
                eip = RegionSupport.getIntLong(iBuffer, p + 4);
                if (ip > eip) {
                    l = m + 1;
                } else {
                    dataptr = RegionSupport.getIntLong(iBuffer, p + 8);
                    break;
                }
            }
        }
        // 没查询到
        if (dataptr == 0) {
            return null;
        }
        // 获取数据
        int dataLen = (int) ((dataptr >> 24) & 0xFF);
        int dataPtr = (int) ((dataptr & 0x00FFFFFF));
        raf.seek(dataPtr);
        byte[] data = new byte[dataLen];
        raf.readFully(data, 0, data.length);
        return new DataBlock((int) RegionSupport.getIntLong(data, 0), new String(data, 4, data.length - 4, StandardCharsets.UTF_8), dataPtr);
    }

    /**
     * 使用B+TREE算法获取IP区域
     *
     * @param ip ip
     * @return DataBlock ignore
     * @throws IOException ignore
     */
    public DataBlock btreeSearch(String ip) throws IOException {
        return btreeSearch(RegionSupport.ip2long(ip));
    }

    /**
     * 使用二进制算法获取IP区域
     *
     * @param ip ip
     * @return DataBlock ignore
     * @throws IOException ignore
     */
    public DataBlock binarySearch(long ip) throws IOException {
        int blen = IndexBlock.getIndexBlockLength();
        if (totalIndexBlocks == 0) {
            raf.seek(0L);
            byte[] superBytes = new byte[8];
            raf.readFully(superBytes, 0, superBytes.length);
            // initialize the global vars
            firstIndexPtr = RegionSupport.getIntLong(superBytes, 0);
            lastIndexPtr = RegionSupport.getIntLong(superBytes, 4);
            totalIndexBlocks = (int) ((lastIndexPtr - firstIndexPtr) / blen) + 1;
        }
        // search the index blocks to define the data
        int l = 0, h = totalIndexBlocks;
        byte[] buffer = new byte[blen];
        long sip, eip, dataptr = 0;
        while (l <= h) {
            int m = (l + h) >> 1;
            // set the file pointer
            raf.seek(firstIndexPtr + m * blen);
            raf.readFully(buffer, 0, buffer.length);
            sip = RegionSupport.getIntLong(buffer, 0);
            if (ip < sip) {
                h = m - 1;
            } else {
                eip = RegionSupport.getIntLong(buffer, 4);
                if (ip > eip) {
                    l = m + 1;
                } else {
                    dataptr = RegionSupport.getIntLong(buffer, 8);
                    break;
                }
            }
        }
        // 没查询到
        if (dataptr == 0) {
            return null;
        }
        // 获取数据
        int dataLen = (int) ((dataptr >> 24) & 0xFF);
        int dataPtr = (int) ((dataptr & 0x00FFFFFF));
        raf.seek(dataPtr);
        byte[] data = new byte[dataLen];
        raf.readFully(data, 0, data.length);
        return new DataBlock((int) RegionSupport.getIntLong(data, 0), new String(data, 4, data.length - 4, StandardCharsets.UTF_8), dataPtr);
    }

    /**
     * 使用二进制算法获取IP区域
     *
     * @param ip ip
     * @return DataBlock ignore
     * @throws IOException ignore
     */
    public DataBlock binarySearch(String ip) throws IOException {
        return binarySearch(RegionSupport.ip2long(ip));
    }

    /**
     * 获取配置项
     */
    public DbConfig getDbConfig() {
        return dbConfig;
    }

    /**
     * 关闭
     */
    public void close() throws IOException {
        headerSip = null;
        headerPtr = null;
        dbBinStr = null;
        raf.close();
    }

}

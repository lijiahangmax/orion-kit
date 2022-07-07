package com.orion.lang.define;

import java.io.Serializable;

/**
 * IO 写入字节实体
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/9 15:47
 */
public class StreamEntry implements Serializable {

    private static final long serialVersionUID = -8954322539057831L;

    private byte[] bytes;

    private int off;

    private int len;

    public StreamEntry(byte[] bytes) {
        this.bytes = bytes;
        this.len = bytes.length;
    }

    public StreamEntry(byte[] bytes, int off, int len) {
        this.bytes = bytes;
        this.off = off;
        this.len = len;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setOff(int off) {
        this.off = off;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getOff() {
        return off;
    }

    public int getLen() {
        return len;
    }

    @Override
    public String toString() {
        return "off: " + off + ", len: " + len;
    }

}

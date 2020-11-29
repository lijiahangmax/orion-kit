package com.orion.lang;

import java.io.Serializable;

/**
 * IO 写入字节实体
 *
 * @author ljh15
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

    public byte[] getBytes() {
        return bytes;
    }

    public StreamEntry setBytes(byte[] bytes) {
        this.bytes = bytes;
        return this;
    }

    public int getOff() {
        return off;
    }

    public StreamEntry setOff(int off) {
        this.off = off;
        return this;
    }

    public int getLen() {
        return len;
    }

    public StreamEntry setLen(int len) {
        this.len = len;
        return this;
    }

    @Override
    public String toString() {
        return "off: " + off + " len: " + len;
    }

}

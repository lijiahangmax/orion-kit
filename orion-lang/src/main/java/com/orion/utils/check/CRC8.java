package com.orion.utils.check;

import java.util.zip.Checksum;

/**
 * CRC8 实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/3 22:01
 */
public class CRC8 implements Checksum {

    private final short init;

    private final short[] crcTable;

    private short value;

    public CRC8(int polynomial) {
        this(polynomial, (short) 0);
    }

    public CRC8(int polynomial, short init) {
        this.value = this.init = init;
        this.crcTable = new short[256];
        for (int dividend = 0; dividend < 256; dividend++) {
            int remainder = dividend;// << 8;
            for (int bit = 0; bit < 8; ++bit) {
                if ((remainder & 0x01) != 0) {
                    remainder = (remainder >>> 1) ^ polynomial;
                } else {
                    remainder >>>= 1;
                }
            }
            crcTable[dividend] = (short) remainder;
        }
    }

    @Override
    public void update(byte[] b, int offset, int len) {
        for (int i = 0; i < len; i++) {
            int data = b[offset + i] ^ value;
            this.value = (short) (crcTable[data & 0xFF] ^ (value << 8));
        }
    }

    public void update(byte[] b) {
        this.update(b, 0, b.length);
    }

    @Override
    public void update(int b) {
        this.update(new byte[]{(byte) b}, 0, 1);
    }

    @Override
    public long getValue() {
        return value & 0xFF;
    }

    @Override
    public void reset() {
        this.value = init;
    }

}

package com.orion.lang.number;

import java.io.Serializable;

/**
 * 128位数字表示 分高位和低位
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 16:00
 */
public class Number128 implements Serializable {

    private static final long serialVersionUID = -709672812391203L;

    private long lowValue;

    private long highValue;

    /**
     * @param lowValue  低位
     * @param highValue 高位
     */
    public Number128(long lowValue, long highValue) {
        this.lowValue = lowValue;
        this.highValue = highValue;
    }

    public long getLowValue() {
        return lowValue;
    }

    public long getHighValue() {
        return highValue;
    }

    public void setLowValue(long lowValue) {
        this.lowValue = lowValue;
    }

    public void setHighValue(long hiValue) {
        this.highValue = hiValue;
    }

    public long[] getLongArray() {
        return new long[]{lowValue, highValue};
    }

}

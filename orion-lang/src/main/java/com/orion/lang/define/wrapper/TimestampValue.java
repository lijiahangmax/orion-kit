package com.orion.lang.define.wrapper;

import com.orion.lang.constant.Const;

/**
 * 时间戳数据
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/7/5 14:57
 */
public class TimestampValue<T> {

    /**
     * 时间戳
     */
    private Long time;

    /**
     * 数据
     */
    private T value;

    public TimestampValue() {
    }

    public TimestampValue(Long time, T value) {
        this.time = time;
        this.value = value;
    }

    public static <T> TimestampValue<T> of(Long time, T value) {
        return new TimestampValue<>(time, value);
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return time + Const.EMPTY + value;
    }
}

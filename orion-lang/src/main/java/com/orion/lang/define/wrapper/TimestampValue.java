package com.orion.lang.define.wrapper;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Objects1;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

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

    /**
     * 映射
     *
     * @param mapping mapping
     * @param <E>     E
     * @return mapped
     */
    public <E> TimestampValue<E> map(Function<T, E> mapping) {
        return new TimestampValue<>(this.time, Objects1.map(this.value, mapping));
    }

    /**
     * @return Optional
     */
    public Optional<T> optional() {
        return Optional.ofNullable(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimestampValue<?> that = (TimestampValue<?>) o;
        return Objects.equals(time, that.time) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, value);
    }

    @Override
    public String toString() {
        return time + Const.EMPTY + value;
    }

}

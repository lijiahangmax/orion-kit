/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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

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
package cn.orionsec.kit.lang.define.wrapper;

import cn.orionsec.kit.lang.able.IJsonObject;
import cn.orionsec.kit.lang.define.iterator.ArrayIterator;
import cn.orionsec.kit.lang.define.iterator.EmptyIterator;
import cn.orionsec.kit.lang.define.support.CloneSupport;
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.json.Jsons;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 元组
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/15 17:14
 */
public class Tuple extends CloneSupport<Tuple> implements Serializable, IJsonObject, Iterable<Object> {

    private static final long serialVersionUID = 228374192841290087L;

    private final Object[] members;

    public Tuple(Object... members) {
        Valid.notEmpty(members, "arguments size is zero");
        this.members = members;
    }

    /**
     * 创建元组
     *
     * @param members 元素
     * @return Tuple
     */
    public static Tuple of(Object... members) {
        return new Tuple(members);
    }

    /**
     * 获取指定位置元素
     *
     * @param <T>   T
     * @param index index
     * @return 元素
     */
    @SuppressWarnings("unchecked")
    public <T> T get(int index) {
        return (T) members[index];
    }

    /**
     * 获得所有元素
     *
     * @return 获得所有元素
     */
    public Object[] getMembers() {
        return this.members;
    }

    /**
     * 获取元组长度
     *
     * @return 长度
     */
    public int size() {
        return members.length;
    }

    @Override
    public int hashCode() {
        return 31 * Arrays.deepHashCode(members);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Tuple other = (Tuple) obj;
        return Arrays.deepEquals(members, other.members);
    }

    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isEmpty() {
        return Arrays1.isEmpty(members);
    }

    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isNotEmpty() {
        return Arrays1.isNotEmpty(members);
    }

    /**
     * @return stream
     */
    public Stream<?> stream() {
        if (this.isEmpty()) {
            return Stream.empty();
        } else {
            return Stream.of(members);
        }
    }

    /**
     * 获取 Optional
     *
     * @param index 索引
     * @param <T>   T
     * @return Optional
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> optional(int index) {
        int size = this.size();
        if (size > index) {
            return Optional.ofNullable((T) members[index]);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Iterator<Object> iterator() {
        if (!this.isEmpty()) {
            return new ArrayIterator<>(members).iterator();
        } else {
            return new EmptyIterator<>();
        }
    }

    @Override
    public Spliterator<Object> spliterator() {
        return new ArrayIterator<>(members).spliterator();
    }

    @Override
    public void forEach(Consumer<? super Object> action) {
        if (!this.isEmpty()) {
            new ArrayIterator<>(members).forEach(action);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(members);
    }

    @Override
    public String toJsonString() {
        return Jsons.toJsonWriteNull(members);
    }

}

package com.orion.lang.wrapper;

import com.orion.lang.iterator.ArrayIterator;
import com.orion.lang.support.CloneSupport;
import com.orion.utils.Valid;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * 元组
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/15 17:14
 */
public class Tuple extends CloneSupport<Tuple> implements Serializable, Iterable<Object> {

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

    @Override
    public String toString() {
        return Arrays.toString(members);
    }

    @Override
    public Iterator<Object> iterator() {
        return new ArrayIterator<>(members).iterator();
    }

    @Override
    public Spliterator<Object> spliterator() {
        return new ArrayIterator<>(members).spliterator();
    }

    @Override
    public void forEach(Consumer<? super Object> action) {
        new ArrayIterator<>(members).forEach(action);
    }

}

package com.orion.lang.iterator;

import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;

import java.io.Serializable;
import java.util.Iterator;

/**
 * 数组迭代器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/17 12:54
 */
public class ArrayIterator<E> implements Iterator<E>, Iterable<E>, Serializable {

    private static final long serialVersionUID = 1832735384832234L;

    /**
     * 数组
     */
    private final E[] array;

    /**
     * 起始位置
     */
    private int startIndex;

    /**
     * 结束位置
     */
    private int endIndex;

    /**
     * 当前位置
     */
    private int index;

    public ArrayIterator(E[] array) {
        this(array, 0);
    }

    public ArrayIterator(E[] array, int startIndex) {
        this(array, startIndex, -1);
    }

    public ArrayIterator(E[] array, int startIndex, int endIndex) {
        this.endIndex = Arrays1.length(array);
        if (endIndex > 0 && endIndex < this.endIndex) {
            this.endIndex = endIndex;
        }

        if (startIndex >= 0 && startIndex < this.endIndex) {
            this.startIndex = startIndex;
        }
        this.array = array;
        this.index = this.startIndex;
    }

    @Override
    public boolean hasNext() {
        return (index < endIndex);
    }

    @Override
    public E next() {
        if (hasNext()) {
            return array[index++];
        }
        throw Exceptions.noSuchElement();
    }

    @Override
    public void remove() {
        throw Exceptions.unsupported("this is a read-only iterator");
    }

    /**
     * 重置索引
     */
    public void reset() {
        this.index = this.startIndex;
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }

    public E[] getArray() {
        return array;
    }

}

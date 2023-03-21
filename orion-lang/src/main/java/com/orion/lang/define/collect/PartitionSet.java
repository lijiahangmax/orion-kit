package com.orion.lang.define.collect;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.math.Numbers;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 分片 set
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/1/16 16:40
 */
public class PartitionSet<T> extends AbstractSet<Set<T>> implements Iterator<Set<T>> {

    private final Iterator<T> iterator;

    private final int size;

    private final int totalSize;

    private int current;

    public PartitionSet(Set<T> set, int size) {
        this.iterator = set.iterator();
        this.size = size;
        this.totalSize = set.size();
    }

    @Override
    public int size() {
        return (totalSize + size - 1) / size;
    }

    @Override
    public boolean isEmpty() {
        return totalSize == 0;
    }

    @Override
    public Iterator<Set<T>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return this.size() > current;
    }

    @Override
    public Set<T> next() {
        if (!this.hasNext()) {
            throw Exceptions.noSuchElement("there are no more elements");
        }
        current++;
        Set<T> set = new HashSet<>(Numbers.getMin2Power(size));
        for (int i = 0; i < size; i++) {
            if (iterator.hasNext()) {
                set.add(iterator.next());
            } else {
                break;
            }
        }
        return set;
    }

}
package com.orion.lang.define.collect;

import com.orion.lang.utils.Valid;

import java.util.AbstractList;
import java.util.List;

/**
 * 分片 list
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/1/16 16:26
 */
public class PartitionList<E> extends AbstractList<List<E>> {

    private final List<E> list;

    private final int size;

    public PartitionList(List<E> list, int size) {
        this.list = list;
        this.size = size;
    }

    public static <E> PartitionList<E> create(List<E> list, int size) {
        return new PartitionList<>(list, size);
    }

    @Override
    public List<E> get(int index) {
        int listSize = this.size();
        Valid.gte(index, 0, "index {} must not be negative", index);
        Valid.lt(index, listSize, "index {} must be less than size {}", index, listSize);
        int start = index * size;
        int end = Math.min(start + size, list.size());
        return list.subList(start, end);
    }

    @Override
    public int size() {
        return (list.size() + size - 1) / size;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

}
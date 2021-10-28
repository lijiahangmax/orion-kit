package com.orion.lang.collect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 带分页的list
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/11/18 14:48
 */
public class LimitList<E> extends ArrayList<E> implements Serializable {

    private static final long serialVersionUID = 829347812390094123L;

    /**
     * 条数
     */
    private int limit;

    public LimitList() {
        this(10);
    }

    public LimitList(int limit) {
        super();
        this.limit = limit;
    }

    public LimitList(int initialCapacity, int limit) {
        super(initialCapacity);
        this.limit = limit;
    }

    public LimitList(Collection<? extends E> c) {
        this(c, 10);
    }

    public LimitList(Collection<? extends E> c, int limit) {
        super(c);
        this.limit = limit;
    }

    public static <E> LimitList<E> of() {
        return new LimitList<>();
    }

    public static <E> LimitList<E> of(int limit) {
        return new LimitList<>(limit);
    }

    public static <E> LimitList<E> of(int initialCapacity, int limit) {
        return new LimitList<>(initialCapacity, limit);
    }

    public static <E> LimitList<E> of(Collection<? extends E> c) {
        return new LimitList<>(c);
    }

    public static <E> LimitList<E> of(Collection<? extends E> c, int limit) {
        return new LimitList<>(c, limit);
    }

    public LimitList<E> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public List<E> page(int p) {
        int offset = (p - 1) * limit;
        int size = size();
        int end = offset + limit;
        int re = Math.min(end, size);
        List<E> list = new ArrayList<>(re);
        for (int i = offset; i < re; i++) {
            list.add(get(i));
        }
        return list;
    }

    public int getLimit() {
        return limit;
    }

    public int getTotal() {
        return size();
    }

    public int getPages() {
        int size = size();
        return size % limit == 0 ? size / limit : (size / limit) + 1;
    }

}

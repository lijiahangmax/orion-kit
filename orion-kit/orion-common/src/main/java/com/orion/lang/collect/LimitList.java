package com.orion.lang.collect;

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
@SuppressWarnings("ALL")
public class LimitList<E> extends ArrayList<E> {

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

    public List<E> page(int p) {
        int offset = (p - 1) * limit;
        int size = size();
        int end = offset + limit;
        int re = end > size ? size : end;
        List<E> list = new ArrayList<>(re);
        for (int i = offset; i < re; i++) {
            list.add(get(i));
        }
        return list;
    }

    public int getLimit() {
        return limit;
    }

    public LimitList<E> setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public int getTotal() {
        return size();
    }

    public int getPages() {
        int size = size();
        return size % limit == 0 ? size / limit : (size / limit) + 1;
    }

}

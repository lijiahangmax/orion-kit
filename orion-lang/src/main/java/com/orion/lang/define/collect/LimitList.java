package com.orion.lang.define.collect;

import com.orion.lang.KitLangConfiguration;
import com.orion.lang.config.KitConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 带分页的 list
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/11/18 14:48
 */
public class LimitList<E> extends ArrayList<E> implements Serializable {

    private static final long serialVersionUID = 829347812390094123L;

    private static final int LIMIT_LIST_DEFAULT_LIMIT = KitConfig.get(KitLangConfiguration.CONFIG.LIMIT_LIST_DEFAULT_LIMIT);

    /**
     * 条数
     */
    private int limit;

    public LimitList() {
        this(LIMIT_LIST_DEFAULT_LIMIT);
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
        this(c, LIMIT_LIST_DEFAULT_LIMIT);
    }

    public LimitList(Collection<? extends E> c, int limit) {
        super(c);
        this.limit = limit;
    }

    public static <E> LimitList<E> create() {
        return new LimitList<>();
    }

    public static <E> LimitList<E> create(int limit) {
        return new LimitList<>(limit);
    }

    public static <E> LimitList<E> create(int initialCapacity, int limit) {
        return new LimitList<>(initialCapacity, limit);
    }

    public static <E> LimitList<E> create(Collection<? extends E> c) {
        return new LimitList<>(c);
    }

    public static <E> LimitList<E> create(Collection<? extends E> c, int limit) {
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

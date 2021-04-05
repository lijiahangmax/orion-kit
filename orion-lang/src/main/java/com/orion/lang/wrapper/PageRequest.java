package com.orion.lang.wrapper;

import java.io.Serializable;

/**
 * 分页公共请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/3 21:55
 */
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -773462378602349895L;

    /**
     * 页码
     */
    private int page;

    /**
     * 大小
     */
    private int limit;

    public PageRequest() {
        this(1, 10);
    }

    public PageRequest(int page) {
        this(page, 10);
    }

    public PageRequest(int page, int limit) {
        this.page = page;
        this.limit = limit;
    }

    public static PageRequest of() {
        return new PageRequest(1, 10);
    }

    public static PageRequest of(int page) {
        return new PageRequest(page, 10);
    }

    public static PageRequest of(int page, int limit) {
        return new PageRequest(page, limit);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}

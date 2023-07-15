package com.orion.lang.define.wrapper;

import com.orion.lang.KitLangConfiguration;
import com.orion.lang.config.KitConfig;

/**
 * 分页公共请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/3 21:55
 */
public class PageRequest implements IPageRequest {

    private static final long serialVersionUID = -773462378602349895L;

    private static final int DEFAULT_LIMIT = KitConfig.get(KitLangConfiguration.CONFIG.PAGE_REQUEST_DEFAULT_LIMIT);

    /**
     * 页码
     */
    private int page;

    /**
     * 大小
     */
    private int limit;

    public PageRequest() {
        this(1, DEFAULT_LIMIT);
    }

    public PageRequest(int page) {
        this(page, DEFAULT_LIMIT);
    }

    public PageRequest(int page, int limit) {
        this.page = page;
        this.limit = limit;
    }

    public static PageRequest of() {
        return new PageRequest(1, DEFAULT_LIMIT);
    }

    public static PageRequest of(int page) {
        return new PageRequest(page, DEFAULT_LIMIT);
    }

    public static PageRequest of(int page, int limit) {
        return new PageRequest(page, limit);
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "page: " + page + ", limit: " + limit;
    }

}

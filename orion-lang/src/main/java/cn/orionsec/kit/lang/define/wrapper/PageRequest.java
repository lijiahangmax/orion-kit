/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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

import cn.orionsec.kit.lang.KitLangConfiguration;
import cn.orionsec.kit.lang.config.KitConfig;

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

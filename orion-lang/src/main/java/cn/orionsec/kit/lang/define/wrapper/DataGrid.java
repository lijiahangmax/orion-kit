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
import cn.orionsec.kit.lang.able.IJsonObject;
import cn.orionsec.kit.lang.config.KitConfig;
import cn.orionsec.kit.lang.define.iterator.EmptyIterator;
import cn.orionsec.kit.lang.define.support.CloneSupport;
import cn.orionsec.kit.lang.utils.collect.Lists;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * DataGrid 模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/5/30 22:52
 */
public class DataGrid<T> extends CloneSupport<DataGrid<T>> implements Serializable, IJsonObject, Iterable<T> {

    private static final long serialVersionUID = 3787662930250625L;

    private static final int DEFAULT_LIMIT = KitConfig.get(KitLangConfiguration.CONFIG.DATA_GRID_DEFAULT_LIMIT);

    /**
     * 页码
     */
    @JSONField(ordinal = 0)
    private int page;

    /**
     * 每页记录数
     */
    @JSONField(ordinal = 1)
    private int limit;

    /**
     * 当前页的数量 应该 <= limit
     */
    @JSONField(ordinal = 2)
    private int size;

    /**
     * 总页数
     */
    @JSONField(ordinal = 3)
    private int pages;

    /**
     * 总记录数
     */
    @JSONField(ordinal = 4)
    private int total;

    /**
     * 结果列表
     */
    @JSONField(ordinal = 5)
    private List<T> rows;

    public DataGrid() {
        this.page = 1;
        this.limit = DEFAULT_LIMIT;
    }

    public DataGrid(List<T> rows) {
        this(rows, Lists.size(rows));
    }

    public DataGrid(List<T> rows, int total) {
        this();
        this.rows = rows;
        this.size = Lists.size(this.rows);
        this.total = total;
        this.resetPages();
    }

    public DataGrid(Pager<T> pager) {
        this.rows = pager.getRows();
        this.total = pager.getTotal();
        this.pages = pager.getPages();
        this.page = pager.getPage();
        this.limit = pager.getLimit();
        this.size = Lists.size(this.rows);
    }

    public static <T> DataGrid<T> of() {
        return new DataGrid<>();
    }

    public static <T> DataGrid<T> of(List<T> rows) {
        return new DataGrid<>(rows);
    }

    public static <T> DataGrid<T> of(List<T> rows, int total) {
        return new DataGrid<>(rows, total);
    }

    public static <T> DataGrid<T> of(Pager<T> pager) {
        return new DataGrid<>(pager);
    }

    /**
     * 添加结果
     *
     * @param row 结果列
     */
    public DataGrid<T> addRow(T row) {
        if (row == null) {
            return this;
        }
        if (this.rows == null) {
            this.rows = new ArrayList<>();
        }
        this.rows.add(row);
        this.size++;
        this.setTotal(total + 1);
        return this;
    }

    /**
     * 添加结果
     *
     * @param rows 结果列表
     */
    public DataGrid<T> addRows(List<T> rows) {
        if (this.rows == null) {
            this.rows = new ArrayList<>();
        }
        int size = Lists.size(rows);
        if (size > 0) {
            this.rows.addAll(rows);
            this.size += size;
            this.setTotal(total + size);
        }
        return this;
    }

    public List<T> getRows() {
        return rows;
    }

    /**
     * 设置结果
     *
     * @param rows 结果列表
     */
    public void setRows(List<T> rows) {
        this.rows = rows;
        if (rows == null) {
            this.size = 0;
        } else {
            this.size = rows.size();
        }
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        this.resetPages();
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
        this.setTotal(total);
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * 重新设置总页数
     *
     * @return this
     */
    public DataGrid<T> resetPages() {
        if (total == 0) {
            this.pages = 0;
        } else {
            this.pages = total % limit == 0 ? total / limit : (total / limit + 1);
        }
        return this;
    }

    /**
     * 是否还有下一页
     *
     * @return 是否还有下一页
     */
    public boolean hasNextPage() {
        return pages > page;
    }

    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isEmpty() {
        return Lists.isEmpty(rows);
    }

    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isNotEmpty() {
        return Lists.isNotEmpty(rows);
    }

    /**
     * @return stream
     */
    public Stream<T> stream() {
        if (this.isEmpty()) {
            return Stream.empty();
        } else {
            return rows.stream();
        }
    }

    /**
     * 映射
     *
     * @param mapping mapping
     * @param <E>     E
     * @return mapped
     */
    public <E> DataGrid<E> map(Function<T, E> mapping) {
        DataGrid<E> result = new DataGrid<>();
        result.page = this.page;
        result.limit = this.limit;
        result.size = this.size;
        result.pages = this.pages;
        result.total = this.total;
        result.rows = Lists.map(this.rows, mapping);
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(rows);
    }

    @Override
    public Iterator<T> iterator() {
        if (!this.isEmpty()) {
            return rows.iterator();
        } else {
            return new EmptyIterator<>();
        }
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        if (!this.isEmpty()) {
            rows.forEach(action);
        }
    }

    @Override
    public Spliterator<T> spliterator() {
        return rows.spliterator();
    }

}

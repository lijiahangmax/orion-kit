package com.orion.lang.wrapper;

import com.alibaba.fastjson.annotation.JSONField;
import com.orion.able.JsonAble;
import com.orion.lang.support.CloneSupport;
import com.orion.utils.collect.Lists;
import com.orion.utils.json.Jsons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * DataGrid模型
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/5/30 22:52
 */
public class DataGrid<T> extends CloneSupport<DataGrid<T>> implements Serializable, JsonAble, Iterable<T> {

    private static final long serialVersionUID = 3787662930250625L;

    /**
     * 总记录数
     */
    private int total;

    /**
     * 结果列表
     */
    private List<T> rows;

    /**
     * 第几页
     */
    private int page = 1;

    /**
     * 每页记录数
     */
    private int limit = 10;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 当前页的数量 应该 <= limit
     */
    private int size;

    public DataGrid() {
    }

    public DataGrid(List<T> rows) {
        this.rows = rows;
        this.size = Lists.size(this.rows);
    }

    public DataGrid(List<T> rows, int total) {
        this.rows = rows;
        this.total = total;
        if (total != 0) {
            this.pages = total % limit == 0 ? total / limit : (total / limit + 1);
        }
        this.size = Lists.size(this.rows);
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
    public DataGrid<T> setRows(List<T> rows) {
        this.rows = rows;
        if (rows != null) {
            size = rows.size();
        }
        return this;
    }

    public int getTotal() {
        return total;
    }

    public DataGrid<T> setTotal(int total) {
        this.total = total;
        if (total != 0) {
            this.pages = total % limit == 0 ? total / limit : (total / limit + 1);
        }
        return this;
    }

    public int getPage() {
        return page;
    }

    public DataGrid<T> setPage(int page) {
        this.page = page;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public DataGrid<T> setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public int getPages() {
        return pages;
    }

    public DataGrid<T> setPages(int pages) {
        this.pages = pages;
        return this;
    }

    public DataGrid<T> setPages() {
        if (total != 0) {
            pages = total % limit == 0 ? total / limit : (total / limit + 1);
        }
        return this;
    }

    public int getSize() {
        return size;
    }

    public DataGrid<T> setSize(int size) {
        this.size = size;
        return this;
    }

    @JSONField(serialize = false)
    public boolean isEmpty() {
        return Lists.isEmpty(rows);
    }

    @Override
    public String toString() {
        return String.valueOf(rows);
    }

    @Override
    public String toJsonString() {
        return Jsons.toJsonWriteNull(this);
    }

    @Override
    public Iterator<T> iterator() {
        return rows.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        rows.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return rows.spliterator();
    }

}

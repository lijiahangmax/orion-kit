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
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 分页信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/5/30 22:52
 */
public class Pager<T> extends CloneSupport<Pager<T>> implements Serializable, IJsonObject, Iterable<T> {

    private static final long serialVersionUID = 6354348839019830L;

    private static final int DEFAULT_LIMIT = KitConfig.get(KitLangConfiguration.CONFIG.PAGER_DEFAULT_LIMIT);

    /*
        <sql id="Base_Pager_Offset">
            LIMIT #{pager.offset}, #{pager.limit}
        </sql>

        <sql id="Base_Pager_Limit">
            ${pager.sql}
        </sql>

        <if test="pager != null">
            <include refid="Base_Pager_Offset"/>
        </if>
     */

    /**
     * 当前页
     */
    private int page;

    /**
     * 每页显示记录数
     */
    private int limit;

    /**
     * 分页数据
     */
    private List<T> rows;

    /**
     * 当前页起始记录
     */
    private int offset;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 总记录数
     */
    private int total;

    /**
     * 上一页
     */
    private int prePage;

    /**
     * 下一页
     */
    private int nextPage;

    /**
     * sql
     */
    private String sql;

    public Pager() {
        this(1, DEFAULT_LIMIT);
    }

    public Pager(int page) {
        this(page, DEFAULT_LIMIT);
    }

    public Pager(IPageRequest request) {
        this(request.getPage(), request.getLimit());
    }

    /**
     * 构造函数
     *
     * @param page  当前页
     * @param limit 每页记录数大小
     */
    public Pager(int page, int limit) {
        this.page = page;
        this.limit = limit;
        this.prePage = 1;
        this.nextPage = 1;
        this.resetOffset();
    }

    public static <T> Pager<T> of() {
        return new Pager<>(1, DEFAULT_LIMIT);
    }

    public static <T> Pager<T> of(int page) {
        return new Pager<>(page, DEFAULT_LIMIT);
    }

    public static <T> Pager<T> of(int page, int limit) {
        return new Pager<>(page, limit);
    }

    public static <T> Pager<T> of(IPageRequest request) {
        return new Pager<>(request);
    }

    /**
     * 判断本页是否还需要继续查询
     *
     * @param count count
     * @param pager pager
     * @return true 需要查询
     */
    public static boolean hasMoreData(int count, Pager<?> pager) {
        if (pager == null) {
            return count != 0;
        }
        return pager.offset < count;
    }

    public boolean hasMoreData() {
        return hasMoreData(this.total, this);
    }

    public boolean hasMoreData(int count) {
        return hasMoreData(count, this);
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        this.resetOffset();
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
        this.resetOffset();
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
        this.prePage = this.page - 1;
        this.nextPage = this.page + 1;
        if (this.page <= 1) {
            this.prePage = 1;
        }
        if (this.page >= this.pages) {
            this.nextPage = this.pages;
        }
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        if (total != 0) {
            this.setPages(total % limit == 0 ? total / limit : (total / limit + 1));
        }
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
        this.sql = "LIMIT " + offset + ", " + limit;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Pager<T> toNextPage() {
        this.setPages(page + 1);
        return this;
    }

    /**
     * 下几页
     *
     * @param nextPage next
     * @return this
     */
    public Pager<T> toNextPage(int nextPage) {
        this.setPages(page + nextPage);
        return this;
    }

    /**
     * 重置偏移量
     */
    private void resetOffset() {
        this.setOffset((page - 1) * limit);
    }

    /**
     * 转化为数据表格容器
     *
     * @return 容器
     */
    public DataGrid<T> toDataGrid() {
        return new DataGrid<>(this);
    }

    /**
     * 获取总页数
     *
     * @param total 条数
     * @param pager 分页
     * @return 页数
     */
    public static int getPages(int total, Pager<?> pager) {
        if (pager == null) {
            return total >= 1 ? 1 : 0;
        }
        int limit = pager.getLimit();
        if (limit == 0) {
            return total >= 1 ? 1 : 0;
        }
        return total % limit == 0 ? total / limit : (total / limit) + 1;
    }

    /**
     * 获取总页数
     *
     * @param total 条数
     * @param limit 条数
     * @return 页数
     */
    public static int getPages(int total, int limit) {
        if (limit == 0) {
            return total >= 1 ? 1 : 0;
        }
        return total % limit == 0 ? total / limit : (total / limit) + 1;
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
    public <E> Pager<E> map(Function<T, E> mapping) {
        Pager<E> result = new Pager<>();
        result.page = this.page;
        result.limit = this.limit;
        result.offset = this.offset;
        result.pages = this.pages;
        result.total = this.total;
        result.prePage = this.prePage;
        result.nextPage = this.nextPage;
        result.sql = this.sql;
        result.rows = Lists.map(this.rows, mapping);
        return result;
    }

    @Override
    public String toString() {
        return sql;
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

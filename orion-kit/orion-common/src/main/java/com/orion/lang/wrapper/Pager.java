package com.orion.lang.wrapper;

import com.orion.able.Jsonable;
import com.orion.utils.collect.Lists;
import com.orion.utils.json.Jsons;

import java.io.Serializable;
import java.util.List;

/**
 * 分页信息
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/5/30 22:52
 */
@SuppressWarnings("ALL")
public class Pager<T> implements Serializable, Jsonable {

    private static final long serialVersionUID = 6354348839019830L;

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
    private int page = 1;

    /**
     * 每页显示记录数
     */
    private int limit = 10;

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
    private int prePage = 1;

    /**
     * 下一页
     */
    private int nextPage = 1;

    /**
     * sql
     */
    private String sql;

    /**
     * 默认当前页为1, 每页显示数为10
     */
    public Pager() {
        this.sql = "LIMIT " + 0 + ", " + limit;
    }

    /**
     * 构造函数
     *
     * @param page 当前页
     */
    public Pager(int page) {
        this.page = page;
        this.offset = (page - 1) * limit;
        this.sql = "LIMIT " + offset + ", " + limit;
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
        this.offset = (page - 1) * limit;
        this.sql = "LIMIT " + offset + ", " + limit;
    }

    public List<T> getRows() {
        return rows;
    }

    public Pager<T> setRows(List<T> rows) {
        this.rows = rows;
        return this;
    }

    public int getPage() {
        return page;
    }

    public Pager<T> setPage(int page) {
        this.page = page;
        this.offset = (page - 1) * limit;
        this.sql = "LIMIT " + offset + ", " + limit;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public Pager<T> setLimit(int limit) {
        this.limit = limit;
        this.offset = (page - 1) * limit;
        this.sql = "LIMIT " + offset + ", " + limit;
        return this;
    }

    public int getPages() {
        return pages;
    }

    public Pager<T> setPages(int pages) {
        this.pages = pages;
        this.prePage = this.page - 1;
        this.nextPage = this.page + 1;
        if (this.page <= 1) {
            this.prePage = 1;
        }
        if (this.page >= this.pages) {
            this.nextPage = this.pages;
        }
        return this;
    }

    public int getTotal() {
        return total;
    }

    public Pager<T> setTotal(int total) {
        this.total = total;
        if (total != 0) {
            setPages(total % limit == 0 ? total / limit : (total / limit + 1));
        }
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public Pager<T> setOffset() {
        setOffset((page - 1) * limit);
        return this;
    }

    public Pager<T> setOffset(int offset) {
        this.offset = offset;
        this.sql = "LIMIT " + offset + ", " + limit;
        return this;
    }

    public int getPrePage() {
        return prePage;
    }

    public Pager<T> setPrePage(int prePage) {
        this.prePage = prePage;
        return this;
    }

    public int getNextPage() {
        return nextPage;
    }

    public Pager<T> setNextPage(int nextPage) {
        this.nextPage = nextPage;
        return this;
    }

    public String getSql() {
        return sql;
    }

    public Pager<T> setSql(String sql) {
        this.sql = sql;
        return this;
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
    public static int getPages(int total, Pager pager) {
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

    public boolean isEmpty() {
        return Lists.isEmpty(rows);
    }

    @Override
    public String toJsonString() {
        return Jsons.toJSONWriteNull(this);
    }

    @Override
    public String toString() {
        return "Pager{" +
                "page=" + page +
                ", limit=" + limit +
                ", rows=" + rows +
                ", offset=" + offset +
                ", pages=" + pages +
                ", total=" + total +
                ", prePage=" + prePage +
                ", nextPage=" + nextPage +
                ", sql='" + sql + '\'' +
                '}';
    }

}

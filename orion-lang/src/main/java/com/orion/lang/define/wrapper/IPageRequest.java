package com.orion.lang.define.wrapper;

import java.io.Serializable;

/**
 * 分页公共请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/12 22:54
 */
public interface IPageRequest extends Serializable {

    /**
     * 获取页码
     *
     * @return page
     */
    int getPage();

    /**
     * 设置页码
     *
     * @param page page
     */
    void setPage(int page);

    /**
     * 获取条数
     *
     * @return limit
     */
    int getLimit();

    /**
     * 设置条数
     *
     * @param limit limit
     */
    void setLimit(int limit);

}

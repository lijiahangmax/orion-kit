package com.orion.office.excel.type;

/**
 * Import 字段类型
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/2 0:15
 */
public enum ExcelReadType {

    /**
     * TEXT
     */
    TEXT,

    /**
     * 数字
     */
    DECIMAL,

    /**
     * 整数
     */
    INTEGER,

    /**
     * 手机号
     */
    PHONE,

    /**
     * 日期
     */
    DATE,

    /**
     * 超链接
     */
    LINK_ADDRESS,

    /**
     * 批注
     */
    COMMENT,

    /**
     * 图片byte[]
     */
    PICTURE

}

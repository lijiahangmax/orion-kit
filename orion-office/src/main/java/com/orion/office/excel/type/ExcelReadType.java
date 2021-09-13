package com.orion.office.excel.type;

/**
 * import 字段类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/2 0:15
 */
public enum ExcelReadType {

    /**
     * TEXT
     *
     * @see String
     */
    TEXT,

    /**
     * 数字
     *
     * @see java.math.BigDecimal
     */
    DECIMAL,

    /**
     * 整数
     *
     * @see Integer
     */
    INTEGER,

    /**
     * 整数
     *
     * @see Long
     */
    LONG,

    /**
     * 手机号
     *
     * @see String
     */
    PHONE,

    /**
     * 日期
     *
     * @see java.util.Date
     */
    DATE,

    /**
     * 超链接
     *
     * @see String
     */
    LINK_ADDRESS,

    /**
     * 批注
     *
     * @see String
     */
    COMMENT,

    /**
     * 图片
     *
     * @see byte[]
     * @see String base64
     * @see java.io.OutputStream
     * @see java.io.ByteArrayOutputStream
     */
    PICTURE

}

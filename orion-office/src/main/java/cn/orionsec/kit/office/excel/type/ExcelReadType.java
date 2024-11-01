/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.office.excel.type;

/**
 * excel 字段类型
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

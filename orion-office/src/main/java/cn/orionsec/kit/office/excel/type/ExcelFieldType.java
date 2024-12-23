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
package cn.orionsec.kit.office.excel.type;

import cn.orionsec.kit.lang.utils.reflect.Classes;
import cn.orionsec.kit.lang.utils.time.Dates;

/**
 * excel 字段类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/6 22:00
 */
public enum ExcelFieldType {

    /**
     * 自动推断
     */
    AUTO,

    /**
     * 文本
     */
    TEXT,

    /**
     * 数字
     */
    NUMBER,

    /**
     * 公式
     */
    FORMULA,

    /**
     * 时间
     */
    DATE,

    /**
     * 时间 格式化
     *
     * @see #TEXT
     */
    DATE_FORMAT,

    /**
     * 小数 格式化
     *
     * @see #TEXT
     */
    DECIMAL_FORMAT,

    /**
     * 布尔值
     */
    BOOLEAN;

    /**
     * 设置ExcelFieldType
     *
     * @param clazz clazz
     * @return ExcelFieldType
     */
    public static ExcelFieldType of(Class<?> clazz) {
        if (clazz == null) {
            return TEXT;
        }
        if (Classes.isNumberClass(clazz)) {
            return NUMBER;
        } else if (Dates.isDateClass(clazz)) {
            return DATE;
        } else if (clazz.equals(Boolean.TYPE) || clazz.equals(Boolean.class)) {
            return BOOLEAN;
        } else {
            return TEXT;
        }
    }

}

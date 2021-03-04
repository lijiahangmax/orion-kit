package com.orion.office.excel.type;

import com.orion.utils.reflect.Classes;
import com.orion.utils.time.Dates;

/**
 * Excel 字段类型
 *
 * @author ljh15
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

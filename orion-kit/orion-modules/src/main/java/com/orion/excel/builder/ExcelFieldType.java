package com.orion.excel.builder;

/**
 * Excel 字段类型
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/6 22:00
 */
public enum ExcelFieldType {

    /**
     * 文本
     */
    TEXT(1),

    /**
     * 数字
     */
    NUMBER(2),

    /**
     * 时间
     */
    DATE(3);

    int code;

    ExcelFieldType(int code) {
        this.code = code;
    }

}

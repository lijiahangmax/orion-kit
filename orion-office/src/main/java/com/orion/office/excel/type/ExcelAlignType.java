package com.orion.office.excel.type;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * Excel 水平对齐方式
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/12/21 14:13
 */
public enum ExcelAlignType {

    /**
     * 默认
     */
    DEFAULT(HorizontalAlignment.GENERAL.getCode()),

    /**
     * 左对齐
     */
    LEFT(HorizontalAlignment.LEFT.getCode()),

    /**
     * 居中
     */
    CENTER(HorizontalAlignment.CENTER.getCode()),

    /**
     * 右对齐
     */
    RIGHT(HorizontalAlignment.RIGHT.getCode()),

    /**
     * 填充
     */
    FILL(HorizontalAlignment.FILL.getCode()),

    /**
     * 两端对齐
     */
    JUSTIFY(HorizontalAlignment.JUSTIFY.getCode()),

    /**
     * 跨列举中
     */
    CENTER_SELECTION(HorizontalAlignment.CENTER_SELECTION.getCode()),

    /**
     * 分布对齐
     */
    DISTRIBUTED(HorizontalAlignment.DISTRIBUTED.getCode());

    private int code;

    ExcelAlignType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}

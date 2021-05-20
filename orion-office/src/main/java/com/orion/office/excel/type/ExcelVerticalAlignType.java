package com.orion.office.excel.type;

import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * Excel 垂直对齐类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/21 14:16
 */
public enum ExcelVerticalAlignType {

    /**
     * 默认
     */
    DEFAULT(-1),

    /**
     * 上对齐
     */
    TOP(VerticalAlignment.TOP.getCode()),

    /**
     * 居中对齐
     */
    CENTER(VerticalAlignment.CENTER.getCode()),

    /**
     * 下对齐
     */
    BOTTOM(VerticalAlignment.BOTTOM.getCode()),

    /**
     * 两端对齐
     */
    JUSTIFY(VerticalAlignment.JUSTIFY.getCode()),

    /**
     * 分布对齐
     */
    DISTRIBUTED(VerticalAlignment.DISTRIBUTED.getCode());

    private int code;

    ExcelVerticalAlignType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}

package com.orion.office.excel.type;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * excel 边距类型
 * 1英寸 = 2.54cm
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/14 17:16
 */
public enum ExcelMarginType {

    /**
     * 左边距
     */
    LEFT(Sheet.LeftMargin),

    /**
     * 右边距
     */
    RIGHT(Sheet.RightMargin),

    /**
     * 上边距
     */
    TOP(Sheet.TopMargin),

    /**
     * 下边距
     */
    BOTTOM(Sheet.BottomMargin),

    /**
     * 页眉边距
     */
    HEADER(Sheet.HeaderMargin),

    /**
     * 页脚边距
     */
    FOOTER(Sheet.FooterMargin);

    private final short code;

    ExcelMarginType(short code) {
        this.code = code;
    }

    public short getCode() {
        return code;
    }

}

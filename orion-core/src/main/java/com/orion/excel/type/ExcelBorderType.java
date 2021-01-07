package com.orion.excel.type;

import org.apache.poi.ss.usermodel.BorderStyle;

/**
 * Excel 边框类型
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/12/21 14:08
 */
public enum ExcelBorderType {

    /**
     * 无边框 默认
     */
    DEFAULT(BorderStyle.NONE.getCode()),

    /**
     * 细边框
     */
    THIN(BorderStyle.THIN.getCode()),

    /**
     * 中等边框
     */
    MEDIUM(BorderStyle.MEDIUM.getCode()),

    /**
     * 粗边框
     */
    THICK(BorderStyle.THICK.getCode()),

    /**
     * 虚线边框
     */
    DASHED(BorderStyle.DASHED.getCode()),

    /**
     * 点边框
     */
    DOTTED(BorderStyle.DOTTED.getCode()),

    /**
     * 丝纹边框
     */
    HAIR(BorderStyle.HAIR.getCode()),

    /**
     * 双线边框
     */
    DOUBLE(BorderStyle.DOUBLE.getCode());

    private int code;

    ExcelBorderType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}

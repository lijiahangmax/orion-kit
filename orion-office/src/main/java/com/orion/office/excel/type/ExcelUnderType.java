package com.orion.office.excel.type;

import org.apache.poi.ss.usermodel.Font;

/**
 * excel 下滑线类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/26 1:11
 */
public enum ExcelUnderType {

    /**
     * 无下划线
     */
    NONE(Font.U_NONE),

    /**
     * 单下划线
     */
    SINGLE(Font.U_SINGLE),

    /**
     * 双下划线
     */
    DOUBLE(Font.U_DOUBLE),

    /**
     * 单下划线 会计风格
     */
    SINGLE_ACCOUNTING(Font.U_SINGLE_ACCOUNTING),

    /**
     * 双下划线 会计风格
     */
    DOUBLE_ACCOUNTING(Font.U_DOUBLE_ACCOUNTING);

    private final int code;

    ExcelUnderType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}

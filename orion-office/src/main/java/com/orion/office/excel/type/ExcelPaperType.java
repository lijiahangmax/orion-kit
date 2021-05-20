package com.orion.office.excel.type;

import org.apache.poi.ss.usermodel.PrintSetup;

/**
 * Excel 纸张类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/30 17:38
 */
public enum ExcelPaperType {

    /**
     * 默认
     */
    DEFAULT(PrintSetup.PRINTER_DEFAULT_PAPERSIZE),

    /**
     * A3 297x420 mm
     */
    A3(PrintSetup.A3_PAPERSIZE),

    /**
     * A4 210x297 mm
     */
    A4(PrintSetup.A4_PAPERSIZE),

    /**
     * A4 小  210x297 mm
     */
    A4_SMALL(PrintSetup.A4_SMALL_PAPERSIZE),

    /**
     * A4 大 210x330 mm
     */
    A4_PLUS(PrintSetup.A4_PLUS_PAPERSIZE),

    /**
     * A4 横向 210x297 mm
     */
    A4_TRANSVERSE(PrintSetup.A4_TRANSVERSE_PAPERSIZE),

    /**
     * A4 旋转 297x210 mm
     */
    A4_ROTATED(PrintSetup.A4_ROTATED_PAPERSIZE),

    /**
     * A5 148x210 mm
     */
    A5(PrintSetup.A5_PAPERSIZE),

    /**
     * B4 250x354 mm
     */
    B4(PrintSetup.B4_PAPERSIZE),

    /**
     * B5 182x257 mm
     */
    B5(PrintSetup.B5_PAPERSIZE),

    /**
     * C3 324x458 mm
     */
    C3(PrintSetup.ENVELOPE_C3_PAPERSIZE),

    /**
     * C4 229x324 mm
     */
    C4(PrintSetup.ENVELOPE_C4_PAPERSIZE),

    /**
     * C5 162x229 mm
     */
    C5(PrintSetup.ENVELOPE_C5_PAPERSIZE),

    /**
     * C6 114x162 mm
     */
    C6(PrintSetup.ENVELOPE_C6_PAPERSIZE),

    /**
     * 215x275 mm
     */
    QUARTO(PrintSetup.QUARTO_PAPERSIZE),

    /**
     * 110x220 mm
     */
    DL(PrintSetup.ENVELOPE_DL_PAPERSIZE);

    ExcelPaperType(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }

}

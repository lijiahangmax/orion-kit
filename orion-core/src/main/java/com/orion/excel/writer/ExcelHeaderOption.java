package com.orion.excel.writer;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import java.io.Serializable;

/**
 * Excel 表头
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/1 17:06
 */
public class ExcelHeaderOption implements Serializable {

    /**
     * 表头值
     */
    private String value;

    /**
     * 单元格样式
     */
    private CellStyle cellStyle;

    /**
     * 字体样式
     */
    private Font font;

    public String getValue() {
        return value;
    }

    public ExcelHeaderOption setValue(String value) {
        this.value = value;
        return this;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public ExcelHeaderOption setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public ExcelHeaderOption setFont(Font font) {
        this.font = font;
        return this;
    }

}

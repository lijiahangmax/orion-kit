package com.orion.excel.builder;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import java.io.Serializable;

/**
 * Excel 表头映射
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/6/1 17:06
 */
public class ExcelHeaderMap implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 单元格样式
     */
    private CellStyle cellStyle;

    /**
     * 字体样式
     */
    private Font font;

    public String getName() {
        return name;
    }

    public ExcelHeaderMap setName(String name) {
        this.name = name;
        return this;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public ExcelHeaderMap setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public ExcelHeaderMap setFont(Font font) {
        this.font = font;
        return this;
    }

    @Override
    public String toString() {
        return "ExcelHeaderMap{" +
                "name='" + name + '\'' +
                ", cellStyle=" + cellStyle +
                ", font=" + font +
                '}';
    }

}

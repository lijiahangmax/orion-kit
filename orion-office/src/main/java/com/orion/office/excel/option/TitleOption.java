package com.orion.office.excel.option;

import com.orion.office.excel.type.ExcelAlignType;
import com.orion.office.excel.type.ExcelBorderType;
import com.orion.office.excel.type.ExcelVerticalAlignType;

import java.io.Serializable;

/**
 * excel 标题配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/31 14:56
 */
public class TitleOption implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 使用行数
     */
    private int useRow;

    /**
     * 使用列数 默认列长
     */
    private int useColumn;

    /**
     * 垂直对齐方式
     */
    private ExcelVerticalAlignType verticalAlign;

    /**
     * 水平对齐方式
     */
    private ExcelAlignType align;

    /**
     * 背景颜色 RGB
     */
    private String backgroundColor;

    /**
     * 边框
     */
    private ExcelBorderType border;

    /**
     * 边框颜色 RGB
     */
    private String borderColor;

    /**
     * 调色板索引
     */
    private short paletteColorIndex;

    /**
     * 字体
     */
    private FontOption font;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUseRow() {
        return useRow;
    }

    public void setUseRow(int useRow) {
        this.useRow = useRow;
    }

    public int getUseColumn() {
        return useColumn;
    }

    public void setUseColumn(int useColumn) {
        this.useColumn = useColumn;
    }

    public ExcelVerticalAlignType getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(ExcelVerticalAlignType verticalAlign) {
        this.verticalAlign = verticalAlign;
    }

    public ExcelAlignType getAlign() {
        return align;
    }

    public void setAlign(ExcelAlignType align) {
        this.align = align;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public ExcelBorderType getBorder() {
        return border;
    }

    public void setBorder(ExcelBorderType border) {
        this.border = border;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public FontOption getFont() {
        return font;
    }

    public void setFont(FontOption font) {
        this.font = font;
    }

    public short getPaletteColorIndex() {
        return paletteColorIndex;
    }

    public void setPaletteColorIndex(short paletteColorIndex) {
        this.paletteColorIndex = paletteColorIndex;
    }

}

/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.office.excel.option;

import cn.orionsec.kit.office.excel.type.ExcelAlignType;
import cn.orionsec.kit.office.excel.type.ExcelBorderType;
import cn.orionsec.kit.office.excel.type.ExcelVerticalAlignType;

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

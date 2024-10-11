/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package com.orion.office.excel.option;

import com.orion.office.excel.type.ExcelAlignType;
import com.orion.office.excel.type.ExcelBorderType;
import com.orion.office.excel.type.ExcelFieldType;
import com.orion.office.excel.type.ExcelVerticalAlignType;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * export 字段写入参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/28 11:20
 */
public class ExportFieldOption implements Serializable {

    /**
     * 单元格宽
     */
    private Integer width;

    /**
     * 自动换行
     */
    private boolean wrapText;

    /**
     * 垂直对齐方式
     */
    private ExcelVerticalAlignType verticalAlign;

    /**
     * 水平对齐方式
     */
    private ExcelAlignType align;

    /**
     * 背景颜色
     */
    private String backgroundColor;

    /**
     * 字体样式
     */
    private FontOption fontOption;

    /**
     * 批注参数
     */
    private CommentOption commentOption;

    /**
     * 超链接参数
     */
    private LinkOption linkOption;

    /**
     * 单元格参数
     */
    private CellOption cellOption;

    /**
     * 图片参数
     */
    private PictureOption pictureOption;

    /**
     * 是否设置边框
     */
    private ExcelBorderType border;

    /**
     * 边框颜色
     */
    private String borderColor;

    /**
     * 缩进
     */
    private Short indent;

    /**
     * 格式
     */
    private String format;

    /**
     * type
     */
    private ExcelFieldType type;

    /**
     * 是否清除空格
     *
     * @see ExcelFieldType#TEXT
     * @see String
     */
    private boolean trim;

    /**
     * 表头
     */
    private String header;

    /**
     * 表头跳过样式
     */
    private boolean skipHeaderStyle;

    /**
     * 是否隐藏
     */
    private boolean hidden;

    /**
     * 是否锁定
     */
    private boolean lock;

    /**
     * 自动调节大小
     */
    private boolean autoResize;

    /**
     * 公式前缀
     */
    private boolean quotePrefixed;

    /**
     * 选择框
     */
    private String[] selectOptions;

    /**
     * 调色板索引 HSSFColor
     */
    private short paletteColorIndex;

    /**
     * getter
     */
    private Method getterMethod;

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public boolean isWrapText() {
        return wrapText;
    }

    public void setWrapText(boolean wrapText) {
        this.wrapText = wrapText;
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

    public FontOption getFontOption() {
        return fontOption;
    }

    public void setFontOption(FontOption fontOption) {
        this.fontOption = fontOption;
    }

    public CommentOption getCommentOption() {
        return commentOption;
    }

    public void setCommentOption(CommentOption commentOption) {
        this.commentOption = commentOption;
    }

    public LinkOption getLinkOption() {
        return linkOption;
    }

    public void setLinkOption(LinkOption linkOption) {
        this.linkOption = linkOption;
    }

    public CellOption getCellOption() {
        return cellOption;
    }

    public void setCellOption(CellOption cellOption) {
        this.cellOption = cellOption;
    }

    public PictureOption getPictureOption() {
        return pictureOption;
    }

    public void setPictureOption(PictureOption pictureOption) {
        this.pictureOption = pictureOption;
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

    public Short getIndent() {
        return indent;
    }

    public void setIndent(Short indent) {
        this.indent = indent;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public ExcelFieldType getType() {
        return type;
    }

    public void setType(ExcelFieldType type) {
        this.type = type;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public boolean isSkipHeaderStyle() {
        return skipHeaderStyle;
    }

    public void setSkipHeaderStyle(boolean skipHeaderStyle) {
        this.skipHeaderStyle = skipHeaderStyle;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean isAutoResize() {
        return autoResize;
    }

    public void setAutoResize(boolean autoResize) {
        this.autoResize = autoResize;
    }

    public boolean isQuotePrefixed() {
        return quotePrefixed;
    }

    public void setQuotePrefixed(boolean quotePrefixed) {
        this.quotePrefixed = quotePrefixed;
    }

    public String[] getSelectOptions() {
        return selectOptions;
    }

    public void setSelectOptions(String[] selectOptions) {
        this.selectOptions = selectOptions;
    }

    public short getPaletteColorIndex() {
        return paletteColorIndex;
    }

    public void setPaletteColorIndex(short paletteColorIndex) {
        this.paletteColorIndex = paletteColorIndex;
    }

    public Method getGetterMethod() {
        return getterMethod;
    }

    public void setGetterMethod(Method getterMethod) {
        this.getterMethod = getterMethod;
    }

    public boolean isTrim() {
        return trim;
    }

    public void setTrim(boolean trim) {
        this.trim = trim;
    }

}

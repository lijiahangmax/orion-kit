package com.orion.excel.common;

import java.io.Serializable;

/**
 * Excel 导出注解 字段样式
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/28 11:20
 */
public class ExportFieldStyle implements Serializable {

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
     * 0上对齐 1居中对齐 2下对齐
     */
    private Integer verticalAlign;

    /**
     * 水平对齐方式
     * 0默认 1左对齐 2居中 3右对齐 5两端对齐
     */
    private Integer align;

    /**
     * 背景颜色
     */
    private String backgroundColor;

    /**
     * 字体样式
     */
    private ExportFontStyle fontStyle;

    /**
     * 时间格式
     */
    private String datePattern;

    public Integer getWidth() {
        return width;
    }

    public ExportFieldStyle setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public boolean isWrapText() {
        return wrapText;
    }

    public ExportFieldStyle setWrapText(boolean wrapText) {
        this.wrapText = wrapText;
        return this;
    }

    public Integer getVerticalAlign() {
        return verticalAlign;
    }

    public ExportFieldStyle setVerticalAlign(Integer verticalAlign) {
        this.verticalAlign = verticalAlign;
        return this;
    }

    public Integer getAlign() {
        return align;
    }

    public ExportFieldStyle setAlign(Integer align) {
        this.align = align;
        return this;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public ExportFieldStyle setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public ExportFontStyle getFontStyle() {
        return fontStyle;
    }

    public ExportFieldStyle setFontStyle(ExportFontStyle fontStyle) {
        this.fontStyle = fontStyle;
        return this;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public ExportFieldStyle setDatePattern(String datePattern) {
        this.datePattern = datePattern;
        return this;
    }

    @Override
    public String toString() {
        return "ExportFieldStyle{" +
                "width=" + width +
                ", wrapText=" + wrapText +
                ", verticalAlign=" + verticalAlign +
                ", align=" + align +
                ", backgroundColor=" + backgroundColor +
                ", fontStyle=" + fontStyle +
                ", datePattern='" + datePattern + '\'' +
                '}';
    }

}

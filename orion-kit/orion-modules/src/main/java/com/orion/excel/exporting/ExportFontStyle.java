package com.orion.excel.exporting;

import java.io.Serializable;

/**
 * Excel 导出注解 字体样式
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/28 11:20
 */
public class ExportFontStyle implements Serializable {

    /**
     * 字体名称
     */
    private String fontName;

    /**
     * 字体大小
     */
    private Integer fontSize;

    /**
     * 字体颜色
     */
    private String fontColor;

    /**
     * 加粗
     */
    private boolean bold;

    /**
     * 斜体
     */
    private boolean italic;

    /**
     * 下滑线
     */
    private boolean under;

    public String getFontName() {
        return fontName;
    }

    public ExportFontStyle setFontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public ExportFontStyle setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public String getFontColor() {
        return fontColor;
    }

    public ExportFontStyle setFontColor(String fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    public boolean isBold() {
        return bold;
    }

    public ExportFontStyle setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public boolean isItalic() {
        return italic;
    }

    public ExportFontStyle setItalic(boolean italic) {
        this.italic = italic;
        return this;
    }

    public boolean isUnder() {
        return under;
    }

    public ExportFontStyle setUnder(boolean under) {
        this.under = under;
        return this;
    }

    @Override
    public String toString() {
        return "ExportFontStyle{" +
                "fontName='" + fontName + '\'' +
                ", fontSize=" + fontSize +
                ", fontColor=" + fontColor +
                ", bold=" + bold +
                ", italic=" + italic +
                ", under=" + under +
                '}';
    }

}

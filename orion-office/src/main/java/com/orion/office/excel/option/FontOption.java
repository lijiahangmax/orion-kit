package com.orion.office.excel.option;

import com.orion.office.excel.type.ExcelUnderType;

import java.io.Serializable;

/**
 * excel 字体参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/28 11:20
 */
public class FontOption implements Serializable {

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
    private String color;

    /**
     * 加粗
     */
    private boolean bold;

    /**
     * 斜体
     */
    private boolean italic;

    /**
     * 删除线
     */
    private boolean delete;

    /**
     * 下滑线
     */
    private ExcelUnderType under;

    /**
     * 调色板索引 HSSFColor
     */
    private short paletteColorIndex;

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public ExcelUnderType getUnder() {
        return under;
    }

    public void setUnder(ExcelUnderType under) {
        this.under = under;
    }

    public short getPaletteColorIndex() {
        return paletteColorIndex;
    }

    public void setPaletteColorIndex(short paletteColorIndex) {
        this.paletteColorIndex = paletteColorIndex;
    }

}

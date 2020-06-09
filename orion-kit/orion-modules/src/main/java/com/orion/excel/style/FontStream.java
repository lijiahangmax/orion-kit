package com.orion.excel.style;

import com.orion.utils.Colors;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FontScheme;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.model.ThemesTable;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

/**
 * Excel 字体样式流
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/23 17:11
 */
public class FontStream {

    private Workbook workbook;

    private Font font;

    public FontStream(Workbook workbook) {
        this.workbook = workbook;
        this.font = workbook.createFont();
    }

    public FontStream(Font font) {
        this.font = font;
    }

    public FontStream(Workbook workbook, Font font) {
        this.workbook = workbook;
        this.font = font;
    }

    /**
     * 获取 FontStream
     *
     * @param workbook Workbook
     * @return FontStream
     */
    public static FontStream fontStream(Workbook workbook) {
        return new FontStream(workbook);
    }

    /**
     * 获取 FontStream
     *
     * @param font Font
     * @return FontStream
     */
    public static FontStream fontStream(Font font) {
        return new FontStream(font);
    }

    /**
     * 获取 FontStream
     *
     * @param workbook Workbook
     * @param font     Font
     * @return FontStream
     */
    public static FontStream fontStream(Workbook workbook, Font font) {
        return new FontStream(workbook, font);
    }

    /**
     * 设置字体名称
     *
     * @param name 名称
     * @return this
     */
    public FontStream name(String name) {
        font.setFontName(name);
        return this;
    }

    /**
     * 设置字体大小
     *
     * @param size size
     * @return this
     */
    public FontStream size(int size) {
        font.setFontHeightInPoints((short) size);
        return this;
    }

    /**
     * 不设置删除线
     *
     * @return this
     */
    public FontStream unsetDeleteLine() {
        font.setStrikeout(false);
        return this;
    }

    /**
     * 设置删除线
     *
     * @return this
     */
    public FontStream deleteLine() {
        font.setStrikeout(true);
        return this;
    }

    /**
     * 不设置下滑线
     *
     * @return this
     */
    public FontStream unsetUnderLine() {
        font.setUnderline((byte) 0);
        return this;
    }

    /**
     * 设置下滑线
     *
     * @return this
     */
    public FontStream underLine() {
        font.setUnderline((byte) 1);
        return this;
    }

    /**
     * 设置双下划线
     *
     * @return this
     */
    public FontStream underDoubleLine() {
        font.setUnderline((byte) 2);
        return this;
    }

    /**
     * 不设置斜体
     *
     * @return this
     */
    public FontStream unsetItalic() {
        font.setItalic(false);
        return this;
    }

    /**
     * 设置斜体
     *
     * @return this
     */
    public FontStream italic() {
        font.setItalic(true);
        return this;
    }

    /**
     * 不设置加粗
     *
     * @return this
     */
    public FontStream unsetBold() {
        font.setBold(false);
        return this;
    }

    /**
     * 设置加粗
     *
     * @return this
     */
    public FontStream bold() {
        font.setBold(true);
        return this;
    }

    /**
     * 不设置偏移
     *
     * @return this
     */
    public FontStream unsetOffset() {
        font.setTypeOffset((short) 0);
        return this;
    }

    /**
     * 设置偏移到顶部
     *
     * @return this
     */
    public FontStream topOffset() {
        font.setTypeOffset((short) 1);
        return this;
    }

    /**
     * 设置偏移到底部
     *
     * @return this
     */
    public FontStream bottomOffset() {
        font.setTypeOffset((short) 2);
        return this;
    }

    /**
     * 设置系统编码
     *
     * @return this
     */
    public FontStream ansiCharset() {
        font.setCharSet(0);
        return this;
    }

    /**
     * 设置默认编码
     *
     * @return this
     */
    public FontStream defaultCharset() {
        font.setCharSet(1);
        return this;
    }

    /**
     * 设置符号编码
     *
     * @return this
     */
    public FontStream symbolCharset() {
        font.setCharSet(2);
        return this;
    }

    /**
     * 设置颜色
     *
     * @return this
     */
    public FontStream color(int c) {
        font.setColor((short) c);
        return this;
    }

    /**
     * 设置颜色
     *
     * @param c rgb
     * @return this
     */
    public FontStream color(byte[] c) {
        if (font instanceof XSSFFont) {
            ((XSSFFont) font).setColor(new XSSFColor(c, null));
        }
        return this;
    }

    /**
     * 设置颜色
     *
     * @param index 调色板颜色覆盖索引
     * @param c     rgb
     * @return this
     */
    public FontStream color(short index, byte[] c) {
        if (workbook instanceof HSSFWorkbook) {
            HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
            HSSFColor color = palette.findColor(c[0], c[1], c[2]);
            if (color == null) {
                palette.setColorAtIndex(index, c[0], c[1], c[2]);
                font.setColor(index);
            } else {
                font.setColor(color.getIndex());
            }
        }
        return this;
    }

    /**
     * 设置颜色
     *
     * @param c #
     * @return this
     */
    public FontStream color(String c) {
        if (font instanceof XSSFFont) {
            ((XSSFFont) font).setColor(new XSSFColor(Colors.toRgb(c), null));
        }
        return this;
    }

    /**
     * 设置颜色
     *
     * @param index 调色板颜色覆盖索引
     * @param c     #
     * @return this
     */
    public FontStream color(short index, String c) {
        return this.color(index, Colors.toRgb(c));
    }

    /**
     * 设置蓝色
     *
     * @return this
     */
    public FontStream blue() {
        font.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        return this;
    }

    /**
     * 设置黑色
     *
     * @return this
     */
    public FontStream black() {
        font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        return this;
    }

    /**
     * 设置红色
     *
     * @return this
     */
    public FontStream red() {
        font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        return this;
    }

    /**
     * 设置粉色
     *
     * @return this
     */
    public FontStream pink() {
        font.setColor(HSSFColor.HSSFColorPredefined.PINK.getIndex());
        return this;
    }

    /**
     * 设置靛青色
     *
     * @return this
     */
    public FontStream indigo() {
        font.setColor(HSSFColor.HSSFColorPredefined.INDIGO.getIndex());
        return this;
    }

    /**
     * 设置字体家族
     *
     * @param i NOT_APPLICABLE 0
     *          ROMAN 1
     *          SWISS 2
     *          MODERN 3
     *          SCRIPT 4
     *          DECORATIVE 5
     * @return this
     */
    public FontStream family(int i) {
        if (font instanceof XSSFFont) {
            ((XSSFFont) font).setFamily(i);
        }
        return this;
    }

    /**
     * 不设置字体家族
     *
     * @return this
     */
    public FontStream unsetFamily() {
        if (font instanceof XSSFFont) {
            ((XSSFFont) font).setFamily(0);
        }
        return this;
    }

    /**
     * 设置属性
     *
     * @param i NONE 1
     *          MAJOR 2
     *          MINOR 3
     * @return this
     */
    public FontStream scheme(int i) {
        if (font instanceof XSSFFont) {
            ((XSSFFont) font).setScheme(FontScheme.valueOf(i));
        }
        return this;
    }

    /**
     * 设置属性
     *
     * @return this
     */
    public FontStream unsetScheme() {
        if (font instanceof XSSFFont) {
            ((XSSFFont) font).setScheme(FontScheme.NONE);
        }
        return this;
    }

    /**
     * 设置主题表格
     *
     * @param table table
     * @return this
     */
    public FontStream themesTable(ThemesTable table) {
        if (font instanceof XSSFFont) {
            ((XSSFFont) font).setThemesTable(table);
        }
        return this;
    }

    /**
     * 设置主题颜色
     *
     * @param i 颜色
     * @return this
     */
    public FontStream themesColor(int i) {
        if (font instanceof XSSFFont) {
            ((XSSFFont) font).setThemeColor((short) i);
        }
        return this;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Font getFont() {
        return font;
    }

    /**
     * 获取字体大小
     *
     * @return 字体大小
     */
    public int getFontSize() {
        return font.getFontHeightInPoints();
    }

    /**
     * 获取字体名称
     *
     * @return 字体名称
     */
    public String getFontName() {
        return font.getFontName();
    }

    /**
     * 是否为加粗字体
     *
     * @return true加粗
     */
    public boolean getBold() {
        return font.getBold();
    }

    /**
     * 是否为斜体
     *
     * @return true斜体
     */
    public boolean getItalic() {
        return font.getItalic();
    }

    /**
     * 获取下滑线类型
     *
     * @return 0无 1单下滑线 2双下滑线
     */
    public byte getUnderLineType() {
        return font.getUnderline();
    }

    /**
     * 获取编码格式
     *
     * @return 0系统编码 1默认编码 2符号编码
     */
    public int getCharset() {
        return font.getCharSet();
    }

    /**
     * 获取偏移类型
     *
     * @return 0不偏移 1上对齐 2下对齐
     */
    public short getOffsetType() {
        return font.getTypeOffset();
    }

    /**
     * 获取颜色编码
     *
     * @return 颜色编码
     */
    public short getColor() {
        return font.getColor();
    }

    /**
     * 获取字体家族
     *
     * @return family
     */
    public int getFamily() {
        if (font instanceof XSSFFont) {
            return ((XSSFFont) font).getFamily();
        }
        return -1;
    }

    /**
     * 获取属性
     *
     * @return scheme
     */
    public int getScheme() {
        if (font instanceof XSSFFont) {
            return ((XSSFFont) font).getScheme().getValue();
        }
        return -1;
    }

    /**
     * 获取主题颜色
     *
     * @return color
     */
    public int getThemeColor() {
        if (font instanceof XSSFFont) {
            return ((XSSFFont) font).getThemeColor();
        }
        return -1;
    }

}

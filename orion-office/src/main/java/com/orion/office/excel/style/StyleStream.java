package com.orion.office.excel.style;

import com.orion.office.excel.Excels;
import com.orion.office.excel.option.ExportFieldOption;
import com.orion.office.excel.option.TitleOption;
import com.orion.office.excel.type.ExcelAlignType;
import com.orion.office.excel.type.ExcelVerticalAlignType;
import com.orion.utils.Colors;
import com.orion.utils.Strings;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel 单元格样式流
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/23 18:28
 */
public class StyleStream {

    private Workbook workbook;

    private CellStyle style;

    public StyleStream(Workbook workbook) {
        this.workbook = workbook;
        this.style = workbook.createCellStyle();
    }

    public StyleStream(Workbook workbook, CellStyle style) {
        this.workbook = workbook;
        this.style = style;
    }

    /**
     * 获取 StyleStream
     *
     * @param workbook Workbook
     * @return StyleStream
     */
    public static StyleStream styleStream(Workbook workbook) {
        return new StyleStream(workbook);
    }

    /**
     * 获取 StyleStream
     *
     * @param workbook Workbook
     * @param style    CellStyle
     * @return StyleStream
     */
    public static StyleStream styleStream(Workbook workbook, CellStyle style) {
        return new StyleStream(workbook, style);
    }

    /**
     * 解析样式
     *
     * @param workbook workbook
     * @param option   option
     */
    public static CellStyle parseStyle(Workbook workbook, ExportFieldOption option) {
        if (option == null) {
            return null;
        }
        StyleStream stream = new StyleStream(workbook);
        // 对齐
        if (option.getAlign() != null && !option.getAlign().equals(ExcelAlignType.DEFAULT)) {
            stream.alignment(option.getAlign().getCode());
        }
        if (option.getVerticalAlign() != null && !option.getVerticalAlign().equals(ExcelVerticalAlignType.DEFAULT)) {
            stream.verticalAlignment(option.getVerticalAlign().getCode());
        }
        stream.wrapText(option.isWrapText());
        stream.lock(option.isLock());
        stream.autoResize(option.isAutoResize());
        // 背景色
        String backgroundColor = option.getBackgroundColor();
        if (!Strings.isEmpty(backgroundColor)) {
            stream.texture(FillPatternType.SOLID_FOREGROUND.getCode());
            if (workbook instanceof XSSFWorkbook || workbook instanceof SXSSFWorkbook) {
                stream.textureColor(backgroundColor);
            } else if (workbook instanceof HSSFWorkbook) {
                short paletteColorIndex = option.getPaletteColorIndex();
                short usePaletteColorIndex = Excels.paletteColor(workbook, paletteColorIndex, Colors.toRgb(backgroundColor));
                stream.textureColor(usePaletteColorIndex);
                if (paletteColorIndex == usePaletteColorIndex) {
                    option.setPaletteColorIndex(++paletteColorIndex);
                }
            }
        }
        // 边框
        if (option.getBorder() != null) {
            stream.border(option.getBorder().getCode());
            String borderColor = option.getBorderColor();
            if (!Strings.isEmpty(borderColor)) {
                if (workbook instanceof XSSFWorkbook || workbook instanceof SXSSFWorkbook) {
                    stream.borderColor(borderColor);
                } else if (workbook instanceof HSSFWorkbook) {
                    short paletteColorIndex = option.getPaletteColorIndex();
                    short usePaletteColorIndex = Excels.paletteColor(workbook, paletteColorIndex, Colors.toRgb(borderColor));
                    stream.borderColor(usePaletteColorIndex);
                    if (paletteColorIndex == usePaletteColorIndex) {
                        option.setPaletteColorIndex(++paletteColorIndex);
                    }
                }
            }
        }
        return stream.getStyle();
    }

    /**
     * 解析列样式
     *
     * @param workbook workbook
     * @param option   option
     */
    public static CellStyle parseColumnStyle(Workbook workbook, ExportFieldOption option) {
        CellStyle style = parseStyle(workbook, option);
        if (style == null) {
            return null;
        }
        if (!Strings.isEmpty(option.getFormat())) {
            style.setDataFormat(workbook.createDataFormat().getFormat(option.getFormat()));
        }
        if (option.getIndent() != null) {
            style.setIndention(option.getIndent());
        }
        if (option.isQuotePrefixed()) {
            style.setQuotePrefixed(true);
        }
        return style;
    }

    /**
     * 解析标题样式
     *
     * @param workbook workbook
     * @param option   option
     */
    public static CellStyle parseTitleStyle(Workbook workbook, TitleOption option) {
        if (option == null) {
            return null;
        }
        // convert
        ExportFieldOption exportFieldOption = new ExportFieldOption();
        exportFieldOption.setAlign(option.getAlign());
        exportFieldOption.setVerticalAlign(option.getVerticalAlign());
        exportFieldOption.setBackgroundColor(option.getBackgroundColor());
        exportFieldOption.setBorder(option.getBorder());
        exportFieldOption.setBorderColor(option.getBorderColor());
        exportFieldOption.setPaletteColorIndex(option.getPaletteColorIndex());
        CellStyle cellStyle = parseStyle(workbook, exportFieldOption);
        option.setPaletteColorIndex(exportFieldOption.getPaletteColorIndex());
        return cellStyle;
    }

    /**
     * 设置字体
     *
     * @return this
     */
    public StyleStream font(Font font) {
        style.setFont(font);
        return this;
    }

    /**
     * 设置字体
     *
     * @return this
     */
    public StyleStream font(FontStream font) {
        style.setFont(font.getFont());
        return this;
    }

    /**
     * 设置自动换行
     *
     * @return this
     */
    public StyleStream wrapText() {
        style.setWrapText(true);
        return this;
    }

    /**
     * 不设置自动换行
     *
     * @return this
     */
    public StyleStream unsetWrapText() {
        style.setWrapText(false);
        return this;
    }

    /**
     * 设置自动换行
     *
     * @param wrap 是否自动换行
     * @return this
     */
    public StyleStream wrapText(boolean wrap) {
        style.setWrapText(wrap);
        return this;
    }

    /**
     * 设置水平默认对齐
     *
     * @return this
     */
    public StyleStream alignment(int code) {
        style.setAlignment(HorizontalAlignment.forInt(code));
        return this;
    }

    /**
     * 设置水平默认对齐
     *
     * @return this
     */
    public StyleStream defaultAlignment() {
        style.setAlignment(HorizontalAlignment.forInt(0));
        return this;
    }

    /**
     * 设置水平左对齐
     *
     * @return this
     */
    public StyleStream leftAlignment() {
        style.setAlignment(HorizontalAlignment.forInt(1));
        return this;
    }

    /**
     * 设置水平居中对齐
     *
     * @return this
     */
    public StyleStream centerAlignment() {
        style.setAlignment(HorizontalAlignment.forInt(2));
        return this;
    }

    /**
     * 设置水平右对齐
     *
     * @return this
     */
    public StyleStream rightAlignment() {
        style.setAlignment(HorizontalAlignment.forInt(3));
        return this;
    }

    /**
     * 设置水平两端对齐
     *
     * @return this
     */
    public StyleStream fillAlignment() {
        style.setAlignment(HorizontalAlignment.forInt(5));
        return this;
    }

    /**
     * 设置垂直对齐
     *
     * @param code code
     * @return this
     */
    public StyleStream verticalAlignment(int code) {
        style.setVerticalAlignment(VerticalAlignment.forInt(code));
        return this;
    }

    /**
     * 设置垂直上对齐
     *
     * @return this
     */
    public StyleStream topVerticalAlignment() {
        style.setVerticalAlignment(VerticalAlignment.forInt(0));
        return this;
    }

    /**
     * 设置垂直居中对齐
     *
     * @return this
     */
    public StyleStream centerVerticalAlignment() {
        style.setVerticalAlignment(VerticalAlignment.forInt(1));
        return this;
    }

    /**
     * 设置垂直下对齐
     *
     * @return this
     */
    public StyleStream bottomVerticalAlignment() {
        style.setVerticalAlignment(VerticalAlignment.forInt(2));
        return this;
    }

    /**
     * 设置锁定
     *
     * @return this
     */
    public StyleStream lock() {
        style.setLocked(true);
        return this;
    }

    /**
     * 设置不锁定
     *
     * @return this
     */
    public StyleStream unlock() {
        style.setLocked(false);
        return this;
    }

    /**
     * 设置锁定
     *
     * @param lock 是否锁定
     * @return this
     */
    public StyleStream lock(boolean lock) {
        style.setLocked(lock);
        return this;
    }

    /**
     * 设置隐藏
     *
     * @return this
     */
    public StyleStream hidden() {
        style.setHidden(true);
        return this;
    }

    /**
     * 设置隐藏
     *
     * @param hidden 是否隐藏
     * @return this
     */
    public StyleStream hidden(boolean hidden) {
        style.setHidden(hidden);
        return this;
    }

    /**
     * 设置显示
     *
     * @return this
     */
    public StyleStream show() {
        style.setHidden(false);
        return this;
    }

    /**
     * 设置显示
     *
     * @param show 是否显示
     * @return this
     */
    public StyleStream show(boolean show) {
        style.setHidden(show);
        return this;
    }

    /**
     * 设置缩进空格
     *
     * @param i 缩进空格数量
     * @return this
     */
    public StyleStream indention(int i) {
        style.setIndention((short) i);
        return this;
    }

    /**
     * 设置旋转角度
     *
     * @param i 角度
     * @return this
     */
    public StyleStream rotation(int i) {
        style.setRotation((short) i);
        return this;
    }

    /**
     * 设置公式前缀
     *
     * @return this
     */
    public StyleStream quotePrefixed() {
        style.setQuotePrefixed(true);
        return this;
    }

    /**
     * 不设置公式前缀
     *
     * @return this
     */
    public StyleStream unsetQuotePrefixed() {
        style.setQuotePrefixed(false);
        return this;
    }

    /**
     * 设置公式前缀
     *
     * @param set 是否设置
     * @return this
     */
    public StyleStream quotePrefixed(boolean set) {
        style.setQuotePrefixed(set);
        return this;
    }

    /**
     * 设置自动调整大小
     *
     * @return this
     */
    public StyleStream autoResize() {
        style.setShrinkToFit(true);
        return this;
    }

    /**
     * 不设置自动调整大小
     *
     * @return this
     */
    public StyleStream unsetAutoResize() {
        style.setShrinkToFit(false);
        return this;
    }

    /**
     * 设置自动调整大小
     *
     * @return this
     */
    public StyleStream autoResize(boolean set) {
        style.setShrinkToFit(set);
        return this;
    }

    /**
     * 设置数据格式化
     *
     * @param index index
     * @return this
     */
    public StyleStream dataFormat(int index) {
        style.setDataFormat((short) index);
        return this;
    }

    /**
     * 设置数据格式化
     *
     * @param format format
     * @return this
     */
    public StyleStream dataFormat(String format) {
        style.setDataFormat(workbook.createDataFormat().getFormat(format));
        return this;
    }

    /**
     * 不设置读顺序(默认)
     *
     * @return this
     */
    public StyleStream unsetReadingOrder() {
        if (style instanceof XSSFCellStyle) {
            // 设置顺序
            ((XSSFCellStyle) style).setReadingOrder(ReadingOrder.CONTEXT);
        }
        return this;
    }

    /**
     * 设置读顺序为从左到右
     *
     * @return this
     */
    public StyleStream leftReading() {
        if (style instanceof XSSFCellStyle) {
            // 设置顺序
            ((XSSFCellStyle) style).setReadingOrder(ReadingOrder.LEFT_TO_RIGHT);
        }
        return this;
    }

    /**
     * 设置读顺序为从右到左
     *
     * @return this
     */
    public StyleStream rightReading() {
        if (style instanceof XSSFCellStyle) {
            // 设置顺序
            ((XSSFCellStyle) style).setReadingOrder(ReadingOrder.RIGHT_TO_LEFT);
        }
        return this;
    }

    /**
     * 不设置纹理
     *
     * @return this
     */
    public StyleStream unsetTexture() {
        style.setFillPattern(FillPatternType.forInt(0));
        return this;
    }

    /**
     * 设置背景纹理
     *
     * @param i 纹理样式
     * @return this
     */
    public StyleStream texture(int i) {
        style.setFillPattern(FillPatternType.forInt(i));
        return this;
    }

    /**
     * 设置纹理前景色
     *
     * @return this
     */
    public StyleStream textureColor(int i) {
        style.setFillForegroundColor((short) i);
        return this;
    }

    /**
     * 设置纹理前景色
     *
     * @param rgb 颜色
     * @return this
     */
    public StyleStream textureColor(byte[] rgb) {
        if (style instanceof XSSFCellStyle) {
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(rgb, null));
        }
        return this;
    }

    /**
     * 设置纹理前景色
     *
     * @param c 颜色
     * @return this
     */
    public StyleStream textureColor(String c) {
        if (style instanceof XSSFCellStyle) {
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(Colors.toRgb(c), null));
        }
        return this;
    }

    /**
     * 设置纹理前景色
     *
     * @param index 覆盖索引
     * @param rgb   颜色
     * @return this
     */
    public StyleStream textureColor(short index, byte[] rgb) {
        if (workbook instanceof HSSFWorkbook) {
            style.setFillForegroundColor(Excels.paletteColor(workbook, index, rgb));
        }
        return this;
    }

    /**
     * 设置纹理前景色
     *
     * @param index 覆盖索引
     * @param c     颜色
     * @return this
     */
    public StyleStream textureColor(short index, String c) {
        if (workbook instanceof HSSFWorkbook) {
            style.setFillForegroundColor(Excels.paletteColor(workbook, index, c));
        }
        return this;
    }

    /**
     * 设置纹理前景色为蓝色
     *
     * @return this
     */
    public StyleStream blueTextureColor() {
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        return this;
    }

    /**
     * 设置纹理前景色为黑色
     *
     * @return this
     */
    public StyleStream blackTextureColor() {
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        return this;
    }

    /**
     * 设置纹理前景色为红色
     *
     * @return this
     */
    public StyleStream redTextureColor() {
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        return this;
    }

    /**
     * 设置纹理前景色为粉色
     *
     * @return this
     */
    public StyleStream pinkTextureColor() {
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.PINK.getIndex());
        return this;
    }

    /**
     * 设置纹理前景色为靛青色
     *
     * @return this
     */
    public StyleStream indigoTextureColor() {
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.INDIGO.getIndex());
        return this;
    }

    /**
     * 设置纹理背景色
     *
     * @param i 颜色
     * @return this
     */
    public StyleStream textureBackgroundColor(int i) {
        style.setFillBackgroundColor((short) i);
        return this;
    }

    /**
     * 设置纹理背景色
     *
     * @param rgb 颜色
     * @return this
     */
    public StyleStream textureBackgroundColor(byte[] rgb) {
        if (style instanceof XSSFCellStyle) {
            ((XSSFCellStyle) style).setFillBackgroundColor(new XSSFColor(rgb, null));
        }
        return this;
    }

    /**
     * 设置纹理背景色
     *
     * @param c 颜色
     * @return this
     */
    public StyleStream textureBackgroundColor(String c) {
        if (style instanceof XSSFCellStyle) {
            ((XSSFCellStyle) style).setFillBackgroundColor(new XSSFColor(Colors.toRgb(c), null));
        }
        return this;
    }

    /**
     * 设置纹理背景色
     *
     * @param index 覆盖索引
     * @param rgb   颜色
     * @return this
     */
    public StyleStream textureBackgroundColor(short index, byte[] rgb) {
        if (workbook instanceof HSSFWorkbook) {
            style.setFillBackgroundColor(Excels.paletteColor(workbook, index, rgb));
        }
        return this;
    }

    /**
     * 设置纹理背景色
     *
     * @param index 覆盖索引
     * @param c     颜色
     * @return this
     */
    public StyleStream textureBackgroundColor(short index, String c) {
        if (workbook instanceof HSSFWorkbook) {
            style.setFillBackgroundColor(Excels.paletteColor(workbook, index, c));
        }
        return this;
    }

    /**
     * 设置纹理背景色为蓝色
     *
     * @return this
     */
    public StyleStream blueTextureBackgroundColor() {
        style.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        return this;
    }

    /**
     * 设置纹理背景色为黑色
     *
     * @return this
     */
    public StyleStream blackTextureBackgroundColor() {
        style.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        return this;
    }

    /**
     * 设置纹理背景色为红色
     *
     * @return this
     */
    public StyleStream redTextureBackgroundColor() {
        style.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        return this;
    }

    /**
     * 设置纹理背景色为粉色
     *
     * @return this
     */
    public StyleStream pinkTextureBackgroundColor() {
        style.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.PINK.getIndex());
        return this;
    }

    /**
     * 设置纹理背景色为靛青色
     *
     * @return this
     */
    public StyleStream indigoTextureBackgroundColor() {
        style.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.INDIGO.getIndex());
        return this;
    }

    /**
     * 使用默认边框
     *
     * @return this
     */
    public StyleStream unsetBorder() {
        return this.border(0);
    }

    /**
     * 使用实线边框
     *
     * @return this
     */
    public StyleStream solidBorder() {
        return this.border(1);
    }

    /**
     * 使用虚线边框
     *
     * @return this
     */
    public StyleStream dashedBorder() {
        return this.border(3);
    }

    /**
     * 设置边框类型
     *
     * @param i 边框类型
     * @return this
     */
    public StyleStream border(int i) {
        style.setBorderTop(BorderStyle.valueOf((short) i));
        style.setBorderLeft(BorderStyle.valueOf((short) i));
        style.setBorderBottom(BorderStyle.valueOf((short) i));
        style.setBorderRight(BorderStyle.valueOf((short) i));
        return this;
    }

    /**
     * 设置边框颜色
     *
     * @param i 颜色
     * @return this
     */
    public StyleStream borderColor(int i) {
        style.setTopBorderColor((short) i);
        style.setLeftBorderColor((short) i);
        style.setBottomBorderColor((short) i);
        style.setRightBorderColor((short) i);
        return this;
    }

    /**
     * 设置边框颜色
     *
     * @param rgb 颜色
     * @return this
     */
    public StyleStream borderColor(byte[] rgb) {
        if (style instanceof XSSFCellStyle) {
            XSSFColor color = new XSSFColor(rgb, null);
            ((XSSFCellStyle) style).setTopBorderColor(color);
            ((XSSFCellStyle) style).setLeftBorderColor(color);
            ((XSSFCellStyle) style).setBottomBorderColor(color);
            ((XSSFCellStyle) style).setRightBorderColor(color);
        }
        return this;
    }

    /**
     * 设置边框颜色
     *
     * @param c 颜色
     * @return this
     */
    public StyleStream borderColor(String c) {
        if (style instanceof XSSFCellStyle) {
            XSSFColor color = new XSSFColor(Colors.toColor(c), null);
            ((XSSFCellStyle) style).setTopBorderColor(color);
            ((XSSFCellStyle) style).setLeftBorderColor(color);
            ((XSSFCellStyle) style).setBottomBorderColor(color);
            ((XSSFCellStyle) style).setRightBorderColor(color);
        }
        return this;
    }

    /**
     * 设置边框颜色
     *
     * @param index 覆盖索引
     * @param rgb   颜色
     * @return this
     */
    public StyleStream borderColor(short index, byte[] rgb) {
        if (workbook instanceof HSSFWorkbook) {
            short ci = Excels.paletteColor(workbook, index, rgb);
            style.setTopBorderColor(ci);
            style.setLeftBorderColor(ci);
            style.setBottomBorderColor(ci);
            style.setRightBorderColor(ci);
        }
        return this;
    }

    /**
     * 设置边框颜色
     *
     * @param index 覆盖索引
     * @param c     颜色
     * @return this
     */
    public StyleStream borderColor(short index, String c) {
        if (workbook instanceof HSSFWorkbook) {
            short ci = Excels.paletteColor(workbook, index, c);
            style.setTopBorderColor(ci);
            style.setLeftBorderColor(ci);
            style.setBottomBorderColor(ci);
            style.setRightBorderColor(ci);
        }
        return this;
    }

    /**
     * 设置边框颜色为蓝色
     *
     * @return this
     */
    public StyleStream blueBorderColor() {
        return this.borderColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
    }

    /**
     * 设置边框颜色为黑色
     *
     * @return this
     */
    public StyleStream blackBorderColor() {
        return this.borderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
    }

    /**
     * 设置边框颜色为红色
     *
     * @return this
     */
    public StyleStream redBorderColor() {
        return this.borderColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
    }

    /**
     * 设置边框颜色为粉色
     *
     * @return this
     */
    public StyleStream pinkBorderColor() {
        return this.borderColor(HSSFColor.HSSFColorPredefined.PINK.getIndex());
    }

    /**
     * 设置边框颜色为靛青色
     *
     * @return this
     */
    public StyleStream indigoBorderColor() {
        return this.borderColor(HSSFColor.HSSFColorPredefined.INDIGO.getIndex());
    }

    /**
     * 使用默认上边框
     *
     * @return this
     */
    public StyleStream unsetTopBorder() {
        return this.topBorder(0);
    }

    /**
     * 使用实线上边框
     *
     * @return this
     */
    public StyleStream solidTopBorder() {
        return this.topBorder(1);
    }

    /**
     * 使用虚线上边框
     *
     * @return this
     */
    public StyleStream dashedTopBorder() {
        return this.topBorder(3);
    }

    /**
     * 设置上边框类型
     *
     * @param i 边框类型
     * @return this
     */
    public StyleStream topBorder(int i) {
        style.setBorderTop(BorderStyle.valueOf((short) i));
        return this;
    }

    /**
     * 设置上边框颜色
     *
     * @param i 颜色
     * @return this
     */
    public StyleStream topBorderColor(int i) {
        style.setTopBorderColor((short) i);
        return this;
    }

    /**
     * 设置上边框颜色
     *
     * @param rgb 颜色
     * @return this
     */
    public StyleStream topBorderColor(byte[] rgb) {
        if (style instanceof XSSFCellStyle) {
            ((XSSFCellStyle) style).setTopBorderColor(new XSSFColor(rgb, null));
        }
        return this;
    }

    /**
     * 设置上边框颜色
     *
     * @param c 颜色
     * @return this
     */
    public StyleStream topBorderColor(String c) {
        if (style instanceof XSSFCellStyle) {
            ((XSSFCellStyle) style).setTopBorderColor(new XSSFColor(Colors.toRgb(c), null));
        }
        return this;
    }

    /**
     * 设置上边框颜色
     *
     * @param index 覆盖索引
     * @param rgb   颜色
     * @return this
     */
    public StyleStream topBorderColor(short index, byte[] rgb) {
        if (workbook instanceof HSSFWorkbook) {
            style.setTopBorderColor(Excels.paletteColor(workbook, index, rgb));
        }
        return this;
    }

    /**
     * 设置上边框颜色
     *
     * @param index 覆盖索引
     * @param c     颜色
     * @return this
     */
    public StyleStream topBorderColor(short index, String c) {
        if (workbook instanceof HSSFWorkbook) {
            style.setTopBorderColor(Excels.paletteColor(workbook, index, c));
        }
        return this;
    }

    /**
     * 设置上边框颜色为蓝色
     *
     * @return this
     */
    public StyleStream blueTopBorderColor() {
        return this.topBorderColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
    }

    /**
     * 设置上边框颜色为黑色
     *
     * @return this
     */
    public StyleStream blackTopBorderColor() {
        return this.topBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
    }

    /**
     * 设置上边框颜色为红色
     *
     * @return this
     */
    public StyleStream redTopBorderColor() {
        return this.topBorderColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
    }

    /**
     * 设置上边框颜色为粉色
     *
     * @return this
     */
    public StyleStream pinkTopBorderColor() {
        return this.topBorderColor(HSSFColor.HSSFColorPredefined.PINK.getIndex());
    }

    /**
     * 设置上边框颜色为靛青色
     *
     * @return this
     */
    public StyleStream indigoTopBorderColor() {
        return this.topBorderColor(HSSFColor.HSSFColorPredefined.INDIGO.getIndex());
    }

    /**
     * 使用默认下边框
     *
     * @return this
     */
    public StyleStream unsetBottomBorder() {
        return this.bottomBorder(0);
    }

    /**
     * 使用实线下边框
     *
     * @return this
     */
    public StyleStream solidBottomBorder() {
        return this.bottomBorder(1);
    }

    /**
     * 使用虚线下边框
     *
     * @return this
     */
    public StyleStream dashedBottomBorder() {
        return this.bottomBorder(3);
    }

    /**
     * 设置下边框类型
     *
     * @param i 边框类型
     * @return this
     */
    public StyleStream bottomBorder(int i) {
        style.setBorderBottom(BorderStyle.valueOf((short) i));
        return this;
    }

    /**
     * 设置下边框颜色
     *
     * @param i 颜色
     * @return this
     */
    public StyleStream bottomBorderColor(int i) {
        style.setBottomBorderColor((short) i);
        return this;
    }

    /**
     * 设置下边框颜色
     *
     * @param rgb 颜色
     * @return this
     */
    public StyleStream bottomBorderColor(byte[] rgb) {
        if (style instanceof XSSFCellStyle) {
            ((XSSFCellStyle) style).setBottomBorderColor(new XSSFColor(rgb, null));
        }
        return this;
    }

    /**
     * 设置下边框颜色
     *
     * @param c 颜色
     * @return this
     */
    public StyleStream bottomBorderColor(String c) {
        if (style instanceof XSSFCellStyle) {
            ((XSSFCellStyle) style).setBottomBorderColor(new XSSFColor(Colors.toRgb(c), null));
        }
        return this;
    }

    /**
     * 设置下边框颜色
     *
     * @param index 覆盖索引
     * @param rgb   颜色
     * @return this
     */
    public StyleStream bottomBorderColor(short index, byte[] rgb) {
        if (workbook instanceof HSSFWorkbook) {
            style.setBottomBorderColor(Excels.paletteColor(workbook, index, rgb));
        }
        return this;
    }

    /**
     * 设置下边框颜色
     *
     * @param index 覆盖索引
     * @param c     颜色
     * @return this
     */
    public StyleStream bottomBorderColor(short index, String c) {
        if (workbook instanceof HSSFWorkbook) {
            style.setBottomBorderColor(Excels.paletteColor(workbook, index, c));
        }
        return this;
    }

    /**
     * 设置下边框颜色为蓝色
     *
     * @return this
     */
    public StyleStream blueBottomBorderColor() {
        return this.bottomBorderColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
    }

    /**
     * 设置下边框颜色为黑色
     *
     * @return this
     */
    public StyleStream blackBottomBorderColor() {
        return this.bottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
    }

    /**
     * 设置下边框颜色为红色
     *
     * @return this
     */
    public StyleStream redBottomBorderColor() {
        return this.bottomBorderColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
    }

    /**
     * 设置下边框颜色为粉色
     *
     * @return this
     */
    public StyleStream pinkBottomBorderColor() {
        return this.bottomBorderColor(HSSFColor.HSSFColorPredefined.PINK.getIndex());
    }

    /**
     * 设置下边框颜色为靛青色
     *
     * @return this
     */
    public StyleStream indigoBottomBorderColor() {
        return this.bottomBorderColor(HSSFColor.HSSFColorPredefined.INDIGO.getIndex());
    }

    /**
     * 使用默认左边框
     *
     * @return this
     */
    public StyleStream unsetLeftBorder() {
        return this.leftBorder(0);
    }

    /**
     * 使用实线左边框
     *
     * @return this
     */
    public StyleStream solidLeftBorder() {
        return this.leftBorder(1);
    }

    /**
     * 使用虚线左边框
     *
     * @return this
     */
    public StyleStream dashedLeftBorder() {
        return this.leftBorder(3);
    }

    /**
     * 设置左边框类型
     *
     * @param i 边框类型
     * @return this
     */
    public StyleStream leftBorder(int i) {
        style.setBorderLeft(BorderStyle.valueOf((short) i));
        return this;
    }

    /**
     * 设置左边框颜色
     *
     * @param i 颜色
     * @return this
     */
    public StyleStream leftBorderColor(int i) {
        style.setLeftBorderColor((short) i);
        return this;
    }

    /**
     * 设置左边框颜色
     *
     * @param rgb 颜色
     * @return this
     */
    public StyleStream leftBorderColor(byte[] rgb) {
        if (style instanceof XSSFCellStyle) {
            ((XSSFCellStyle) style).setLeftBorderColor(new XSSFColor(rgb, null));
        }
        return this;
    }

    /**
     * 设置左边框颜色
     *
     * @param c 颜色
     * @return this
     */
    public StyleStream leftBorderColor(String c) {
        if (style instanceof XSSFCellStyle) {
            ((XSSFCellStyle) style).setLeftBorderColor(new XSSFColor(Colors.toRgb(c), null));
        }
        return this;
    }

    /**
     * 设置左边框颜色
     *
     * @param index 覆盖索引
     * @param rgb   颜色
     * @return this
     */
    public StyleStream leftBorderColor(short index, byte[] rgb) {
        if (workbook instanceof HSSFWorkbook) {
            style.setLeftBorderColor(Excels.paletteColor(workbook, index, rgb));
        }
        return this;
    }

    /**
     * 设置左边框颜色
     *
     * @param index 覆盖索引
     * @param c     颜色
     * @return this
     */
    public StyleStream leftBorderColor(short index, String c) {
        if (workbook instanceof HSSFWorkbook) {
            style.setLeftBorderColor(Excels.paletteColor(workbook, index, c));
        }
        return this;
    }

    /**
     * 设置左边框颜色为蓝色
     *
     * @return this
     */
    public StyleStream blueLeftBorderColor() {
        return this.leftBorderColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
    }

    /**
     * 设置左边框颜色为黑色
     *
     * @return this
     */
    public StyleStream blackLeftBorderColor() {
        return this.leftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
    }

    /**
     * 设置左边框颜色为红色
     *
     * @return this
     */
    public StyleStream redLeftBorderColor() {
        return this.leftBorderColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
    }

    /**
     * 设置左边框颜色为粉色
     *
     * @return this
     */
    public StyleStream pinkLeftBorderColor() {
        return this.leftBorderColor(HSSFColor.HSSFColorPredefined.PINK.getIndex());
    }

    /**
     * 设置左边框颜色为靛青色
     *
     * @return this
     */
    public StyleStream indigoLeftBorderColor() {
        return this.leftBorderColor(HSSFColor.HSSFColorPredefined.INDIGO.getIndex());
    }

    /**
     * 使用默认右边框
     *
     * @return this
     */
    public StyleStream unsetRightBorder() {
        return this.rightBorder(0);
    }

    /**
     * 使用实线右边框
     *
     * @return this
     */
    public StyleStream solidRightBorder() {
        return this.rightBorder(1);
    }

    /**
     * 使用虚线右边框
     *
     * @return this
     */
    public StyleStream dashedRightBorder() {
        return this.rightBorder(3);
    }

    /**
     * 设置右边框类型
     *
     * @param i 边框类型
     * @return this
     */
    public StyleStream rightBorder(int i) {
        style.setBorderRight(BorderStyle.valueOf((short) i));
        return this;
    }

    /**
     * 设置右边框颜色
     *
     * @param i 颜色
     * @return this
     */
    public StyleStream rightBorderColor(int i) {
        style.setRightBorderColor((short) i);
        return this;
    }

    /**
     * 设置右边框颜色
     *
     * @param rgb 颜色
     * @return this
     */
    public StyleStream rightBorderColor(byte[] rgb) {
        if (style instanceof XSSFCellStyle) {
            ((XSSFCellStyle) style).setRightBorderColor(new XSSFColor(rgb, null));
        }
        return this;
    }

    /**
     * 设置右边框颜色
     *
     * @param c 颜色
     * @return this
     */
    public StyleStream rightBorderColor(String c) {
        if (style instanceof XSSFCellStyle) {
            ((XSSFCellStyle) style).setRightBorderColor(new XSSFColor(Colors.toRgb(c), null));
        }
        return this;
    }

    /**
     * 设置右边框颜色
     *
     * @param index 覆盖索引
     * @param rgb   颜色
     * @return this
     */
    public StyleStream rightBorderColor(short index, byte[] rgb) {
        if (workbook instanceof HSSFWorkbook) {
            style.setRightBorderColor(Excels.paletteColor(workbook, index, rgb));
        }
        return this;
    }

    /**
     * 设置右边框颜色
     *
     * @param index 覆盖索引
     * @param c     颜色
     * @return this
     */
    public StyleStream rightBorderColor(short index, String c) {
        if (workbook instanceof HSSFWorkbook) {
            style.setRightBorderColor(Excels.paletteColor(workbook, index, c));
        }
        return this;
    }

    /**
     * 设置右边框颜色为蓝色
     *
     * @return this
     */
    public StyleStream blueRightBorderColor() {
        return this.rightBorderColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
    }

    /**
     * 设置右边框颜色为黑色
     *
     * @return this
     */
    public StyleStream blackRightBorderColor() {
        return this.rightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
    }

    /**
     * 设置右边框颜色为红色
     *
     * @return this
     */
    public StyleStream redRightBorderColor() {
        return this.rightBorderColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
    }

    /**
     * 设置右边框颜色为粉色
     *
     * @return this
     */
    public StyleStream pinkRightBorderColor() {
        return this.rightBorderColor(HSSFColor.HSSFColorPredefined.PINK.getIndex());
    }

    /**
     * 设置右边框颜色为靛青色
     *
     * @return this
     */
    public StyleStream indigoRightBorderColor() {
        return this.rightBorderColor(HSSFColor.HSSFColorPredefined.INDIGO.getIndex());
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public CellStyle getStyle() {
        return style;
    }

    /**
     * 是否自动换行
     *
     * @return true自动换行
     */
    public boolean isWrapText() {
        return style.getWrapText();
    }

    /**
     * 获取垂直对齐方式
     *
     * @return 0上 1中 2下
     */
    public int getVerticalAlignmentType() {
        return style.getVerticalAlignment().getCode();
    }

    /**
     * 获取水平对齐方式
     *
     * @return 0默认 1左对齐 2居中 3右对齐 5两端对齐
     */
    public int getAlignment() {
        return style.getAlignment().getCode();
    }

    /**
     * 获取旋转角度
     *
     * @return 旋转角度
     */
    public int getRotation() {
        return style.getRotation();
    }

    /**
     * 获取style索引
     *
     * @return style索引
     */
    public short getIndex() {
        return style.getIndex();
    }

    /**
     * 获取格式索引
     *
     * @return 格式索引
     */
    public int getDataFormat() {
        return style.getDataFormat();
    }

    /**
     * 获取空格缩进大小
     *
     * @return 空格缩进大小
     */
    public int getIndention() {
        return style.getIndention();
    }

    /**
     * 获取单元格是否锁定样式
     *
     * @return true锁定样式
     */
    public boolean isLocked() {
        return style.getLocked();
    }

    /**
     * 获取纹理类型
     *
     * @return ignore
     */
    public int getFillPattern() {
        return style.getFillPattern().getCode();
    }

    /**
     * 获取纹理颜色
     *
     * @return 纹理颜色
     */
    public int getFillForegroundColor() {
        return style.getFillForegroundColor();
    }

    /**
     * 获取纹理背景色
     *
     * @return 纹理背景色
     */
    public int getFillBackgroundColor() {
        return style.getFillBackgroundColor();
    }

    /**
     * 获取左边框类型
     *
     * @return ignore
     */
    public int getBorderLeftType() {
        return style.getBorderLeft().getCode();
    }

    /**
     * 获取右边框类型
     *
     * @return ignore
     */
    public int getBorderRightType() {
        return style.getBorderRight().getCode();
    }

    /**
     * 获取上边框类型
     *
     * @return ignore
     */
    public int getBorderTopType() {
        return style.getBorderTop().getCode();
    }

    /**
     * 获取下边框类型
     *
     * @return ignore
     */
    public int getBorderBottomType() {
        return style.getBorderBottom().getCode();
    }

    /**
     * 获取左边框颜色
     *
     * @return 颜色
     */
    public short getBorderLeftColor() {
        return style.getLeftBorderColor();
    }

    /**
     * 获取右边框颜色
     *
     * @return 颜色
     */
    public short getBorderRightColor() {
        return style.getRightBorderColor();
    }

    /**
     * 获取上边框颜色
     *
     * @return 颜色
     */
    public short getBorderTopColor() {
        return style.getTopBorderColor();
    }

    /**
     * 获取下边框颜色
     *
     * @return 颜色
     */
    public short getBorderBottomColor() {
        return style.getBottomBorderColor();
    }

    /**
     * 公式前缀
     *
     * @return ignore
     */
    public boolean isQuotePrefixed() {
        return style.getQuotePrefixed();
    }

    /**
     * 是否自动调整大小
     *
     * @return ignore
     */
    public boolean isAutoResize() {
        return style.getShrinkToFit();
    }

    /**
     * 获取数据格式化索引
     *
     * @return ignore
     */
    public short getDataFormatIndex() {
        return style.getDataFormat();
    }

    /**
     * 获取数据格式化字符串
     *
     * @return ignore
     */
    public String getDataFormatString() {
        return style.getDataFormatString();
    }

    /**
     * 获取读取顺序
     *
     * @return 0默认 1从左往右 2从右往左
     */
    public int getReadingOrder() {
        if (style instanceof XSSFCellStyle) {
            return ((XSSFCellStyle) style).getReadingOrder().getCode();
        }
        return -1;
    }

}

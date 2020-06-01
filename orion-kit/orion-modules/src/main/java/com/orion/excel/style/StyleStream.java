package com.orion.excel.style;

import com.orion.utils.Colors;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 * Excel 单元格样式流
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/23 18:28
 */
public class StyleStream {

    private Workbook workbook;

    private CellStyle style;

    public StyleStream(Workbook workbook) {
        this.workbook = workbook;
        this.style = workbook.createCellStyle();
    }

    public StyleStream(CellStyle style) {
        this.style = style;
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
     * @param style CellStyle
     * @return StyleStream
     */
    public static StyleStream styleStream(CellStyle style) {
        return new StyleStream(style);
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
     * 设置字体
     *
     * @return this
     */
    public StyleStream font(Font font) {
        style.setFont(font);
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
     * @return this
     */
    public StyleStream wrapText() {
        style.setWrapText(true);
        return this;
    }

    /**
     * 设置水平默认对齐
     *
     * @return this
     */
    public StyleStream defalutAlignment() {
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
     * 设置隐藏
     *
     * @return this
     */
    public StyleStream hidden() {
        style.setHidden(true);
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
     * 设置纹理前景颜
     *
     * @return this
     */
    public StyleStream textureColor(int i) {
        style.setFillForegroundColor((short) i);
        return this;
    }

    /**
     * 设置纹理前景颜
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
     * 设置纹理前景颜
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
     * 设置纹理前景颜
     *
     * @param index 覆盖索引
     * @param rgb   颜色
     * @return this
     */
    public StyleStream textureColor(short index, byte[] rgb) {
        if (workbook instanceof HSSFWorkbook) {
            style.setFillForegroundColor(getPaletteColorIndex(index, rgb));
        }
        return this;
    }

    /**
     * 设置纹理前景颜
     *
     * @param index 覆盖索引
     * @param c     颜色
     * @return this
     */
    public StyleStream textureColor(short index, String c) {
        if (workbook instanceof HSSFWorkbook) {
            style.setFillForegroundColor(getPaletteColorIndex(index, c));
        }
        return this;
    }

    /**
     * 设置纹理前景颜为蓝色
     *
     * @return this
     */
    public StyleStream blueTextureColor() {
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        return this;
    }

    /**
     * 设置纹理前景颜为黑色
     *
     * @return this
     */
    public StyleStream blackTextureColor() {
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        return this;
    }

    /**
     * 设置纹理前景颜为红色
     *
     * @return this
     */
    public StyleStream redTextureColor() {
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        return this;
    }

    /**
     * 设置纹理前景颜为粉色
     *
     * @return this
     */
    public StyleStream pinkTextureColor() {
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.PINK.getIndex());
        return this;
    }

    /**
     * 设置纹理前景颜为靛青色
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
            style.setFillBackgroundColor(getPaletteColorIndex(index, rgb));
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
            style.setFillBackgroundColor(getPaletteColorIndex(index, c));
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
            short ci = getPaletteColorIndex(index, rgb);
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
            short ci = getPaletteColorIndex(index, c);
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
            style.setTopBorderColor(getPaletteColorIndex(index, rgb));
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
            style.setTopBorderColor(getPaletteColorIndex(index, c));
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
            style.setBottomBorderColor(getPaletteColorIndex(index, rgb));
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
            style.setBottomBorderColor(getPaletteColorIndex(index, c));
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
            style.setLeftBorderColor(getPaletteColorIndex(index, rgb));
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
            style.setLeftBorderColor(getPaletteColorIndex(index, c));
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
            style.setRightBorderColor(getPaletteColorIndex(index, rgb));
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
            style.setRightBorderColor(getPaletteColorIndex(index, c));
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

    /**
     * 通过调色板获取颜色
     *
     * @param index 调色板颜色覆盖索引
     * @param c     color
     * @return index
     */
    private short getPaletteColorIndex(short index, String c) {
        return getPaletteColorIndex(index, Colors.toRgb(c));
    }

    /**
     * 通过调色板获取颜色
     *
     * @param index 调色板颜色覆盖索引
     * @param c     color
     * @return index
     */
    private short getPaletteColorIndex(short index, byte[] rgb) {
        if (workbook instanceof HSSFWorkbook) {
            HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
            HSSFColor color = palette.findColor(rgb[0], rgb[1], rgb[2]);
            if (color == null) {
                palette.setColorAtIndex(index, rgb[0], rgb[1], rgb[2]);
                return index;
            } else {
                return color.getIndex();
            }
        }
        return 0;
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
    public Boolean getWrapText() {
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
    public Boolean getLocked() {
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

}

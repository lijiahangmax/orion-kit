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
package com.orion.office.excel.style;

import com.orion.office.excel.option.PrintOption;
import com.orion.office.excel.type.ExcelMarginType;
import com.orion.office.excel.type.ExcelPaperType;
import org.apache.poi.ss.usermodel.PaperSize;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * excel 打印设置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/7 20:01
 */
public class PrintStream {

    /**
     * sheet
     */
    private final Sheet sheet;

    /**
     * 打印设置
     */
    private final PrintSetup printSetup;

    public PrintStream(Sheet sheet) {
        this.sheet = sheet;
        this.printSetup = sheet.getPrintSetup();
    }

    public static PrintStream stream(Sheet sheet) {
        return new PrintStream(sheet);
    }

    /**
     * 解析 print
     *
     * @param sheet  sheet
     * @param option option
     * @return PrintSetup
     */
    public static PrintSetup parsePrint(Sheet sheet, PrintOption option) {
        PrintStream stream = new PrintStream(sheet)
                .printGridLines(option.isPrintGridLines())
                .printRowHeading(option.isPrintRowHeading())
                .autoLimit(option.isAutoLimit())
                .fit(option.isFit())
                .color(option.isColor())
                .landScapePrint(option.isLandScapePrint())
                .printOrientation(option.isSetPrintOrientation())
                .scale(option.getScale())
                .notes(option.isNotes())
                .usePage(option.isUsePage())
                .draft(option.isDraft())
                .topToBottomOrder(option.isTopToBottom())
                .horizontallyCenter(option.isHorizontallyCenter())
                .verticallyCenter(option.isVerticallyCenter());
        ExcelPaperType paper = option.getPaper();
        if (paper != null) {
            stream.paper(paper.getCode());
        }
        Integer horizontalResolution = option.getHorizontalResolution();
        if (horizontalResolution != null) {
            stream.horizontalResolution(horizontalResolution);
        }
        Integer verticalResolution = option.getVerticalResolution();
        if (verticalResolution != null) {
            stream.verticalResolution(verticalResolution);
        }
        Integer width = option.getWidth();
        if (width != null) {
            stream.width(width);
        }
        Integer height = option.getHeight();
        if (height != null) {
            stream.height(height);
        }
        Double leftMargin = option.getLeftMargin();
        if (leftMargin != null) {
            stream.margin(ExcelMarginType.LEFT, leftMargin);
        }
        Double rightMargin = option.getRightMargin();
        if (rightMargin != null) {
            stream.margin(ExcelMarginType.RIGHT, rightMargin);
        }
        Double topMargin = option.getTopMargin();
        if (topMargin != null) {
            stream.margin(ExcelMarginType.TOP, topMargin);
        }
        Double bottomMargin = option.getBottomMargin();
        if (bottomMargin != null) {
            stream.margin(ExcelMarginType.BOTTOM, bottomMargin);
        }
        Double headerMargin = option.getHeaderMargin();
        if (headerMargin != null) {
            stream.margin(ExcelMarginType.HEADER, headerMargin);
        }
        Double footerMargin = option.getFooterMargin();
        if (footerMargin != null) {
            stream.margin(ExcelMarginType.FOOTER, footerMargin);
        }
        Integer pageStart = option.getPageStart();
        if (pageStart != null) {
            stream.pageStart(pageStart);
        }
        Integer copies = option.getCopies();
        if (copies != null) {
            stream.copies(copies);
        }
        Integer limit = option.getLimit();
        if (limit != null) {
            stream.autoLimit(true);
            stream.limit(limit);
        }
        int[] repeat = option.getRepeat();
        if (repeat != null) {
            stream.repeat(repeat);
        }
        return stream.getPrintSetup();
    }

    /**
     * 打印网格线
     *
     * @return this
     */
    public PrintStream printGridLines() {
        sheet.setPrintGridlines(true);
        return this;
    }

    /**
     * 打印网格线
     *
     * @param print 是否打印
     * @return this
     */
    public PrintStream printGridLines(boolean print) {
        sheet.setPrintGridlines(print);
        return this;
    }

    /**
     * 不打印网格线
     *
     * @return this
     */
    public PrintStream unPrintGridLines() {
        sheet.setPrintGridlines(false);
        return this;
    }

    /**
     * 打印行标题和列标题
     *
     * @return this
     */
    public PrintStream printRowHeading() {
        sheet.setPrintRowAndColumnHeadings(true);
        return this;
    }

    /**
     * 打印行标题和列标题
     *
     * @param print 是否打印
     * @return this
     */
    public PrintStream printRowHeading(boolean print) {
        sheet.setPrintRowAndColumnHeadings(print);
        return this;
    }

    /**
     * 打印行标题和列标题
     *
     * @return this
     */
    public PrintStream unPrintHeading() {
        sheet.setPrintRowAndColumnHeadings(false);
        return this;
    }

    /**
     * sheet页自适应
     *
     * @return this
     */
    public PrintStream fit() {
        sheet.setFitToPage(true);
        return this;
    }

    /**
     * 打印自适应
     *
     * @param auto 是否自适应
     * @return this
     */
    public PrintStream fit(boolean fit) {
        sheet.setFitToPage(fit);
        return this;
    }

    /**
     * 打印自适应
     *
     * @return this
     */
    public PrintStream unFit() {
        sheet.setFitToPage(false);
        return this;
    }

    /**
     * sheet页行数自适应
     *
     * @return this
     */
    public PrintStream autoLimit() {
        sheet.setAutobreaks(true);
        return this;
    }

    /**
     * sheet页行数自适应
     *
     * @param auto 是否自适应
     * @return this
     */
    public PrintStream autoLimit(boolean auto) {
        sheet.setAutobreaks(auto);
        return this;
    }

    /**
     * sheet页行数不自适应
     *
     * @return this
     */
    public PrintStream unAutoLimit() {
        sheet.setAutobreaks(false);
        return this;
    }

    /**
     * 每页行数
     *
     * @param limit 行数
     * @return this
     */
    public PrintStream limit(int limit) {
        sheet.setRowBreak(limit);
        return this;
    }

    /**
     * 设置页面水平居中
     *
     * @return this
     */
    public PrintStream horizontallyCenter() {
        sheet.setHorizontallyCenter(true);
        return this;
    }

    /**
     * 设置页面水平居中
     *
     * @param center 是否水平居中
     * @return this
     */
    public PrintStream horizontallyCenter(boolean center) {
        sheet.setHorizontallyCenter(center);
        return this;
    }

    /**
     * 不设置页面水平居中
     *
     * @return this
     */
    public PrintStream unHorizontallyCenter() {
        sheet.setHorizontallyCenter(false);
        return this;
    }

    /**
     * 设置页面垂直居中
     *
     * @return this
     */
    public PrintStream verticallyCenter() {
        sheet.setVerticallyCenter(true);
        return this;
    }

    /**
     * 设置页面垂直居中
     *
     * @param center 是否垂直居中
     * @return this
     */
    public PrintStream verticallyCenter(boolean center) {
        sheet.setVerticallyCenter(center);
        return this;
    }

    /**
     * 不设置页面垂直居中
     *
     * @return this
     */
    public PrintStream unVerticallyCenter() {
        sheet.setVerticallyCenter(false);
        return this;
    }

    /**
     * 重复打印行和列
     * 0 rowStartIndex
     * 1 rowEndIndex
     * 2 columnStartIndex
     * 3 columnEndIndex
     * [1, 3] = [0, 1, 0, 3]
     *
     * @param repeat repeat
     * @return this
     */
    public PrintStream repeat(int[] repeat) {
        if (repeat == null) {
            return this;
        }
        if (repeat.length == 2) {
            return repeat(repeat[0], repeat[1]);
        } else if (repeat.length == 4) {
            return repeat(repeat[0], repeat[1], repeat[2], repeat[3]);
        }
        return this;
    }

    /**
     * 重复打印行和列
     *
     * @param rowEndIndex    结束行索引
     * @param columnEndIndex 结束列索引
     * @return this
     */
    public PrintStream repeat(int rowEndIndex, int columnEndIndex) {
        sheet.setRepeatingRows(new CellRangeAddress(0, rowEndIndex, 0, 0));
        sheet.setRepeatingColumns(new CellRangeAddress(0, 0, 0, columnEndIndex));
        return this;
    }

    /**
     * 重复打印行和列
     *
     * @param rowStartIndex    开始行索引
     * @param rowEndIndex      结束行索引
     * @param columnStartIndex 开始列索引
     * @param columnEndIndex   结束列索引
     * @return this
     */
    public PrintStream repeat(int rowStartIndex, int rowEndIndex, int columnStartIndex, int columnEndIndex) {
        sheet.setRepeatingRows(new CellRangeAddress(rowStartIndex, rowEndIndex, 0, 0));
        sheet.setRepeatingColumns(new CellRangeAddress(0, 0, columnStartIndex, columnEndIndex));
        return this;
    }

    /**
     * 设置纸张大小
     *
     * @param i 纸张大小
     * @return this
     */
    public PrintStream paper(int i) {
        printSetup.setPaperSize((short) i);
        return this;
    }

    /**
     * 设置纸张大小
     *
     * @param s 纸张大小
     * @return this
     */
    public PrintStream paper(PaperSize s) {
        printSetup.setPaperSize((short) (s.ordinal() + 1));
        return this;
    }

    /**
     * 设置纸张大小
     *
     * @param type 纸张
     * @return this
     */
    public PrintStream paper(ExcelPaperType type) {
        printSetup.setPaperSize((short) type.getCode());
        return this;
    }

    /**
     * 打印彩色纸张
     *
     * @return this
     */
    public PrintStream color() {
        printSetup.setNoColor(false);
        return this;
    }

    /**
     * 打印彩色纸张
     *
     * @param color 是否为彩色
     * @return this
     */
    public PrintStream color(boolean color) {
        printSetup.setNoColor(!color);
        return this;
    }

    /**
     * 打印黑白纸张
     *
     * @return this
     */
    public PrintStream noColor() {
        printSetup.setNoColor(true);
        return this;
    }

    /**
     * 横向打印
     *
     * @return this
     */
    public PrintStream landScapePrint() {
        printSetup.setLandscape(true);
        return this;
    }

    /**
     * 横向打印
     *
     * @param landScape 是否横向打印
     * @return this
     */
    public PrintStream landScapePrint(boolean landScape) {
        printSetup.setLandscape(landScape);
        return this;
    }

    /**
     * 纵向打印
     *
     * @return this
     */
    public PrintStream portraitPrint() {
        printSetup.setLandscape(false);
        return this;
    }

    /**
     * 纵向打印
     *
     * @param portrait 是否纵向打印
     * @return this
     */
    public PrintStream portraitPrint(boolean portrait) {
        printSetup.setLandscape(portrait);
        return this;
    }

    /**
     * 设置打印方向
     *
     * @param set 是否设置打印方向
     * @return this
     */
    public PrintStream printOrientation(boolean set) {
        printSetup.setNoOrientation(!set);
        return this;
    }

    /**
     * 设置打印方向
     *
     * @return this
     */
    public PrintStream setPrintOrientation() {
        printSetup.setNoOrientation(false);
        return this;
    }

    /**
     * 不设置打印方向
     *
     * @return this
     */
    public PrintStream unsetPrintOrientation() {
        printSetup.setNoOrientation(true);
        return this;
    }

    /**
     * 设置缩放比例 10 - 400
     *
     * @param scale 缩放比例
     * @return this
     */
    public PrintStream scale(int scale) {
        printSetup.setScale((short) scale);
        return this;
    }

    /**
     * 打印批注
     *
     * @return this
     */
    public PrintStream notes() {
        printSetup.setNotes(true);
        return this;
    }

    /**
     * 不打印批注
     *
     * @return this
     */
    public PrintStream unNotes() {
        printSetup.setNotes(false);
        return this;
    }

    /**
     * 打印批注
     *
     * @param print 是否打印批注
     * @return this
     */
    public PrintStream notes(boolean print) {
        printSetup.setNotes(print);
        return this;
    }

    /**
     * 水平分辨率
     *
     * @param dpi 分辨率
     * @return this
     */
    public PrintStream horizontalResolution(int dpi) {
        printSetup.setHResolution((short) dpi);
        return this;
    }

    /**
     * 垂直分辨率
     *
     * @param dpi 分辨率
     * @return this
     */
    public PrintStream verticalResolution(int dpi) {
        printSetup.setVResolution((short) dpi);
        return this;
    }

    /**
     * 设置页宽
     *
     * @param width 页宽
     * @return this
     */
    public PrintStream width(int width) {
        printSetup.setFitWidth((short) width);
        return this;
    }

    /**
     * 设置页高
     *
     * @param height 页高
     * @return this
     */
    public PrintStream height(int height) {
        printSetup.setFitHeight((short) height);
        return this;
    }

    /**
     * 设置左边距 英寸
     *
     * @param margin 边距
     * @return this
     */
    public PrintStream leftMargin(double margin) {
        return margin(ExcelMarginType.LEFT, margin);
    }

    /**
     * 设置右边距 英寸
     *
     * @param margin 边距
     * @return this
     */
    public PrintStream rightMargin(double margin) {
        return margin(ExcelMarginType.RIGHT, margin);
    }

    /**
     * 设置上边距 英寸
     *
     * @param margin 边距
     * @return this
     */
    public PrintStream topMargin(double margin) {
        return margin(ExcelMarginType.TOP, margin);
    }

    /**
     * 设置下边距 英寸
     *
     * @param margin 边距
     * @return this
     */
    public PrintStream bottomMargin(double margin) {
        return margin(ExcelMarginType.BOTTOM, margin);
    }

    /**
     * 设置页眉边距 英寸
     *
     * @param margin 边距
     * @return this
     */
    public PrintStream headerMargin(double margin) {
        return margin(ExcelMarginType.HEADER, margin);
    }

    /**
     * 设置页脚边距 英寸
     *
     * @param margin 边距
     * @return this
     */
    public PrintStream footerMargin(double margin) {
        return margin(ExcelMarginType.FOOTER, margin);
    }

    /**
     * 设置边距 英寸
     *
     * @param type   类型
     * @param margin 边距
     * @return this
     */
    public PrintStream margin(ExcelMarginType type, double margin) {
        sheet.setMargin(type.getCode(), margin);
        return this;
    }

    /**
     * 设置边距 英寸
     *
     * @param type   类型 {@link ExcelMarginType}
     * @param margin 边距
     * @return this
     */
    public PrintStream margin(short type, double margin) {
        sheet.setMargin(type, margin);
        return this;
    }

    /**
     * 使用起始页
     *
     * @return this
     */
    public PrintStream usePage() {
        printSetup.setUsePage(true);
        return this;
    }

    /**
     * 使用起始页
     *
     * @param use 是否使用
     * @return this
     */
    public PrintStream usePage(boolean use) {
        printSetup.setUsePage(use);
        return this;
    }

    /**
     * 不使用起始页
     *
     * @return this
     */
    public PrintStream unUsePage() {
        printSetup.setUsePage(false);
        return this;
    }

    /**
     * 起始页
     *
     * @param page 起始页
     * @return this
     */
    public PrintStream pageStart(int page) {
        printSetup.setUsePage(true);
        printSetup.setPageStart((short) page);
        return this;
    }

    /**
     * 打印份数
     *
     * @param i 份数
     * @return this
     */
    public PrintStream copies(int i) {
        printSetup.setCopies((short) i);
        return this;
    }

    /**
     * 使用草稿模式
     *
     * @return this
     */
    public PrintStream draft() {
        printSetup.setDraft(true);
        return this;
    }

    /**
     * 使用草稿模式
     *
     * @param use 是否使用
     * @return this
     */
    public PrintStream draft(boolean use) {
        printSetup.setDraft(use);
        return this;
    }

    /**
     * 不使用草稿模式
     *
     * @return this
     */
    public PrintStream unsetDraft() {
        printSetup.setDraft(false);
        return this;
    }

    /**
     * 设置顺序为从左往右
     *
     * @return this
     */
    public PrintStream leftToRightOrder() {
        printSetup.setLeftToRight(true);
        return this;
    }

    /**
     * 设置顺序为从左往右
     *
     * @param left 是否从左到右
     * @return this
     */
    public PrintStream leftToRightOrder(boolean left) {
        printSetup.setLeftToRight(left);
        return this;
    }

    /**
     * 设置顺序为自上而下
     *
     * @return this
     */
    public PrintStream topToBottomOrder() {
        printSetup.setLeftToRight(false);
        return this;
    }

    /**
     * 设置顺序为自上而下
     *
     * @param top 是否自上而下
     * @return this
     */
    public PrintStream topToBottomOrder(boolean top) {
        printSetup.setLeftToRight(top);
        return this;
    }

    /**
     * 获取纸张大小
     *
     * @return 大小
     */
    public int getPaper() {
        return printSetup.getPaperSize();
    }

    /**
     * 获取打印颜色是否为彩色
     *
     * @return 彩色true
     */
    public boolean isColor() {
        return !printSetup.getNoColor();
    }

    /**
     * 获取打印颜色是否为黑白
     *
     * @return 黑白true
     */
    public boolean isNoColor() {
        return printSetup.getNoColor();
    }

    /**
     * 获取打印方向
     *
     * @return 1默认 2横向 3纵向
     */
    public int getOrientation() {
        return printSetup.getNoOrientation() ? 1 : printSetup.getLandscape() ? 3 : 2;
    }

    /**
     * 获取缩放比例
     *
     * @return 缩放比例
     */
    public int getScale() {
        return printSetup.getScale();
    }

    /**
     * 是否打印批注
     *
     * @return true打印
     */
    public boolean isPrintNotes() {
        return printSetup.getNotes();
    }

    /**
     * 获取水平分辨率
     *
     * @return 水平分辨率
     */
    public int getHorizontalResolution() {
        return printSetup.getHResolution();
    }

    /**
     * 获取垂直分辨率
     *
     * @return 垂直分辨率
     */
    public int getVerticalResolution() {
        return printSetup.getVResolution();
    }

    /**
     * 获取宽度
     *
     * @return 宽度
     */
    public int getWidth() {
        return printSetup.getFitWidth();
    }

    /**
     * 获取高度
     *
     * @return 高度
     */
    public int getHeight() {
        return printSetup.getFitHeight();
    }

    /**
     * 获取左边距 英寸
     *
     * @return 边距
     */
    public double getLeftMargin() {
        return getMargin(ExcelMarginType.LEFT);
    }

    /**
     * 获取右边距 英寸
     *
     * @return 边距
     */
    public double getRightMargin() {
        return getMargin(ExcelMarginType.RIGHT);
    }

    /**
     * 获取上边距 英寸
     *
     * @return 边距
     */
    public double getTopMargin() {
        return getMargin(ExcelMarginType.TOP);
    }

    /**
     * 获取下边距 英寸
     *
     * @return 边距
     */
    public double getBottomMargin() {
        return getMargin(ExcelMarginType.BOTTOM);
    }

    /**
     * 获取边距 英寸
     *
     * @param type type
     * @return 边距
     */
    public double getMargin(ExcelMarginType type) {
        return sheet.getMargin(type.getCode());
    }

    /**
     * 获取边距 英寸
     *
     * @param type 类型 {@link ExcelMarginType}
     * @return 边距
     */
    public double getMargin(int type) {
        return sheet.getMargin((short) type);
    }

    /**
     * 是否使用起始页码
     *
     * @return true使用
     */
    public boolean getUsePage() {
        return printSetup.getUsePage();
    }

    /**
     * 获取起始页码
     *
     * @return 起始页码
     */
    public int getPageStart() {
        return printSetup.getPageStart();
    }

    /**
     * 获取打印份数
     *
     * @return 打印份数
     */
    public int getCopies() {
        return printSetup.getCopies();
    }

    /**
     * 是否为草稿模式
     *
     * @return true草稿模式
     */
    public boolean isDraft() {
        return printSetup.getDraft();
    }

    /**
     * 获取打印顺序
     *
     * @return 0从左到右 1自上而下
     */
    public int getPrintOrder() {
        return printSetup.getLeftToRight() ? 0 : 1;
    }

    /**
     * 获取配置是否合法
     *
     * @return true合法
     */
    public boolean isValid() {
        return printSetup.getValidSettings();
    }

    /**
     * sheet页行数是否自适应
     *
     * @return true自适应
     */
    public boolean isAutoLimit() {
        return sheet.getAutobreaks();
    }

    /**
     * 打印
     *
     * @return true自适应
     */
    public boolean isFit() {
        return sheet.getFitToPage();
    }

    /**
     * sheet页是否水平居中
     *
     * @return true水平居中
     */
    public boolean isHorizontallyCenter() {
        return sheet.getHorizontallyCenter();
    }

    /**
     * sheet页是否垂直居中
     *
     * @return true垂直居中
     */
    public boolean isVerticallyCenter() {
        return sheet.getVerticallyCenter();
    }

    /**
     * 重复打印的行和列
     * <p>
     * 0 rowStartIndex
     * * 1 rowEndIndex
     * * 2 columnStartIndex
     * * 3 columnEndIndex
     *
     * @return array
     */
    public int[] getRepeat() {
        int[] r = new int[4];
        CellRangeAddress rows = sheet.getRepeatingRows();
        if (rows != null) {
            r[0] = rows.getFirstRow();
            r[1] = rows.getLastRow();
        }
        CellRangeAddress columns = sheet.getRepeatingColumns();
        if (columns != null) {
            r[2] = columns.getFirstColumn();
            r[3] = columns.getLastColumn();
        }
        return r;
    }

    /**
     * 是否打印网格线
     *
     * @return true打印
     */
    public boolean isPrintGridLines() {
        return sheet.isPrintGridlines();
    }

    /**
     * 是否打印行和列的标题
     *
     * @return true打印
     */
    public boolean isPrintHeadings() {
        return sheet.isPrintRowAndColumnHeadings();
    }

    public Sheet getSheet() {
        return sheet;
    }

    public PrintSetup getPrintSetup() {
        return printSetup;
    }

}

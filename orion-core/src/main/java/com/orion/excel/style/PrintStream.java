package com.orion.excel.style;

import com.orion.excel.option.PrintOption;
import org.apache.poi.ss.usermodel.PaperSize;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Excel 打印设置
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/7 20:01
 */
public class PrintStream {

    /**
     * sheet
     */
    private Sheet sheet;

    /**
     * 打印设置
     */
    private PrintSetup printSetup;

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
                .printHeading(option.isPrintHeading())
                .autoBreak(option.isAutoBreak())
                .paper(option.getPaper().getCode())
                .color(option.isColor())
                .landScapePrint(option.isLandScapePrint())
                .printOrientation(option.isSetPrintOrientation())
                .scale(option.getScale())
                .notes(option.isNotes())
                .usePage(option.isUsePage())
                .draft(option.isDraft())
                .topToBottomOrder(option.isTopToBottom());
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
        Integer headerMargin = option.getHeaderMargin();
        if (headerMargin != null) {
            stream.headerMargin(headerMargin);
        }
        Integer footerMargin = option.getFooterMargin();
        if (footerMargin != null) {
            stream.footerMargin(footerMargin);
        }
        Integer pageStart = option.getPageStart();
        if (pageStart != null) {
            stream.pageStart(pageStart);
        }
        Integer copies = option.getCopies();
        if (copies != null) {
            stream.copies(copies);
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
        sheet.setPrintGridlines(true);
        return this;
    }

    /**
     * 打印行和列的标题
     *
     * @return this
     */
    public PrintStream printHeading() {
        sheet.setPrintRowAndColumnHeadings(true);
        return this;
    }

    /**
     * 打印行和列的标题
     *
     * @param print 是否打印
     * @return this
     */
    public PrintStream printHeading(boolean print) {
        sheet.setPrintRowAndColumnHeadings(print);
        return this;
    }

    /**
     * 打印行和列的标题
     *
     * @return this
     */
    public PrintStream unPrintHeading() {
        sheet.setPrintRowAndColumnHeadings(false);
        return this;
    }

    /**
     * sheet页自适应页面大小
     *
     * @return this
     */
    public PrintStream autoBreak() {
        sheet.setAutobreaks(false);
        return this;
    }

    /**
     * sheet页自适应页面大小
     *
     * @param auto 是否自适应
     * @return this
     */
    public PrintStream autoBreak(boolean auto) {
        sheet.setAutobreaks(auto);
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
     * 设置纸张A4
     *
     * @return this
     */
    public PrintStream paperA4() {
        printSetup.setPaperSize((short) (PaperSize.A4_PAPER.ordinal() + 1));
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
     * 页脚边距
     *
     * @param margin 边距
     * @return this
     */
    public PrintStream footerMargin(double margin) {
        printSetup.setFooterMargin(margin);
        return this;
    }

    /**
     * 头部边距
     *
     * @param margin 边距
     * @return this
     */
    public PrintStream headerMargin(double margin) {
        printSetup.setHeaderMargin(margin);
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
     * 获取页脚边距
     *
     * @return 页脚边距
     */
    public double getFooterMargin() {
        return printSetup.getFooterMargin();
    }

    /**
     * 获取头部边距
     *
     * @return 头部边距
     */
    public double getHeaderMargin() {
        return printSetup.getHeaderMargin();
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
     * sheet页是否自适应页面大小
     *
     * @return true自适应
     */
    public boolean isAutoBreak() {
        return sheet.getAutobreaks();
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

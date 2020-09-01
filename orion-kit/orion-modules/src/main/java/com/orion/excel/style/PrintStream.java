package com.orion.excel.style;

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

    public PrintStream(PrintSetup printSetup) {
        this.printSetup = printSetup;
    }

    public PrintStream(Sheet sheet, PrintSetup printSetup) {
        this.sheet = sheet;
        this.printSetup = printSetup;
    }

    public static PrintStream printStream(Sheet sheet) {
        return new PrintStream(sheet);
    }

    public static PrintStream printStream(PrintSetup printSetup) {
        return new PrintStream(printSetup);
    }

    public static PrintStream printStream(Sheet sheet, PrintSetup printSetup) {
        return new PrintStream(sheet, printSetup);
    }

    /**
     * 设置纸张
     *
     * @param i 纸张
     * @return this
     */
    public PrintStream paper(int i) {
        printSetup.setPaperSize((short) i);
        return this;
    }

    /**
     * 设置纸张
     *
     * @param s 纸张
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
    public PrintStream colour() {
        printSetup.setNoColor(false);
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
     * 纵向打印
     *
     * @return this
     */
    public PrintStream portraitPrint() {
        printSetup.setLandscape(false);
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
    public PrintStream unsetNotes() {
        printSetup.setNotes(false);
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
     * 不使用起始页
     *
     * @return this
     */
    public PrintStream unUsedPage() {
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
     * 设置顺序为自上而下
     *
     * @return this
     */
    public PrintStream topToBottomOrder() {
        printSetup.setLeftToRight(false);
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
    public boolean getNoColor() {
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
    public boolean getNotes() {
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
    public int getcopies() {
        return printSetup.getCopies();
    }

    /**
     * 是否为草稿模式
     *
     * @return true草稿模式
     */
    public boolean getDraft() {
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
    public boolean getValid() {
        return printSetup.getValidSettings();
    }

    public Sheet getSheet() {
        return sheet;
    }

    public PrintSetup getPrintSetup() {
        return printSetup;
    }

}

package com.orion.excel.option;

import com.orion.excel.type.ExcelPaperType;

import java.io.Serializable;

/**
 * Excel 打印参数
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/12/25 16:24
 */
public class PrintOption implements Serializable {

    /**
     * 是否打印网格线
     */
    private boolean printGridLines;

    /**
     * 是否打印列和标题
     */
    private boolean printHeading;

    /**
     * sheet是否自适应
     */
    private boolean autoBreak;

    /**
     * 纸张大小
     */
    private ExcelPaperType paper;

    /**
     * 是否是彩色
     */
    private boolean color;

    /**
     * 是否横向打印
     */
    private boolean landScapePrint;

    /**
     * 是否设置打印方向
     */
    private boolean setPrintOrientation;

    /**
     * 缩放比例  10 - 400
     */
    private Integer scale;

    /**
     * 是否打印批注
     */
    private boolean notes;

    /**
     * 水平分辨率
     */
    private Integer horizontalResolution;

    /**
     * 垂直分辨率
     */
    private Integer verticalResolution;

    /**
     * 宽
     */
    private Integer width;

    /**
     * 高
     */
    private Integer height;

    /**
     * 页眉边距
     */
    private Integer headerMargin;

    /**
     * 页脚边距
     */
    private Integer footerMargin;

    /**
     * 是否使用起始页
     */
    private boolean usePage;

    /**
     * 起始页码
     */
    private Integer pageStart;

    /**
     * 打印份数
     */
    private Integer copies;

    /**
     * 是否为草稿模式
     */
    private boolean draft;

    /**
     * 是否自上而下
     */
    private boolean topToBottom;

    public boolean isPrintGridLines() {
        return printGridLines;
    }

    public void setPrintGridLines(boolean printGridLines) {
        this.printGridLines = printGridLines;
    }

    public boolean isPrintHeading() {
        return printHeading;
    }

    public void setPrintHeading(boolean printHeading) {
        this.printHeading = printHeading;
    }

    public boolean isAutoBreak() {
        return autoBreak;
    }

    public void setAutoBreak(boolean autoBreak) {
        this.autoBreak = autoBreak;
    }

    public ExcelPaperType getPaper() {
        return paper;
    }

    public void setPaper(ExcelPaperType paper) {
        this.paper = paper;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public boolean isLandScapePrint() {
        return landScapePrint;
    }

    public void setLandScapePrint(boolean landScapePrint) {
        this.landScapePrint = landScapePrint;
    }

    public boolean isSetPrintOrientation() {
        return setPrintOrientation;
    }

    public void setSetPrintOrientation(boolean setPrintOrientation) {
        this.setPrintOrientation = setPrintOrientation;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public boolean isNotes() {
        return notes;
    }

    public void setNotes(boolean notes) {
        this.notes = notes;
    }

    public Integer getHorizontalResolution() {
        return horizontalResolution;
    }

    public void setHorizontalResolution(Integer horizontalResolution) {
        this.horizontalResolution = horizontalResolution;
    }

    public Integer getVerticalResolution() {
        return verticalResolution;
    }

    public void setVerticalResolution(Integer verticalResolution) {
        this.verticalResolution = verticalResolution;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getHeaderMargin() {
        return headerMargin;
    }

    public void setHeaderMargin(Integer headerMargin) {
        this.headerMargin = headerMargin;
    }

    public Integer getFooterMargin() {
        return footerMargin;
    }

    public void setFooterMargin(Integer footerMargin) {
        this.footerMargin = footerMargin;
    }

    public boolean isUsePage() {
        return usePage;
    }

    public void setUsePage(boolean usePage) {
        this.usePage = usePage;
    }

    public Integer getPageStart() {
        return pageStart;
    }

    public void setPageStart(Integer pageStart) {
        this.pageStart = pageStart;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public boolean isTopToBottom() {
        return topToBottom;
    }

    public void setTopToBottom(boolean topToBottom) {
        this.topToBottom = topToBottom;
    }

}

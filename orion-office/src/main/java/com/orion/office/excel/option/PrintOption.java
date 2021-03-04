package com.orion.office.excel.option;

import com.orion.office.excel.type.ExcelPaperType;

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
     * 打印行标题和列标题
     */
    private boolean printRowHeading;

    /**
     * sheet页行数自适应
     */
    private boolean autoLimit;

    /**
     * sheet每页行数
     */
    private Integer limit;

    /**
     * 打印自适应
     */
    private boolean fit;

    /**
     * 页面水平居中
     */
    private boolean horizontallyCenter;

    /**
     * 页面垂直居中
     */
    private boolean verticallyCenter;

    /**
     * 重复打印的行和列
     * 0 rowStartIndex
     * 1 rowEndIndex
     * 2 columnStartIndex
     * 3 columnEndIndex
     * [1, 3] = [0, 1, 0, 3]
     */
    private int[] repeat;

    /**
     * 纸张大小
     */
    private ExcelPaperType paper = ExcelPaperType.DEFAULT;

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

    /**
     * 左边距 英寸
     */
    private Double leftMargin;

    /**
     * 右边距 英寸
     */
    private Double rightMargin;

    /**
     * 上边距 英寸
     */
    private Double topMargin;

    /**
     * 下边距 英寸
     */
    private Double bottomMargin;

    /**
     * 页眉边距 英寸
     */
    private Double headerMargin;

    /**
     * 页脚边距 英寸
     */
    private Double footerMargin;

    public boolean isPrintGridLines() {
        return printGridLines;
    }

    public PrintOption setPrintGridLines(boolean printGridLines) {
        this.printGridLines = printGridLines;
        return this;
    }

    public boolean isPrintRowHeading() {
        return printRowHeading;
    }

    public PrintOption setPrintRowHeading(boolean printRowHeading) {
        this.printRowHeading = printRowHeading;
        return this;
    }

    public boolean isAutoLimit() {
        return autoLimit;
    }

    public PrintOption setAutoLimit(boolean autoLimit) {
        this.autoLimit = autoLimit;
        return this;
    }

    public ExcelPaperType getPaper() {
        return paper;
    }

    public PrintOption setPaper(ExcelPaperType paper) {
        this.paper = paper;
        return this;
    }

    public boolean isColor() {
        return color;
    }

    public PrintOption setColor(boolean color) {
        this.color = color;
        return this;
    }

    public boolean isLandScapePrint() {
        return landScapePrint;
    }

    public PrintOption setLandScapePrint(boolean landScapePrint) {
        this.landScapePrint = landScapePrint;
        return this;
    }

    public boolean isSetPrintOrientation() {
        return setPrintOrientation;
    }

    public PrintOption setSetPrintOrientation(boolean setPrintOrientation) {
        this.setPrintOrientation = setPrintOrientation;
        return this;
    }

    public Integer getScale() {
        return scale;
    }

    public PrintOption setScale(Integer scale) {
        this.scale = scale;
        return this;
    }

    public boolean isNotes() {
        return notes;
    }

    public PrintOption setNotes(boolean notes) {
        this.notes = notes;
        return this;
    }

    public Integer getHorizontalResolution() {
        return horizontalResolution;
    }

    public PrintOption setHorizontalResolution(Integer horizontalResolution) {
        this.horizontalResolution = horizontalResolution;
        return this;
    }

    public Integer getVerticalResolution() {
        return verticalResolution;
    }

    public PrintOption setVerticalResolution(Integer verticalResolution) {
        this.verticalResolution = verticalResolution;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public PrintOption setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public PrintOption setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public boolean isUsePage() {
        return usePage;
    }

    public PrintOption setUsePage(boolean usePage) {
        this.usePage = usePage;
        return this;
    }

    public Integer getPageStart() {
        return pageStart;
    }

    public PrintOption setPageStart(Integer pageStart) {
        this.pageStart = pageStart;
        return this;
    }

    public Integer getCopies() {
        return copies;
    }

    public PrintOption setCopies(Integer copies) {
        this.copies = copies;
        return this;
    }

    public boolean isDraft() {
        return draft;
    }

    public PrintOption setDraft(boolean draft) {
        this.draft = draft;
        return this;
    }

    public boolean isTopToBottom() {
        return topToBottom;
    }

    public PrintOption setTopToBottom(boolean topToBottom) {
        this.topToBottom = topToBottom;
        return this;
    }

    public boolean isFit() {
        return fit;
    }

    public PrintOption setFit(boolean fit) {
        this.fit = fit;
        return this;
    }

    public boolean isHorizontallyCenter() {
        return horizontallyCenter;
    }

    public PrintOption setHorizontallyCenter(boolean horizontallyCenter) {
        this.horizontallyCenter = horizontallyCenter;
        return this;
    }

    public boolean isVerticallyCenter() {
        return verticallyCenter;
    }

    public PrintOption setVerticallyCenter(boolean verticallyCenter) {
        this.verticallyCenter = verticallyCenter;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public PrintOption setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public int[] getRepeat() {
        return repeat;
    }

    public PrintOption setRepeat(int[] repeat) {
        this.repeat = repeat;
        return this;
    }

    public PrintOption setRepeat(int rowEndIndex, int columnEndIndex) {
        this.repeat = new int[]{0, rowEndIndex, 0, columnEndIndex};
        return this;
    }

    public PrintOption setRepeat(int rowStartIndex, int rowEndIndex, int columnStartIndex, int columnEndIndex) {
        this.repeat = new int[]{rowStartIndex, rowEndIndex, columnStartIndex, columnEndIndex};
        return this;
    }

    public Double getLeftMargin() {
        return leftMargin;
    }

    public PrintOption setLeftMargin(Double leftMargin) {
        this.leftMargin = leftMargin;
        return this;
    }

    public Double getRightMargin() {
        return rightMargin;
    }

    public PrintOption setRightMargin(Double rightMargin) {
        this.rightMargin = rightMargin;
        return this;
    }

    public Double getTopMargin() {
        return topMargin;
    }

    public PrintOption setTopMargin(Double topMargin) {
        this.topMargin = topMargin;
        return this;
    }

    public Double getBottomMargin() {
        return bottomMargin;
    }

    public PrintOption setBottomMargin(Double bottomMargin) {
        this.bottomMargin = bottomMargin;
        return this;
    }

    public Double getHeaderMargin() {
        return headerMargin;
    }

    public PrintOption setHeaderMargin(Double headerMargin) {
        this.headerMargin = headerMargin;
        return this;
    }

    public Double getFooterMargin() {
        return footerMargin;
    }

    public PrintOption setFooterMargin(Double footerMargin) {
        this.footerMargin = footerMargin;
        return this;
    }

}

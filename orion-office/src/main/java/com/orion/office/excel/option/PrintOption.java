package com.orion.office.excel.option;

import com.orion.office.excel.type.ExcelPaperType;

import java.io.Serializable;

/**
 * excel 打印参数
 *
 * @author Jiahang Li
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

    public void setPrintGridLines(boolean printGridLines) {
        this.printGridLines = printGridLines;
    }

    public boolean isPrintRowHeading() {
        return printRowHeading;
    }

    public void setPrintRowHeading(boolean printRowHeading) {
        this.printRowHeading = printRowHeading;
    }

    public boolean isAutoLimit() {
        return autoLimit;
    }

    public void setAutoLimit(boolean autoLimit) {
        this.autoLimit = autoLimit;
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

    public boolean isFit() {
        return fit;
    }

    public void setFit(boolean fit) {
        this.fit = fit;
    }

    public boolean isHorizontallyCenter() {
        return horizontallyCenter;
    }

    public void setHorizontallyCenter(boolean horizontallyCenter) {
        this.horizontallyCenter = horizontallyCenter;
    }

    public boolean isVerticallyCenter() {
        return verticallyCenter;
    }

    public void setVerticallyCenter(boolean verticallyCenter) {
        this.verticallyCenter = verticallyCenter;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public int[] getRepeat() {
        return repeat;
    }

    public void setRepeat(int[] repeat) {
        this.repeat = repeat;
    }

    public void setRepeat(int rowEndIndex, int columnEndIndex) {
        this.repeat = new int[]{0, rowEndIndex, 0, columnEndIndex};
    }

    public void setRepeat(int rowStartIndex, int rowEndIndex, int columnStartIndex, int columnEndIndex) {
        this.repeat = new int[]{rowStartIndex, rowEndIndex, columnStartIndex, columnEndIndex};
    }

    public Double getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(Double leftMargin) {
        this.leftMargin = leftMargin;
    }

    public Double getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(Double rightMargin) {
        this.rightMargin = rightMargin;
    }

    public Double getTopMargin() {
        return topMargin;
    }

    public void setTopMargin(Double topMargin) {
        this.topMargin = topMargin;
    }

    public Double getBottomMargin() {
        return bottomMargin;
    }

    public void setBottomMargin(Double bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    public Double getHeaderMargin() {
        return headerMargin;
    }

    public void setHeaderMargin(Double headerMargin) {
        this.headerMargin = headerMargin;
    }

    public Double getFooterMargin() {
        return footerMargin;
    }

    public void setFooterMargin(Double footerMargin) {
        this.footerMargin = footerMargin;
    }

}

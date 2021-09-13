package com.orion.office.excel.option;

import java.io.Serializable;

/**
 * export 表格参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/28 11:20
 */
public class ExportSheetOption implements Serializable {

    /**
     * sheet 名称
     */
    private String name;

    /**
     * 默认行宽
     */
    private Integer columnWidth;

    /**
     * 标题行高
     */
    private Integer titleHeight;

    /**
     * 数据行高
     */
    private Integer rowHeight;

    /**
     * 头部行高
     */
    private Integer headerHeight;

    /**
     * 缩放
     */
    private Integer zoom;

    /**
     * 列是否使用默认样式 全局
     * skip() 会使用默认样式
     */
    private boolean columnUseDefaultStyle;

    /**
     * 是否将index作为排序字段
     */
    private boolean indexToSort;

    /**
     * 头是否使用列的样式
     */
    private boolean headerUseColumnStyle;

    /**
     * 是否跳过默认表头
     */
    private boolean skipFieldHeader;

    /**
     * 是否跳过批注
     */
    private boolean skipComment;

    /**
     * 是否跳过超链接
     */
    private boolean skipLink;

    /**
     * 是否跳过图片
     */
    private boolean skipPicture;

    /**
     * 是否跳过图片异常
     */
    private boolean skipPictureException;

    /**
     * 是否跳过下拉选择框
     */
    private boolean skipSelectOption;

    /**
     * 是否跳过标题
     */
    private boolean skipTitle;

    /**
     * 是否跳过空行
     */
    private boolean skipNullRows;

    /**
     * 是否固定表头
     */
    private boolean freezeHeader;

    /**
     * 是否可以筛选
     */
    private boolean filterHeader;

    /**
     * 是否为选中
     */
    private boolean selected;

    /**
     * 是否隐藏
     */
    private boolean hidden;

    /**
     * 页眉
     */
    private HeaderOption headerOption;

    /**
     * 页脚
     */
    private FooterOption footerOption;

    /**
     * 标题
     */
    private TitleOption titleOption;

    /**
     * 打印
     */
    private PrintOption printOption;

    /**
     * 标题和表头所占用的行数
     */
    private int titleAndHeaderLastRowIndex;

    /**
     * 最大列索引
     */
    private int columnMaxIndex;

    /**
     * 标题
     */
    private String title;

    /**
     * 名字是否重新设置
     */
    private boolean nameReset;

    /**
     * 是否隐藏网格线
     */
    private boolean displayGridLines;

    /**
     * 是否隐藏列数和行数
     */
    private boolean displayRowColHeadings;

    /**
     * 是否不执行公式 会修改列宽单位
     */
    private boolean displayFormulas;

    public ExportSheetOption() {
        this.skipNullRows = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }

    public Integer getTitleHeight() {
        return titleHeight;
    }

    public void setTitleHeight(Integer titleHeight) {
        this.titleHeight = titleHeight;
    }

    public Integer getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(Integer rowHeight) {
        this.rowHeight = rowHeight;
    }

    public Integer getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(Integer headerHeight) {
        this.headerHeight = headerHeight;
    }

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public boolean isColumnUseDefaultStyle() {
        return columnUseDefaultStyle;
    }

    public void setColumnUseDefaultStyle(boolean columnUseDefaultStyle) {
        this.columnUseDefaultStyle = columnUseDefaultStyle;
    }

    public boolean isIndexToSort() {
        return indexToSort;
    }

    public void setIndexToSort(boolean indexToSort) {
        this.indexToSort = indexToSort;
    }

    public boolean isHeaderUseColumnStyle() {
        return headerUseColumnStyle;
    }

    public void setHeaderUseColumnStyle(boolean headerUseColumnStyle) {
        this.headerUseColumnStyle = headerUseColumnStyle;
    }

    public boolean isSkipFieldHeader() {
        return skipFieldHeader;
    }

    public void setSkipFieldHeader(boolean skipFieldHeader) {
        this.skipFieldHeader = skipFieldHeader;
    }

    public boolean isSkipComment() {
        return skipComment;
    }

    public void setSkipComment(boolean skipComment) {
        this.skipComment = skipComment;
    }

    public boolean isSkipLink() {
        return skipLink;
    }

    public void setSkipLink(boolean skipLink) {
        this.skipLink = skipLink;
    }

    public boolean isSkipPicture() {
        return skipPicture;
    }

    public void setSkipPicture(boolean skipPicture) {
        this.skipPicture = skipPicture;
    }

    public boolean isSkipPictureException() {
        return skipPictureException;
    }

    public void setSkipPictureException(boolean skipPictureException) {
        this.skipPictureException = skipPictureException;
    }

    public boolean isSkipSelectOption() {
        return skipSelectOption;
    }

    public void setSkipSelectOption(boolean skipSelectOption) {
        this.skipSelectOption = skipSelectOption;
    }

    public boolean isFreezeHeader() {
        return freezeHeader;
    }

    public void setFreezeHeader(boolean freezeHeader) {
        this.freezeHeader = freezeHeader;
    }

    public boolean isFilterHeader() {
        return filterHeader;
    }

    public void setFilterHeader(boolean filterHeader) {
        this.filterHeader = filterHeader;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public HeaderOption getHeaderOption() {
        return headerOption;
    }

    public void setHeaderOption(HeaderOption headerOption) {
        this.headerOption = headerOption;
    }

    public FooterOption getFooterOption() {
        return footerOption;
    }

    public void setFooterOption(FooterOption footerOption) {
        this.footerOption = footerOption;
    }

    public TitleOption getTitleOption() {
        return titleOption;
    }

    public void setTitleOption(TitleOption titleOption) {
        this.titleOption = titleOption;
    }

    public PrintOption getPrintOption() {
        return printOption;
    }

    public void setPrintOption(PrintOption printOption) {
        this.printOption = printOption;
    }

    public int getTitleAndHeaderLastRowIndex() {
        return titleAndHeaderLastRowIndex;
    }

    public void setTitleAndHeaderLastRowIndex(int titleAndHeaderLastRowIndex) {
        this.titleAndHeaderLastRowIndex = titleAndHeaderLastRowIndex;
    }

    public int getColumnMaxIndex() {
        return columnMaxIndex;
    }

    public void setColumnMaxIndex(int columnMaxIndex) {
        this.columnMaxIndex = columnMaxIndex;
    }

    public boolean isSkipTitle() {
        return skipTitle;
    }

    public void setSkipTitle(boolean skipTitle) {
        this.skipTitle = skipTitle;
    }

    public boolean isSkipNullRows() {
        return skipNullRows;
    }

    public void setSkipNullRows(boolean skipNullRows) {
        this.skipNullRows = skipNullRows;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isNameReset() {
        return nameReset;
    }

    public void setNameReset(boolean nameReset) {
        this.nameReset = nameReset;
    }

    public boolean isDisplayGridLines() {
        return displayGridLines;
    }

    public void setDisplayGridLines(boolean displayGridLines) {
        this.displayGridLines = displayGridLines;
    }

    public boolean isDisplayRowColHeadings() {
        return displayRowColHeadings;
    }

    public void setDisplayRowColHeadings(boolean displayRowColHeadings) {
        this.displayRowColHeadings = displayRowColHeadings;
    }

    public boolean isDisplayFormulas() {
        return displayFormulas;
    }

    public void setDisplayFormulas(boolean displayFormulas) {
        this.displayFormulas = displayFormulas;
    }

}

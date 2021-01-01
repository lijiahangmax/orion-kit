package com.orion.excel.option;

import java.io.Serializable;

/**
 * Excel 表格参数
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/28 11:20
 */
public class SheetOption implements Serializable {

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
    private boolean skipNullRow = true;

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
     * 头使用行的样式
     */
    private boolean headUseColumnStyle;

    /**
     * 是否添加了默认表头
     */
    private boolean addDefaultHeader;

    /**
     * 是否已经添加了标题
     */
    private boolean addTitle;

    /**
     * 列数
     */
    private int columnSize;

    /**
     * 标题
     */
    private String title;

    /**
     * 名字是否重新设置
     */
    private boolean nameReset;

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

    public boolean isHeadUseColumnStyle() {
        return headUseColumnStyle;
    }

    public void setHeadUseColumnStyle(boolean headUseColumnStyle) {
        this.headUseColumnStyle = headUseColumnStyle;
    }

    public boolean isAddDefaultHeader() {
        return addDefaultHeader;
    }

    public void setAddDefaultHeader(boolean addDefaultHeader) {
        this.addDefaultHeader = addDefaultHeader;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public boolean isSkipTitle() {
        return skipTitle;
    }

    public void setSkipTitle(boolean skipTitle) {
        this.skipTitle = skipTitle;
    }

    public boolean isSkipNullRow() {
        return skipNullRow;
    }

    public void setSkipNullRow(boolean skipNullRow) {
        this.skipNullRow = skipNullRow;
    }

    public boolean isAddTitle() {
        return addTitle;
    }

    public void setAddTitle(boolean addTitle) {
        this.addTitle = addTitle;
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

}

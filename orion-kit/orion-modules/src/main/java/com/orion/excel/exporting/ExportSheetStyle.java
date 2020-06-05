package com.orion.excel.exporting;

import java.io.Serializable;

/**
 * Excel 导出注解 表格样式
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/28 11:20
 */
public class ExportSheetStyle implements Serializable {

    /**
     * sheet 名称
     */
    private String name;

    /**
     * 默认行宽
     */
    private Integer rowWidth;

    /**
     * 数据行高
     */
    private Integer rowHeight;
    /**
     * 头部行高
     */
    private Integer headerHeight;

    /**
     * 头是否使用列的样式
     */
    private boolean headerUseRowStyle;

    public String getName() {
        return name;
    }

    public ExportSheetStyle setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getHeaderHeight() {
        return headerHeight;
    }

    public ExportSheetStyle setHeaderHeight(Integer headerHeight) {
        this.headerHeight = headerHeight;
        return this;
    }

    public Integer getRowHeight() {
        return rowHeight;
    }

    public ExportSheetStyle setRowHeight(Integer rowHeight) {
        this.rowHeight = rowHeight;
        return this;
    }

    public boolean isHeaderUseRowStyle() {
        return headerUseRowStyle;
    }

    public ExportSheetStyle setHeaderUseRowStyle(boolean headerUseRowStyle) {
        this.headerUseRowStyle = headerUseRowStyle;
        return this;
    }

    public Integer getRowWidth() {
        return rowWidth;
    }

    public ExportSheetStyle setRowWidth(Integer rowWidth) {
        this.rowWidth = rowWidth;
        return this;
    }

    @Override
    public String toString() {
        return "ExportSheetStyle{" +
                "name='" + name + '\'' +
                ", rowWidth=" + rowWidth +
                ", headerHeight=" + headerHeight +
                ", rowHeight=" + rowHeight +
                ", headerUseRowStyle=" + headerUseRowStyle +
                '}';
    }

}

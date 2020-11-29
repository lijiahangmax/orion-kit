package com.orion.excel.builder;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import java.io.Serializable;

/**
 * Excel 字段映射
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/1 16:40
 */
public class ExcelFieldMap implements Serializable {

    /**
     * 表格列索引 用于bean
     */
    private Integer columnIndex;

    /**
     * 字段名称 用于bean
     */
    private String fieldName;

    /**
     * 数据索引 用于数组或map
     */
    private Integer fieldIndex;

    /**
     * 单元格样式
     */
    private CellStyle cellStyle;

    /**
     * 字体样式
     */
    private Font font;

    /**
     * 字段类型
     */
    private ExcelFieldType fieldType;

    /**
     * 日期格式化 必须设置ExcelFieldType.DATE
     */
    private String datePattern;

    public ExcelFieldMap() {
    }

    public ExcelFieldMap(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public ExcelFieldMap(Integer columnIndex, String fieldName) {
        this.columnIndex = columnIndex;
        this.fieldName = fieldName;
    }

    public ExcelFieldMap(Integer columnIndex, Integer fieldIndex) {
        this.columnIndex = columnIndex;
        this.fieldIndex = fieldIndex;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public ExcelFieldMap setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
        return this;
    }

    public String getFieldName() {
        return fieldName;
    }

    public ExcelFieldMap setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public Integer getFieldIndex() {
        return fieldIndex;
    }

    public ExcelFieldMap setFieldIndex(Integer fieldIndex) {
        this.fieldIndex = fieldIndex;
        return this;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public ExcelFieldMap setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public ExcelFieldMap setFont(Font font) {
        this.font = font;
        return this;
    }

    public ExcelFieldType getFieldType() {
        return fieldType;
    }

    public ExcelFieldMap setFieldType(ExcelFieldType fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public ExcelFieldMap setDatePattern(String datePattern) {
        this.datePattern = datePattern;
        return this;
    }

    @Override
    public String toString() {
        return "ExcelFieldMap{" +
                "columnIndex=" + columnIndex +
                ", fieldName='" + fieldName + '\'' +
                ", fieldIndex=" + fieldIndex +
                ", cellStyle=" + cellStyle +
                ", font=" + font +
                ", fieldType=" + fieldType +
                ", datePattern='" + datePattern + '\'' +
                '}';
    }

}

package com.orion.excel.builder;

import com.orion.excel.type.ExcelFieldType;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Excel 字段映射
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/1 16:40
 */
public class ExcelFieldOption implements Serializable {

    /**
     * bean 表格列索引
     * map 数据索引
     * array 数据索引
     */
    private Integer index;

    /**
     * 字段名称 用于bean
     */
    private String fieldName;

    /**
     * 单元格样式
     */
    private CellStyle style;

    /**
     * 字体样式
     */
    private Font font;

    /**
     * 字段类型
     */
    private ExcelFieldType type;

    /**
     * 日期格式化
     *
     * @see ExcelFieldType#DATE
     */
    private String datePattern;

    /**
     * getterMethod
     */
    private Method getterMethod;

    public ExcelFieldOption() {
    }

    public ExcelFieldOption(Integer index) {
        this.index = index;
    }

    public ExcelFieldOption(Integer index, String fieldName) {
        this.index = index;
        this.fieldName = fieldName;
    }

    public Integer getIndex() {
        return index;
    }

    public ExcelFieldOption setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public String getFieldName() {
        return fieldName;
    }

    public ExcelFieldOption setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public CellStyle getStyle() {
        return style;
    }

    public ExcelFieldOption setStyle(CellStyle style) {
        this.style = style;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public ExcelFieldOption setFont(Font font) {
        this.font = font;
        return this;
    }

    public ExcelFieldType getType() {
        return type;
    }

    public ExcelFieldOption setType(ExcelFieldType type) {
        this.type = type;
        return this;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public ExcelFieldOption setDatePattern(String datePattern) {
        this.datePattern = datePattern;
        return this;
    }

    protected Method getGetterMethod() {
        return getterMethod;
    }

    protected void setGetterMethod(Method getterMethod) {
        this.getterMethod = getterMethod;
    }

}

package com.orion.excel.option;

import com.orion.excel.type.ExcelReadType;

import java.io.Serializable;

/**
 * Import 读取字段参数
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/2 0:38
 */
public class ImportFieldOption implements Serializable {

    /**
     * index
     */
    private int index;

    /**
     * 读取类型
     */
    private ExcelReadType type;

    /**
     * 格式
     */
    private String parseFormat;

    /**
     * 单元格配置
     */
    private CellOption cellOption;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ExcelReadType getType() {
        return type;
    }

    public void setType(ExcelReadType type) {
        this.type = type;
    }

    public String getParseFormat() {
        return parseFormat;
    }

    public void setParseFormat(String parseFormat) {
        this.parseFormat = parseFormat;
    }

    public CellOption getCellOption() {
        return cellOption;
    }

    public void setCellOption(CellOption cellOption) {
        this.cellOption = cellOption;
    }

}

package com.orion.office.excel.option;

import com.orion.office.excel.type.ExcelFieldType;

import java.io.Serializable;

/**
 * 写入字段类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/9 20:57
 */
public class WriteFieldOption implements Serializable {

    /**
     * 表格列索引
     */
    private int index;

    private ExcelFieldType type;

    private CellOption cellOption;

    public WriteFieldOption(int index) {
        this.index = index;
        this.type = ExcelFieldType.AUTO;
    }

    public WriteFieldOption(int index, ExcelFieldType type) {
        this.index = index;
        this.type = type;
    }

    public WriteFieldOption(int index, ExcelFieldType type, String format) {
        this.index = index;
        this.type = type;
        this.cellOption = new CellOption(format);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ExcelFieldType getType() {
        return type;
    }

    public void setType(ExcelFieldType type) {
        this.type = type;
    }

    public CellOption getCellOption() {
        return cellOption;
    }

    public void setCellOption(CellOption cellOption) {
        this.cellOption = cellOption;
    }

}

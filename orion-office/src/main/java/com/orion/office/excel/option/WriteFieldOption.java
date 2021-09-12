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

    public WriteFieldOption setIndex(int index) {
        this.index = index;
        return this;
    }

    public ExcelFieldType getType() {
        return type;
    }

    public WriteFieldOption setType(ExcelFieldType type) {
        this.type = type;
        return this;
    }

    public CellOption getCellOption() {
        return cellOption;
    }

    public WriteFieldOption setCellOption(CellOption cellOption) {
        this.cellOption = cellOption;
        return this;
    }

}

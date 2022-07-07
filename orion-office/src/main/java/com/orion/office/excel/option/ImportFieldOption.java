package com.orion.office.excel.option;

import com.orion.lang.utils.Strings;
import com.orion.office.excel.type.ExcelReadType;

import java.io.Serializable;

/**
 * import 读取字段参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/2 0:38
 */
public class ImportFieldOption implements Serializable {

    /**
     * row index
     */
    private int index;

    /**
     * 读取类型
     */
    private ExcelReadType type;

    /**
     * 单元格配置
     */
    private CellOption cellOption;

    public ImportFieldOption() {
    }

    public ImportFieldOption(int index, ExcelReadType type) {
        this.index = index;
        this.type = type;
    }

    public ImportFieldOption(int index, ExcelReadType type, String parseFormat) {
        this.index = index;
        this.type = type;
        if (!Strings.isEmpty(parseFormat)) {
            this.cellOption = new CellOption(parseFormat);
        }
    }

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

    public CellOption getCellOption() {
        return cellOption;
    }

    public void setCellOption(CellOption cellOption) {
        this.cellOption = cellOption;
    }

}

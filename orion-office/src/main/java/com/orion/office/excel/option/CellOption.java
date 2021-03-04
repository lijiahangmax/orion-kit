package com.orion.office.excel.option;

import java.io.Serializable;

/**
 * Excel 单元格参数
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/12/24 16:00
 */
public class CellOption implements Serializable {

    /**
     * 格式化
     */
    private String format;

    public CellOption() {
    }

    public CellOption(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}

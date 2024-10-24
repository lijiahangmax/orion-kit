/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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

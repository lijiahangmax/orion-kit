/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.office.excel.option;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.office.excel.type.ExcelReadType;

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

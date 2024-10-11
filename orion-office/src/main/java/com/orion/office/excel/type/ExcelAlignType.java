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
package com.orion.office.excel.type;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * excel 水平对齐方式
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/21 14:13
 */
public enum ExcelAlignType {

    /**
     * 默认
     */
    DEFAULT(HorizontalAlignment.GENERAL.getCode()),

    /**
     * 左对齐
     */
    LEFT(HorizontalAlignment.LEFT.getCode()),

    /**
     * 居中
     */
    CENTER(HorizontalAlignment.CENTER.getCode()),

    /**
     * 右对齐
     */
    RIGHT(HorizontalAlignment.RIGHT.getCode()),

    /**
     * 填充
     */
    FILL(HorizontalAlignment.FILL.getCode()),

    /**
     * 两端对齐
     */
    JUSTIFY(HorizontalAlignment.JUSTIFY.getCode()),

    /**
     * 跨列举中
     */
    CENTER_SELECTION(HorizontalAlignment.CENTER_SELECTION.getCode()),

    /**
     * 分布对齐
     */
    DISTRIBUTED(HorizontalAlignment.DISTRIBUTED.getCode());

    private final int code;

    ExcelAlignType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}

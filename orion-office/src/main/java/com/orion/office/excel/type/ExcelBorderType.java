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

import org.apache.poi.ss.usermodel.BorderStyle;

/**
 * excel 边框类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/21 14:08
 */
public enum ExcelBorderType {

    /**
     * 无边框 默认
     */
    DEFAULT(BorderStyle.NONE.getCode()),

    /**
     * 细边框
     */
    THIN(BorderStyle.THIN.getCode()),

    /**
     * 中等边框
     */
    MEDIUM(BorderStyle.MEDIUM.getCode()),

    /**
     * 粗边框
     */
    THICK(BorderStyle.THICK.getCode()),

    /**
     * 虚线边框
     */
    DASHED(BorderStyle.DASHED.getCode()),

    /**
     * 点边框
     */
    DOTTED(BorderStyle.DOTTED.getCode()),

    /**
     * 丝纹边框
     */
    HAIR(BorderStyle.HAIR.getCode()),

    /**
     * 双线边框
     */
    DOUBLE(BorderStyle.DOUBLE.getCode());

    private final int code;

    ExcelBorderType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}

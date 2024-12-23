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
package cn.orionsec.kit.office.excel.annotation;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.office.excel.type.ExcelAlignType;
import cn.orionsec.kit.office.excel.type.ExcelBorderType;
import cn.orionsec.kit.office.excel.type.ExcelVerticalAlignType;

import java.lang.annotation.*;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/31 9:48
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportTitle {

    /**
     * 标题
     */
    String title();

    /**
     * 使用行数
     */
    int useRow() default 1;

    /**
     * 使用列数 默认列长
     */
    int useColumn() default -1;

    /**
     * 垂直对齐方式
     */
    ExcelVerticalAlignType verticalAlign() default ExcelVerticalAlignType.CENTER;

    /**
     * 水平对齐方式
     */
    ExcelAlignType align() default ExcelAlignType.CENTER;

    /**
     * 背景颜色 RGB
     */
    String backgroundColor() default "#6B9AC9";

    /**
     * 边框 首行无法显示 上左边框
     */
    ExcelBorderType border() default ExcelBorderType.DEFAULT;

    /**
     * 边框颜色 RGB
     */
    String borderColor() default Strings.EMPTY;

    /**
     * 字体
     */
    ExportFont font() default @ExportFont(bold = true);

}

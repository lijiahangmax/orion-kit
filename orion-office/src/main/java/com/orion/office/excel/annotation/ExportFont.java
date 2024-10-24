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
package com.orion.office.excel.annotation;

import com.orion.lang.utils.Strings;
import com.orion.office.excel.type.ExcelUnderType;

import java.lang.annotation.*;

/**
 * excel 导出字体注解
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/27 12:11
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportFont {

    /**
     * 字体名称
     */
    String fontName() default Strings.EMPTY;

    /**
     * 字体大小
     */
    int fontSize() default -1;

    /**
     * 字体颜色 HEX
     */
    String color() default Strings.EMPTY;

    /**
     * 是否加粗
     */
    boolean bold() default false;

    /**
     * 是否使用斜体
     */
    boolean italic() default false;

    /**
     * 是否使用删除线
     */
    boolean delete() default false;

    /**
     * 是否使用下滑线
     */
    ExcelUnderType under() default ExcelUnderType.NONE;

}

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
import cn.orionsec.kit.office.excel.option.PictureOption;
import cn.orionsec.kit.office.excel.type.ExcelPictureType;

import java.lang.annotation.*;

/**
 * excel 导出图片
 * 可以和超链接一起使用
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see ExportLink
 * @since 2020/12/27 22:49
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportPicture {

    /**
     * 图片类型
     */
    ExcelPictureType type() default ExcelPictureType.AUTO;

    /**
     * 数据是否为base64
     */
    boolean base64() default false;

    /**
     * 如果是流是否自动关闭
     *
     * @see java.io.InputStream
     */
    boolean autoClose() default false;

    /**
     * X 缩放
     */
    double scaleX() default 1;

    /**
     * Y 缩放
     */
    double scaleY() default 1;

    /**
     * 图片公式
     * <p>
     * 默认: @
     * 字段: $xxx
     */
    String image() default PictureOption.ORIGIN;

    /**
     * 单元格内容公式
     * <p>
     * 不设置内容: ""
     * 原文本: @
     * 固定文本: !xxx
     * 字段: $xxx
     */
    String text() default Strings.EMPTY;

}

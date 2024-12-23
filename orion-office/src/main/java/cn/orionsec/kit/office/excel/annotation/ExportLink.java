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

import cn.orionsec.kit.office.excel.option.LinkOption;
import cn.orionsec.kit.office.excel.type.ExcelLinkType;

import java.lang.annotation.*;

/**
 * excel 导出字段超链接注解
 * 可以和图片一起使用
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see ExportPicture
 * @since 2020/5/27 12:11
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportLink {

    /**
     * 超链接类型
     */
    ExcelLinkType type() default ExcelLinkType.LINK_URL;

    /**
     * 超链地址公式
     * <p>
     * 固定地址: !xxx
     * 字段: $xxx
     * 原文本: @
     */
    String address() default LinkOption.ORIGIN;

    /**
     * 超链文本公式
     * <p>
     * 固定文本: !xxx
     * 字段: $xxx
     * 原文本: @
     */
    String text() default LinkOption.ORIGIN;

}

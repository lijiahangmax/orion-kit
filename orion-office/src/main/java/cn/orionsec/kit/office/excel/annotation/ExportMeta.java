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
import cn.orionsec.kit.office.KitOfficeConfiguration;

import java.lang.annotation.*;

/**
 * excel 元数据注解
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/8 18:06
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportMeta {

    /**
     * 作者
     * <p>
     * 为空则使用默认作者
     *
     * @see KitOfficeConfiguration#EXCEL_DEFAULT_AUTHOR
     */
    String author() default Strings.EMPTY;

    /**
     * 标题
     */
    String title() default Strings.EMPTY;

    /**
     * 主体
     */
    String subject() default Strings.EMPTY;

    /**
     * 关键字
     */
    String keywords() default Strings.EMPTY;

    /**
     * 修订
     */
    String revision() default Strings.EMPTY;

    /**
     * 描述
     */
    String description() default Strings.EMPTY;

    /**
     * 分类
     */
    String category() default Strings.EMPTY;

    /**
     * 公司
     */
    String company() default Strings.EMPTY;

    /**
     * 经理
     */
    String manager() default Strings.EMPTY;

    /**
     * 应用
     * <p>
     * 为空则使用默认应用
     *
     * @see KitOfficeConfiguration#EXCEL_DEFAULT_APPLICATION
     */
    String application() default Strings.EMPTY;

    /**
     * 修订人
     */
    String modifiedUser() default Strings.EMPTY;

    /**
     * 内容状态
     */
    String contentStatus() default Strings.EMPTY;

    /**
     * 内容类型
     */
    String contentType() default Strings.EMPTY;

    /**
     * 标识符
     */
    String identifier() default Strings.EMPTY;

}

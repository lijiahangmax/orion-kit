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
package cn.orionsec.kit.office.csv.annotation;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.constant.Letters;
import cn.orionsec.kit.office.csv.type.CsvEscapeMode;

import java.lang.annotation.*;

/**
 * csv 导入配置注解
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/25 11:34
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ImportSetting {

    /**
     * 文本限定符
     */
    char textQualifier() default Letters.QUOTE;

    /**
     * 是否使用文本限定符
     */
    boolean useTextQualifier() default true;

    /**
     * 列边界符
     */
    char delimiter() default Letters.COMMA;

    /**
     * 行边界符
     */
    char lineDelimiter() default Letters.NULL;

    /**
     * 注释符
     */
    char comment() default Letters.POUND;

    /**
     * 转义类型
     * 1. 双文本限定符转义
     * 2. 反斜杠转义
     */
    CsvEscapeMode escapeMode() default CsvEscapeMode.DOUBLE_QUALIFIER;

    /**
     * 编码格式
     */
    String charset() default Const.UTF_8;

    /**
     * 是否区分大小写
     */
    boolean caseSensitive() default true;

    /**
     * 是否去除首尾空格
     */
    boolean trim() default true;

    /**
     * 是否读取注释
     */
    boolean useComments() default false;

    /**
     * 安全开关 如果行数过大 则会抛出异常 用于限制内存
     */
    boolean safetySwitch() default true;

    /**
     * 是否跳过空行 行长度为0
     */
    boolean skipEmptyRows() default true;

    /**
     * 是否跳过raw记录
     */
    boolean skipRawRow() default true;

}

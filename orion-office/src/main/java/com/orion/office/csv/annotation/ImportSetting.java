package com.orion.office.csv.annotation;

import com.orion.constant.Const;
import com.orion.constant.Letters;
import com.orion.office.csv.type.CsvEscapeMode;

import java.lang.annotation.*;

/**
 * Csv 导入配置注解
 *
 * @author ljh15
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
     * 2. 反斜杠转义
     * 1. 双文本限定符转义
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

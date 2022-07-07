package com.orion.office.csv.annotation;

import com.orion.lang.constant.Const;
import com.orion.lang.constant.Letters;
import com.orion.office.csv.type.CsvEscapeMode;

import java.lang.annotation.*;

/**
 * csv 导出配置注解
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/25 11:36
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportSetting {

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
     * 是否强制使用文本限定符 包裹文字
     */
    boolean forceQualifier() default false;

    /**
     * 是否去除首尾空格
     */
    boolean trim() default false;

    /**
     * 是否将index作为排序字段
     */
    boolean indexToSort() default false;

}

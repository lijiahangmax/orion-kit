package com.orion.csv.annotation;

import com.orion.csv.type.CsvEscapeMode;
import com.orion.utils.constant.Letters;
import com.orion.utils.io.Streams;

import java.lang.annotation.*;

/**
 * Csv 导出配置注解
 *
 * @author ljh15
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
     * 2. 反斜杠转义
     * 1. 双文本限定符转义
     */
    CsvEscapeMode escapeMode() default CsvEscapeMode.DOUBLE_QUALIFIER;

    /**
     * 编码格式
     */
    String charset() default Streams.UFT_8;

    /**
     * 是否强制使用文本限定符 包裹文字
     */
    boolean forceQualifier() default false;

}

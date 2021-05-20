package com.orion.office.csv.annotation;

import com.orion.utils.Strings;

import java.lang.annotation.*;

/**
 * Csv 导出字段注解
 * <p>
 * 可以加到 field 或 getter 上, 但是必须有getter方法
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/25 11:33
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportField {

    /**
     * 列
     */
    int value();

    /**
     * 表头
     */
    String header() default Strings.EMPTY;

}

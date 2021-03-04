package com.orion.office.csv.annotation;

import java.lang.annotation.*;

/**
 * Csv 导入字段注解
 * <p>
 * 可以加到 field 或 setter上, 但是必须有setter方法
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/25 11:33
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ImportField {

    /**
     * 列
     */
    int value();

}

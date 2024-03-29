package com.orion.office.excel.annotation;

import java.lang.annotation.*;

/**
 * excel 导入忽略字段
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/2 0:14
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ImportIgnore {
}

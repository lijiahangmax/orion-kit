package com.orion.excel.annotation;

import java.lang.annotation.*;

/**
 * Excel 导出忽略字段
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/12/27 16:48
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportIgnore {

}

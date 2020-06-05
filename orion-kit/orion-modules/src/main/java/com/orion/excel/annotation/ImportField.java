package com.orion.excel.annotation;

import java.lang.annotation.*;

/**
 * Excel 导入注解
 * 可以加到 set方法 或者 字段上, 但是必须有set方法
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/27 12:12
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ImportField {

    /**
     * 导出表格列索引 从0开始
     */
    int value();

}

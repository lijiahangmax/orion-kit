package com.orion.office.excel.annotation;

import com.orion.office.excel.type.ExcelReadType;
import com.orion.utils.Strings;

import java.lang.annotation.*;

/**
 * Excel 导入注解
 * 可以加到 set方法 或者 字段上, 但是必须有set方法
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/27 12:12
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ImportField {

    /**
     * 导出表格列索引 从0开始
     */
    int index();

    /**
     * 读取类型
     */
    ExcelReadType type() default ExcelReadType.TEXT;

    /**
     * 解析格式
     */
    String parseFormat() default Strings.EMPTY;

}

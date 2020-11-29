package com.orion.excel.annotation;

import java.lang.annotation.*;

/**
 * Excel 导出表格注解
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/27 12:11
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportSheet {

    /**
     * sheet 名称
     */
    String value() default "";

    /**
     * 默认行宽
     */
    int rowWidth() default -1;

    /**
     * 头部行高
     */
    int headerHeight() default -1;

    /**
     * 数据行高
     */
    int rowHeight() default -1;

    /**
     * 表头是否使用数据列的样式
     */
    boolean headerUseRowStyle() default true;

}

package com.orion.excel.annotation;

import java.lang.annotation.*;

/**
 * Excel 导出字段注解
 * 可以加到 get方法 或者 字段上, 但是必须有get方法
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/27 12:11
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportField {

    /**
     * 表格列索引 从0开始
     */
    int value();

    /**
     * 单元格宽
     */
    int width() default -1;

    /**
     * 表头
     *
     * @return 表头
     */
    String header() default "";

    /**
     * 自动换行
     */
    boolean wrapText() default false;

    /**
     * 垂直对齐方式
     * 0上对齐 1居中对齐 2下对齐
     */
    int verticalAlign() default -1;

    /**
     * 水平对齐方式
     * 0默认 1左对齐 2居中 3右对齐 5两端对齐
     */
    int align() default -1;

    /**
     * 背景颜色
     */
    String backgroundColor() default "";

    /**
     * 边框
     * 0默认(不设置) 1细边框 2中边框 3粗边框
     */
    int border() default -1;

    /**
     * 边框颜色
     */
    String borderColor() default "";

    /**
     * 时间格式化
     */
    String datePattern() default "";

}

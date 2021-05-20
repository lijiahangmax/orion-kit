package com.orion.office.excel.annotation;

import com.orion.office.excel.type.ExcelAlignType;
import com.orion.office.excel.type.ExcelBorderType;
import com.orion.office.excel.type.ExcelVerticalAlignType;
import com.orion.utils.Strings;

import java.lang.annotation.*;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/31 9:48
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportTitle {

    /**
     * 标题
     */
    String title();

    /**
     * 使用行数
     */
    int useRow() default 1;

    /**
     * 使用列数 默认列长
     */
    int useColumn() default -1;

    /**
     * 垂直对齐方式
     */
    ExcelVerticalAlignType verticalAlign() default ExcelVerticalAlignType.CENTER;

    /**
     * 水平对齐方式
     */
    ExcelAlignType align() default ExcelAlignType.CENTER;

    /**
     * 背景颜色 RGB
     */
    String backgroundColor() default "#6B9AC9";

    /**
     * 边框 首行无法显示 上左边框
     */
    ExcelBorderType border() default ExcelBorderType.DEFAULT;

    /**
     * 边框颜色 RGB
     */
    String borderColor() default Strings.EMPTY;

    /**
     * 字体
     */
    ExportFont font() default @ExportFont(bold = true);

}

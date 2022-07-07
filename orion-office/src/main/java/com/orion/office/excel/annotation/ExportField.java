package com.orion.office.excel.annotation;

import com.orion.lang.utils.Strings;
import com.orion.office.excel.type.ExcelAlignType;
import com.orion.office.excel.type.ExcelBorderType;
import com.orion.office.excel.type.ExcelFieldType;
import com.orion.office.excel.type.ExcelVerticalAlignType;

import java.lang.annotation.*;

/**
 * excel 导出字段注解
 * 可以加到 get方法 或者 字段上, 但是必须有get方法
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/27 12:11
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportField {

    /**
     * 表格列索引 从0开始
     */
    int index();

    /**
     * 单元格宽
     */
    int width() default -1;

    /**
     * 表头
     */
    String header() default Strings.EMPTY;

    /**
     * 自动换行
     */
    boolean wrapText() default false;

    /**
     * 垂直对齐方式
     */
    ExcelVerticalAlignType verticalAlign() default ExcelVerticalAlignType.CENTER;

    /**
     * 水平对齐方式
     */
    ExcelAlignType align() default ExcelAlignType.DEFAULT;

    /**
     * 背景颜色 RGB
     */
    String backgroundColor() default Strings.EMPTY;

    /**
     * 边框
     */
    ExcelBorderType border() default ExcelBorderType.DEFAULT;

    /**
     * 边框颜色 RGB
     */
    String borderColor() default Strings.EMPTY;

    /**
     * 缩进
     */
    int indent() default -1;

    /**
     * 格式化
     */
    String format() default Strings.EMPTY;

    /**
     * 类型
     */
    ExcelFieldType type() default ExcelFieldType.AUTO;

    /**
     * 是否清除空格
     *
     * @see ExcelFieldType#TEXT
     * @see String
     */
    boolean trim() default false;

    /**
     * 表头跳过样式
     */
    boolean skipHeaderStyle() default false;

    /**
     * 隐藏列
     */
    boolean hidden() default false;

    /**
     * 是否锁定
     */
    boolean lock() default false;

    /**
     * 自动调节大小
     */
    boolean autoResize() default false;

    /**
     * 公式前缀
     */
    boolean quotePrefixed() default false;

    /**
     * 选择框
     */
    String[] selectOptions() default {};

}

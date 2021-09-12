package com.orion.office.excel.annotation;

import com.orion.office.excel.type.ExcelUnderType;
import com.orion.utils.Strings;

import java.lang.annotation.*;

/**
 * excel 导出字体注解
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/27 12:11
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportFont {

    /**
     * 字体名称
     */
    String fontName() default Strings.EMPTY;

    /**
     * 字体大小
     */
    int fontSize() default -1;

    /**
     * 字体颜色 RGB
     */
    String color() default Strings.EMPTY;

    /**
     * 是否加粗
     */
    boolean bold() default false;

    /**
     * 是否使用斜体
     */
    boolean italic() default false;

    /**
     * 是否使用删除线
     */
    boolean delete() default false;

    /**
     * 是否使用下滑线
     */
    ExcelUnderType under() default ExcelUnderType.NONE;

}

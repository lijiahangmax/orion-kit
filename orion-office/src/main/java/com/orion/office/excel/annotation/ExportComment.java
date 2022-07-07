package com.orion.office.excel.annotation;

import com.orion.lang.utils.Strings;

import java.lang.annotation.*;

/**
 * excel 表头批注注解
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/25 16:02
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportComment {

    /**
     * 批注
     */
    String comment();

    /**
     * 作者
     */
    String author() default Strings.EMPTY;

    /**
     * 是否可见
     */
    boolean visible() default false;

}

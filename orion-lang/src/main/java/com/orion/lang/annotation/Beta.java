package com.orion.lang.annotation;

import com.orion.lang.constant.Const;

import java.lang.annotation.*;

/**
 * 测试版本 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/7/7 22:37
 */
@Target({ElementType.TYPE, ElementType.FIELD,
        ElementType.METHOD, ElementType.PARAMETER,
        ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE,
        ElementType.ANNOTATION_TYPE, ElementType.PACKAGE,
        ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface Beta {

    /**
     * 测试版本
     */
    @Alias("version")
    String value() default Const.EMPTY;

    @Alias("value")
    String version() default Const.EMPTY;

}

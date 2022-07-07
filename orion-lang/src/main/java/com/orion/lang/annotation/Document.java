package com.orion.lang.annotation;

import com.orion.lang.constant.Const;

import java.lang.annotation.*;

/**
 * api 文档
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
public @interface Document {

    /**
     * 文档
     */
    String value();

    /**
     * 描述
     */
    String desc() default Const.EMPTY;

    /**
     * 版本
     */
    String version() default Const.EMPTY;

    /**
     * 维护者
     */
    String support() default Const.EMPTY;

    /**
     * review
     */
    String review() default Const.EMPTY;

}

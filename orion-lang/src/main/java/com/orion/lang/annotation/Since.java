package com.orion.lang.annotation;

import com.orion.lang.constant.Const;

import java.lang.annotation.*;

/**
 * 发行版本
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
public @interface Since {

    /**
     * 发行版本
     */
    @Alias("version")
    String value();

    @Alias("value")
    String version() default Const.EMPTY;

}

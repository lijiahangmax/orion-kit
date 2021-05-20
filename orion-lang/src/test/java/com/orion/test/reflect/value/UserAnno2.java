package com.orion.test.reflect.value;

import java.lang.annotation.*;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/19 17:39
 */
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserAnno2 {
}

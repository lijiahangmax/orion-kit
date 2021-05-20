package com.orion.utils.script;

import java.lang.annotation.*;

/**
 * 上下文参数 可加到字段或者getter方法 但是必须得有getter
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/5 14:33
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bind {

    /**
     * 参数
     */
    String value();

}

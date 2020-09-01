package com.orion.able;

import com.orion.utils.reflect.BeanWrapper;

/**
 * Bean 转化接口
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/30 17:11
 */
public interface ConvertBeanable {

    <T> T convert();

    default <T> T copyProperties(Class<T> clazz) {
        return BeanWrapper.copyProperties(this, clazz);
    }

}

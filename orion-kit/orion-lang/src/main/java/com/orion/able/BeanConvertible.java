package com.orion.able;

import com.orion.utils.reflect.BeanWrapper;

/**
 * Bean 转化接口
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/30 17:11
 */
public interface BeanConvertible {

    /**
     * sourceBean -> targetBean
     *
     * @param clazz targetBean class
     * @param <T>   T
     * @return targetBean
     */
    <T> T convert(Class<T> clazz);

    /**
     * copy bean
     *
     * @param clazz targetBean
     * @param <T>   T
     * @return targetBean
     */
    default <T> T copyProperties(Class<T> clazz) {
        return BeanWrapper.copyProperties(this, clazz);
    }

}

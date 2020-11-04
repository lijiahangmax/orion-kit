package com.orion.able;

import com.orion.utils.reflect.BeanWrapper;

import java.util.Map;

/**
 * Map转化接口
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/8/19 20:03
 */
public interface Mapable<K, V> {

    /**
     * 转为 map
     *
     * @return map
     */
    Map<K, V> toMap();

    /**
     * 转为 map
     *
     * @return map
     */
    default Map<String, Object> asMap() {
        return BeanWrapper.toMap(this);
    }

}

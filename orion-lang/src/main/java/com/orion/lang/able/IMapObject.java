package com.orion.lang.able;

import com.orion.lang.utils.reflect.BeanWrapper;

import java.util.Map;

/**
 * Map转化接口
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/8/19 20:03
 */
public interface IMapObject<K, V> {

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

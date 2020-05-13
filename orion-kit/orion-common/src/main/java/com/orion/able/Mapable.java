package com.orion.able;

import com.orion.utils.reflect.BeanWrapper;

import java.util.Map;

/**
 * Map转化接口
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/8/19 20:03
 */
public interface Mapable {

    Map toMap();

    default Map<String, Object> toMaped() {
        return BeanWrapper.toMap(this);
    }

}

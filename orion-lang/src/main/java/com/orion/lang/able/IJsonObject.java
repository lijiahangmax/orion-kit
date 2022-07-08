package com.orion.lang.able;

import com.orion.lang.utils.json.Jsons;

/**
 * 对象转 json 接口
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/8/15 20:34
 */
public interface IJsonObject {

    /**
     * 对象转 json
     *
     * @return json
     */
    default String toJsonString() {
        return Jsons.toJsonWriteNull(this);
    }

}

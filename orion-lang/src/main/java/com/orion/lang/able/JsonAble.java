package com.orion.lang.able;

import com.orion.lang.utils.json.Jsons;

/**
 * Json接口
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/8/15 20:34
 */
public interface JsonAble {

    /**
     * 对象转json
     *
     * @return jsonString
     */
    String toJsonString();

    /**
     * 对象转json
     *
     * @return jsonString
     */
    default String toJson() {
        return Jsons.toJsonWriteNull(this);
    }

}

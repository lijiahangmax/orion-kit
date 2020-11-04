package com.orion.able;

import com.orion.utils.json.Jsons;

/**
 * Json接口
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/8/15 20:34
 */
public interface JsonAble {

    String toJsonString();

    default String toJson() {
        return Jsons.toJson(this);
    }

}

package com.orion.base;

import com.orion.utils.json.Jsons;

import java.io.Serializable;

/**
 * 基本DTO接口
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/10/25 16:38
 */
public interface BaseInnerDTO extends Serializable {

    default String toJSON() {
        return Jsons.toJSON(this);
    }

}

package com.orion.base;

import com.orion.able.Jsonable;
import com.orion.utils.json.Jsons;

import java.io.Serializable;

/**
 * 基本DTO
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/8/15 20:35
 */
public class BaseDTO implements Serializable, Jsonable {

    private static final long serialVersionUID = 9291230628088591L;

    @Override
    public String toJsonString() {
        return Jsons.toJSON(this);
    }

}

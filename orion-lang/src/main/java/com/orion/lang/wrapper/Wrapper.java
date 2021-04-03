package com.orion.lang.wrapper;

import com.orion.able.JsonAble;
import com.orion.utils.json.Jsons;

import java.io.Serializable;

/**
 * 包装标识接口
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/9/16 14:35
 */
public interface Wrapper<T> extends Serializable, JsonAble {

    // -------------------- HTTP --------------------

    int HTTP_OK_CODE = 200;

    String HTTP_OK_MESSAGE = "success";

    int HTTP_ERROR_CODE = 500;

    String HTTP_ERROR_MESSAGE = "error";

    // -------------------- RPC --------------------

    int RPC_SUCCESS_CODE = 2000;

    String RPC_SUCCESS_MESSAGE = "success";

    int RPC_ERROR_CODE = 5000;

    String RPC_ERROR_MESSAGE = "error";

    String PRC_TRACE_PREFIX = "PRC-TRACE-ID-";

    // -------------------- URL --------------------

    int URL_NO_OPERATION = 1;

    int URL_REFRESH = 2;

    int URL_REDIRECT = 3;


    /**
     * 获取json
     *
     * @return json
     */
    @Override
    default String toJsonString() {
        return Jsons.toJsonWriteNull(this);
    }

}

package com.orion.lang.define.wrapper;

import com.orion.lang.able.IJsonObject;
import com.orion.lang.constant.KitConfig;

import java.io.Serializable;

/**
 * 包装标识接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/9/16 14:35
 */
public interface Wrapper<T> extends Serializable, IJsonObject {

    // -------------------- HTTP --------------------

    Integer HTTP_OK_CODE = KitConfig.get(WrapperConfig.HTTP_OK_CODE);

    String HTTP_OK_MESSAGE = KitConfig.get(WrapperConfig.HTTP_OK_MESSAGE);

    Integer HTTP_ERROR_CODE = KitConfig.get(WrapperConfig.HTTP_ERROR_CODE);

    String HTTP_ERROR_MESSAGE = KitConfig.get(WrapperConfig.HTTP_ERROR_MESSAGE);

    // -------------------- RPC --------------------

    Integer RPC_SUCCESS_CODE = KitConfig.get(WrapperConfig.RPC_SUCCESS_CODE);

    String RPC_SUCCESS_MESSAGE = KitConfig.get(WrapperConfig.RPC_SUCCESS_MESSAGE);

    Integer RPC_ERROR_CODE = KitConfig.get(WrapperConfig.RPC_ERROR_CODE);

    String RPC_ERROR_MESSAGE = KitConfig.get(WrapperConfig.RPC_ERROR_MESSAGE);

    String PRC_TRACE_PREFIX = KitConfig.get(WrapperConfig.PRC_TRACE_PREFIX);

    // -------------------- URL --------------------

    int URL_NO_OPERATION = KitConfig.get(WrapperConfig.URL_NO_OPERATION);

    int URL_REFRESH = KitConfig.get(WrapperConfig.URL_REFRESH);

    int URL_REDIRECT = KitConfig.get(WrapperConfig.URL_REDIRECT);

}

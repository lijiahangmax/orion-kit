package com.orion.lang.wrapper;

import java.io.Serializable;

/**
 * 包装标识接口
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/9/16 14:35
 */
public interface Wrapper<T> extends Serializable {

    // --------------- Restful ---------------

    int HTTP_OK_CODE = 200;

    String HTTP_OK_MESSAGE = "success";

    int HTTP_ERROR_CODE = 500;

    String HTTP_ERROR_MESSAGE = "error";

    // --------------- RPC ---------------

    int RPC_OK_CODE = 2000;

    String RPC_OK_MESSAGE = "success";

    int RPC_ERROR_CODE = 5000;

    String RPC_ERROR_MESSAGE = "error";

    String TRACE_PREFIX = "SESSION-TRACE-";

}

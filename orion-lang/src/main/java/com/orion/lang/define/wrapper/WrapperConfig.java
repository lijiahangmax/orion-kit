package com.orion.lang.define.wrapper;

import com.orion.lang.constant.KitConfig;

/**
 * wrapper 配置项
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/6 17:08
 */
public class WrapperConfig {

    public static final String HTTP_OK_CODE = "http.wrapper.ok.code";

    public static final String HTTP_OK_MESSAGE = "http.wrapper.ok.message";

    public static final String HTTP_ERROR_CODE = "http.wrapper.error.code";

    public static final String HTTP_ERROR_MESSAGE = "http.wrapper.error.message";

    public static final String RPC_SUCCESS_CODE = "rpc.wrapper.ok.code";

    public static final String RPC_SUCCESS_MESSAGE = "rpc.wrapper.ok.message";

    public static final String RPC_ERROR_CODE = "rpc.wrapper.error.code";

    public static final String RPC_ERROR_MESSAGE = "rpc.wrapper.error.message";

    public static final String PRC_TRACE_PREFIX = "rpc.wrapper.trace.prefix";

    public static final String URL_NO_OPERATION = "url.wrapper.no.opt";

    public static final String URL_REFRESH = "url.wrapper.refresh";

    public static final String URL_REDIRECT = "url.wrapper.redirect";

    // http wrapper
    static {
        KitConfig.put(HTTP_OK_CODE, 200);
        KitConfig.put(HTTP_OK_MESSAGE, "success");
        KitConfig.put(HTTP_ERROR_CODE, 500);
        KitConfig.put(HTTP_ERROR_MESSAGE, "error");
    }

    // rpc wrapper
    static {
        KitConfig.put(RPC_SUCCESS_CODE, 2000);
        KitConfig.put(RPC_SUCCESS_MESSAGE, "success");
        KitConfig.put(RPC_ERROR_CODE, 5000);
        KitConfig.put(RPC_ERROR_MESSAGE, "error");
        KitConfig.put(PRC_TRACE_PREFIX, "PRC-TRACE-");
    }

    // url wrapper
    static {
        KitConfig.put(URL_NO_OPERATION, 1);
        KitConfig.put(URL_REFRESH, 2);
        KitConfig.put(URL_REDIRECT, 3);
    }

}

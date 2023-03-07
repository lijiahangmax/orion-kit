package com.orion.lang;

import com.orion.lang.constant.KitConfig;

/**
 * orion-lang 配置初始化器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/6 18:26
 */
public final class KitLangConfiguration {

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

    public static final String PAGE_REQUEST_DEFAULT_LIMIT = "page.request.default.limit";

    public static final String PAGER_DEFAULT_LIMIT = "pager.default.limit";

    public static final String DATA_GRID_DEFAULT_LIMIT = "data.grid.default.limit";

    // http wrapper
    static {
        KitConfig.init(HTTP_OK_CODE, 200);
        KitConfig.init(HTTP_OK_MESSAGE, "success");
        KitConfig.init(HTTP_ERROR_CODE, 500);
        KitConfig.init(HTTP_ERROR_MESSAGE, "error");
    }

    // rpc wrapper
    static {
        KitConfig.init(RPC_SUCCESS_CODE, 2000);
        KitConfig.init(RPC_SUCCESS_MESSAGE, "success");
        KitConfig.init(RPC_ERROR_CODE, 5000);
        KitConfig.init(RPC_ERROR_MESSAGE, "error");
        KitConfig.init(PRC_TRACE_PREFIX, "PRC-TRACE-");
    }

    // url wrapper
    static {
        KitConfig.init(URL_NO_OPERATION, 1);
        KitConfig.init(URL_REFRESH, 2);
        KitConfig.init(URL_REDIRECT, 3);
    }

    // pager
    static {
        KitConfig.init(PAGE_REQUEST_DEFAULT_LIMIT, 10);
        KitConfig.override(PAGER_DEFAULT_LIMIT, 10);
        KitConfig.override(DATA_GRID_DEFAULT_LIMIT, 10);
    }

}

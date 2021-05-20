package com.orion.http.ok.ws;

/**
 * OkHttp WebSocket常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/10 11:25
 */
public class OkWebSocketConst {

    public static final String CLIENT_SESSION_ID_HEADER = "Mock-Client-Session-Id";

    public static final String SERVER_TEMP_SESSION_ID = "TEMP-";

    public static final int SERVER_FAIL_CLOSE_CODE = 3001;

    public static final String SERVER_FAIL_CLOSE_REASON = "Server 关闭Fail连接";

    public static final int SERVER_CLOSE_CODE = 3002;

    public static final String SERVER_CLOSE_REASON = "Server 关闭连接";

    public static final int SERVER_CLIENT_FULL_CLOSE_CODE = 3002;

    public static final String SERVER_CLIENT_FULL_CLOSE_REASON = "Server 连接已满 关闭连接";

    public static final int CLIENT_FAIL_CLOSE_CODE = 3004;

    public static final String CLIENT_FAIL_CLOSE_REASON = "Client 关闭Fail连接";

    public static final int CLIENT_CLOSE_CODE = 3005;

    public static final String CLIENT_CLOSE_REASON = "Client 关闭连接";

}

package com.orion.http.ok.ws;

/**
 * OkHttp WebSocket 常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/10 11:25
 */
public interface OkWebSocketConst {

    String CLIENT_SESSION_ID_HEADER = "Mock-Client-Session-Id";

    String SERVER_TEMP_SESSION_ID = "TEMP-";

    int SERVER_FAIL_CLOSE_CODE = 3001;

    String SERVER_FAIL_CLOSE_REASON = "Server 关闭Fail连接";

    int SERVER_CLOSE_CODE = 3002;

    String SERVER_CLOSE_REASON = "Server 关闭连接";

    int SERVER_CLIENT_FULL_CLOSE_CODE = 3002;

    String SERVER_CLIENT_FULL_CLOSE_REASON = "Server 连接已满 关闭连接";

    int CLIENT_FAIL_CLOSE_CODE = 3004;

    String CLIENT_FAIL_CLOSE_REASON = "Client 关闭Fail连接";

    int CLIENT_CLOSE_CODE = 3005;

    String CLIENT_CLOSE_REASON = "Client 关闭连接";

}

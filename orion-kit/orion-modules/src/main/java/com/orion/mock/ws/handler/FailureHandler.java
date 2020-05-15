package com.orion.mock.ws.handler;

import okhttp3.Response;
import okhttp3.WebSocket;

/**
 * 连接失败接口
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/9 21:53
 */
@FunctionalInterface
public interface FailureHandler {

    /**
     * 连接失败处理
     *
     * @param webSocket webSocket
     * @param t         error
     * @param response  response
     */
    void failure(WebSocket webSocket, Throwable t, Response response);

}

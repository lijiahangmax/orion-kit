package com.orion.http.ok.ws.handler;

import okhttp3.Response;
import okhttp3.WebSocket;

/**
 * 打开连接接口
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/9 21:55
 */
@FunctionalInterface
public interface OpenHandler {

    /**
     * 打开连接调用
     *
     * @param webSocket webSocket
     * @param response  response
     */
    void open(WebSocket webSocket, Response response);

}

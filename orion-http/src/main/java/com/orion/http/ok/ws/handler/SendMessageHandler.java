package com.orion.http.ok.ws.handler;

import okhttp3.WebSocket;

/**
 * 发送消息接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/9 21:51
 */
@FunctionalInterface
public interface SendMessageHandler {

    /**
     * 发送webSocket消息
     *
     * @param webSocket webSocket
     * @param message   message
     */
    void send(WebSocket webSocket, String message);

}

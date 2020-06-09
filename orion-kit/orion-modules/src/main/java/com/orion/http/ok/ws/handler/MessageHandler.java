package com.orion.http.ok.ws.handler;

import okhttp3.WebSocket;

/**
 * 接收消息接口
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/9 21:51
 */
@FunctionalInterface
public interface MessageHandler {

    /**
     * 接收webSocket消息
     *
     * @param webSocket webSocket
     * @param message   message
     */
    void message(WebSocket webSocket, String message);

}

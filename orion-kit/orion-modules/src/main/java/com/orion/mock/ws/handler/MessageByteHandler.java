package com.orion.mock.ws.handler;

import okhttp3.WebSocket;
import okio.ByteString;

/**
 * 接收消息接口
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/9 23:31
 */
@FunctionalInterface
public interface MessageByteHandler {

    /**
     * 发送消息接口
     *
     * @param webSocket webSocket
     * @param bytes     bytes
     */
    void message(WebSocket webSocket, ByteString bytes);

}

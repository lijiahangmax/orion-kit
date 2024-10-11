/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.orion.http.ok.ws;

import com.orion.http.ok.ws.handler.*;
import com.orion.lang.constant.Const;
import com.orion.lang.id.Sequences;
import com.orion.lang.utils.Threads;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OkHttp WebSocket 服务端
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/9 15:35
 */
public class OkWebSocketServer extends WebSocketListener {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OkWebSocketServer.class);

    /**
     * OkHttp WebSocket Server
     */
    private final MockWebServer server;

    /**
     * address
     */
    private final InetAddress address;

    /**
     * port
     */
    private final int port;

    /**
     * 调用shutdown等待客户端的时间 ms
     */
    private int shutDownWaitClientTime;

    /**
     * 客户端最大连接客户端数量
     */
    private int maxClientCount;

    /**
     * 打开连接接口
     */
    private OpenHandler openHandler;

    /**
     * 接收 text 消息接口
     */
    private MessageHandler messageHandler;

    /**
     * 接收 byte 消息接口
     */
    private MessageByteHandler messageByteHandler;

    /**
     * 关闭连接接口
     */
    private CloseHandler closeHandler;

    /**
     * 连接失败接口
     */
    private FailureHandler failureHandler;

    /**
     * 关闭接口
     */
    private ShutdownHandler shutdownHandler;

    /**
     * 发送 text 消息接口
     */
    private SendMessageHandler sendTextHandler;

    /**
     * 发送 byte 消息接口
     */
    private SendMessageByteHandler sendByteHandler;

    /**
     * 客户端
     */
    private final Map<String, WebSocket> clients;

    public OkWebSocketServer(int port) {
        this(null, port);
    }

    public OkWebSocketServer(InetAddress address, int port) {
        this.server = new MockWebServer();
        this.address = address;
        this.port = port;
        this.shutDownWaitClientTime = Const.MS_S_3;
        this.clients = new ConcurrentHashMap<>();
    }

    /**
     * 启动server
     *
     * @throws IOException IOException
     */
    public void start() throws IOException {
        if (address != null) {
            server.start(address, port);
        } else {
            server.start(port);
        }
        LOGGER.info("WebSocketServer 初始化完成 host: [{}], port: [{}]", server.getHostName(), server.getPort());
        server.enqueue(new MockResponse().withWebSocketUpgrade(this));
    }

    /**
     * 停止 server
     *
     * @throws IOException IOException
     */
    public void shutdown() throws IOException {
        if (shutdownHandler != null) {
            shutdownHandler.shutdown(clients);
        }
        for (WebSocket value : clients.values()) {
            value.close(OkWebSocketConst.SERVER_CLOSE_CODE, OkWebSocketConst.SERVER_CLOSE_REASON);
        }
        clients.clear();
        // 等待客户端关闭
        Threads.sleep(shutDownWaitClientTime);
        server.shutdown();
    }

    /**
     * 关闭连接最大等待时长
     *
     * @param timeout timeout
     * @return this
     */
    public OkWebSocketServer shutDownWaitClientTime(int timeout) {
        if (timeout >= Const.MS_S_1) {
            this.shutDownWaitClientTime = timeout;
        }
        return this;
    }

    /**
     * 设置最大连接数
     *
     * @param maxCount 最大连接数
     * @return this
     */
    public OkWebSocketServer maxClientCount(int maxCount) {
        if (maxCount > 0) {
            this.maxClientCount = maxCount;
        }
        return this;
    }

    /**
     * 广播
     */
    public void broadcast(String text) {
        for (WebSocket webSocket : clients.values()) {
            if (sendTextHandler != null) {
                sendTextHandler.send(webSocket, text);
            }
            webSocket.send(text);
        }
    }

    public void broadcast(byte[] bs) {
        this.broadcast(ByteString.of(bs));
    }

    public void broadcast(byte[] bs, int offset, int len) {
        this.broadcast(ByteString.of(bs, offset, len));
    }

    public void broadcast(ByteString bs) {
        for (WebSocket webSocket : clients.values()) {
            if (sendByteHandler != null) {
                sendByteHandler.send(webSocket, bs);
            }
            webSocket.send(bs);
        }
    }

    /**
     * 点播
     */
    public void spread(String sessionId, String text) {
        WebSocket webSocket = clients.get(sessionId);
        if (webSocket == null) {
            return;
        }
        this.spread(webSocket, text);
    }

    public void spread(String sessionId, byte[] bs) {
        this.spread(sessionId, ByteString.of(bs));
    }

    public void spread(String sessionId, byte[] bs, int offset, int len) {
        this.spread(sessionId, ByteString.of(bs, offset, len));
    }

    public void spread(String sessionId, ByteString bs) {
        WebSocket webSocket = clients.get(sessionId);
        if (webSocket == null) {
            return;
        }
        this.spread(webSocket, bs);
    }

    public void spread(WebSocket webSocket, String text) {
        if (webSocket == null) {
            return;
        }
        if (sendTextHandler != null) {
            sendTextHandler.send(webSocket, text);
        }
        webSocket.send(text);
    }

    public void spread(WebSocket webSocket, byte[] bs) {
        this.spread(webSocket, ByteString.of(bs));
    }

    public void spread(WebSocket webSocket, byte[] bs, int offset, int len) {
        this.spread(webSocket, ByteString.of(bs, offset, len));
    }

    public void spread(WebSocket webSocket, ByteString bs) {
        if (webSocket == null) {
            return;
        }
        if (sendByteHandler != null) {
            sendByteHandler.send(webSocket, bs);
        }
        webSocket.send(bs);
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        if (maxClientCount != 0 && clients.size() >= maxClientCount) {
            webSocket.close(OkWebSocketConst.SERVER_CLIENT_FULL_CLOSE_CODE, OkWebSocketConst.SERVER_CLIENT_FULL_CLOSE_REASON);
            LOGGER.warn("WebSocketServer-Client建立连接失败-超出最大数量 response: [{}]", response);
            server.enqueue(new MockResponse().withWebSocketUpgrade(this));
            return;
        }
        LOGGER.info("WebSocketServer-Client已建立连接 response: [{}]", response);
        if (openHandler != null) {
            openHandler.open(webSocket, response);
        }
        String clientSessionId = response.request().header(OkWebSocketConst.CLIENT_SESSION_ID_HEADER);
        if (clientSessionId == null) {
            clientSessionId = (OkWebSocketConst.SERVER_TEMP_SESSION_ID + Sequences.nextId());
        }
        clients.put(clientSessionId, webSocket);
        LOGGER.info("WebSocketServer-连接ClientSessionId: [{}], 当前连接数量: [{}]", clientSessionId, clients.size());
        server.enqueue(new MockResponse().withWebSocketUpgrade(this));
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        LOGGER.debug("WebSocketServer-收到Client Text信息 msg: '{}'", text);
        if (messageHandler != null) {
            messageHandler.message(webSocket, text);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        LOGGER.debug("WebSocketServer收到Client Byte信息 size: '{}'", bytes.size());
        if (messageByteHandler != null) {
            messageByteHandler.message(webSocket, bytes);
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        LOGGER.info("WebSocketServer-准备关闭Client连接 code: {}, reason: '{}'", code, reason);
        if (closeHandler != null) {
            closeHandler.close(webSocket, code, reason);
        }
        String remove = OkWebSocketServer.this.remove(webSocket);
        boolean close = webSocket.close(code, reason);
        LOGGER.info("WebSocketServer-关闭Client连接完成 code: {}, reason: '{}', close: {}, remove: {}", code, reason, close, remove);
        LOGGER.info("WebSocketServer-当前Client连接数量: [{}]", clients.size());
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        LOGGER.info("WebSocketServer-Client连接已关闭, code: {}, reason: '{}', 当前连接数量: [{}]", code, reason, clients.size());
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        if (failureHandler != null) {
            failureHandler.failure(webSocket, t, response);
        }
        String remove = OkWebSocketServer.this.remove(webSocket);
        boolean close = webSocket.close(OkWebSocketConst.SERVER_FAIL_CLOSE_CODE, OkWebSocketConst.SERVER_FAIL_CLOSE_REASON);
        LOGGER.error("WebSocketServer-处理Client连接失败 close: {}, remove: {}, error: {}-{}, response: {}", close, remove, t.getClass().getName(), t.getMessage(), response);
    }

    private String remove(WebSocket client) {
        String key = null;
        Set<Map.Entry<String, WebSocket>> entries = clients.entrySet();
        for (Map.Entry<String, WebSocket> entry : entries) {
            if (entry.getValue().equals(client)) {
                key = entry.getKey();
                break;
            }
        }
        if (key != null) {
            clients.remove(key);
        }
        return key;
    }

    public OkWebSocketServer openHandler(OpenHandler openHandler) {
        this.openHandler = openHandler;
        return this;
    }

    public OkWebSocketServer messageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        return this;
    }

    public OkWebSocketServer messageByteHandler(MessageByteHandler messageByteHandler) {
        this.messageByteHandler = messageByteHandler;
        return this;
    }

    public OkWebSocketServer sendMessageByteHandler(SendMessageByteHandler sendMessageByteHandler) {
        this.sendByteHandler = sendMessageByteHandler;
        return this;
    }

    public OkWebSocketServer sendTextHandler(SendMessageHandler sendTextHandler) {
        this.sendTextHandler = sendTextHandler;
        return this;
    }

    public OkWebSocketServer closeHandler(CloseHandler closeHandler) {
        this.closeHandler = closeHandler;
        return this;
    }

    public OkWebSocketServer failureHandler(FailureHandler failureHandler) {
        this.failureHandler = failureHandler;
        return this;
    }

    public OkWebSocketServer shutdownHandler(ShutdownHandler shutdownHandler) {
        this.shutdownHandler = shutdownHandler;
        return this;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public int getClientCount() {
        return clients.size();
    }

}

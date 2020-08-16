package com.orion.http.ok.ws;

import com.orion.http.ok.ws.handler.*;
import com.orion.id.Sequences;
import com.orion.utils.Threads;
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
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mock WebSocket服务端
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/9 15:35
 */
@SuppressWarnings("ALL")
public class MockWebSocketServer {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MockWebSocketServer.class);

    /**
     * mockServer
     */
    private final MockWebServer MOCK_WEB_SERVER;

    /**
     * 接收数量
     */
    private int receiveCount;

    /**
     * 发送数量
     */
    private int sendCount;

    /**
     * address
     */
    private InetAddress address;

    /**
     * port
     */
    private int port;

    /**
     * 调用shutdown等待客户端的时间 ms
     */
    private int shutDownWaitClientClientTime = 10000;

    /**
     * 客户端最大连接客户端数量
     */
    private int maxClientCount;

    /**
     * 打开连接接口
     */
    private OpenHandler openHandler;

    /**
     * 接收Text消息接口
     */
    private MessageHandler messageHandler;

    /**
     * 接收Byte消息接口
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
     * 发送Text消息接口
     */
    private SendMessageHandler sendMessageHandler;

    /**
     * 发送Byte消息接口
     */
    private SendMessageByteHandler sendMessageByteHandler;

    /**
     * 客户端
     */
    private Map<WebSocket, String> clients = new ConcurrentHashMap<>();

    public MockWebSocketServer(int port) {
        this(null, port);
    }

    public MockWebSocketServer(InetAddress address, int port) {
        MOCK_WEB_SERVER = new MockWebServer();
        this.address = address;
        this.port = port;
    }

    /**
     * 启动server
     *
     * @throws IOException IOException
     */
    public void start() throws IOException {
        if (address != null) {
            MOCK_WEB_SERVER.start(address, port);
        } else {
            MOCK_WEB_SERVER.start(port);
        }
        LOGGER.info("WebSocketServer 初始化完成 host: [{}], port: [{}]", MOCK_WEB_SERVER.getHostName(), MOCK_WEB_SERVER.getPort());
        initMockServer();
    }

    /**
     * 停止server
     *
     * @throws IOException IOException
     */
    public void shutdown() throws IOException {
        if (shutdownHandler != null) {
            shutdownHandler.shutdown(clients);
        }
        for (Map.Entry<WebSocket, String> entry : clients.entrySet()) {
            entry.getKey().close(MockWebSocketConst.SERVER_CLOSE_CODE, MockWebSocketConst.SERVER_CLOSE_REASON);
        }
        for (WebSocket webSocket : clients.keySet()) {
            clients.remove(webSocket);
        }
        // 等待客户端关闭
        Threads.sleep(shutDownWaitClientClientTime);
        MOCK_WEB_SERVER.shutdown();
    }

    /**
     * 设置内容最大长度
     *
     * @param size size
     * @return this
     */
    public MockWebSocketServer shutDownWaitClientClientTime(int shutdownTime) {
        if (shutdownTime > 0 && shutdownTime >= 1000) {
            this.shutDownWaitClientClientTime = shutdownTime;
        }
        return this;
    }

    /**
     * 设置最大连接数
     *
     * @param maxCount 最大连接数
     * @return this
     */
    public MockWebSocketServer maxClientCount(int maxCount) {
        if (maxCount > 0) {
            this.maxClientCount = maxCount;
        }
        return this;
    }

    /**
     * 广播
     */
    public void broadcast(String text) {
        for (WebSocket webSocket : clients.keySet()) {
            if (sendMessageHandler != null) {
                sendMessageHandler.send(webSocket, text);
            }
            webSocket.send(text);
            sendCount++;
        }
    }

    public void broadcast(byte[] bs) {
        broadcast(ByteString.of(bs));
    }

    public void broadcast(byte[] bs, int offset, int len) {
        broadcast(ByteString.of(bs, offset, len));
    }

    public void broadcast(ByteString bs) {
        for (WebSocket webSocket : clients.keySet()) {
            if (sendMessageByteHandler != null) {
                sendMessageByteHandler.send(webSocket, bs);
            }
            webSocket.send(bs);
            sendCount++;
        }
    }

    public void broadcast(String text, String... sessionId) {
        if (sessionId == null) {
            return;
        }
        for (Map.Entry<WebSocket, String> entry : clients.entrySet()) {
            for (String s : sessionId) {
                if (entry.getValue().equals(s)) {
                    WebSocket webSocket = entry.getKey();
                    if (sendMessageHandler != null) {
                        sendMessageHandler.send(webSocket, text);
                    }
                    webSocket.send(text);
                    sendCount++;
                }
            }
        }
    }

    public void broadcast(byte[] bs, String... sessionId) {
        broadcast(ByteString.of(bs), sessionId);
    }

    public void broadcast(byte[] bs, int offset, int len, String... sessionId) {
        broadcast(ByteString.of(bs, offset, len), sessionId);
    }

    public void broadcast(ByteString bs, String... sessionId) {
        if (sessionId == null) {
            return;
        }
        for (Map.Entry<WebSocket, String> entry : clients.entrySet()) {
            for (String s : sessionId) {
                if (entry.getValue().equals(s)) {
                    WebSocket webSocket = entry.getKey();
                    if (sendMessageByteHandler != null) {
                        sendMessageByteHandler.send(webSocket, bs);
                    }
                    webSocket.send(bs);
                    sendCount++;
                }
            }
        }
    }

    /**
     * 点播
     */
    public void spread(WebSocket webSocket, String text) {
        if (webSocket == null) {
            return;
        }
        if (sendMessageHandler != null) {
            sendMessageHandler.send(webSocket, text);
        }
        webSocket.send(text);
        sendCount++;
    }

    public void spread(WebSocket webSocket, byte[] bs) {
        spread(webSocket, ByteString.of(bs));
    }

    public void spread(WebSocket webSocket, byte[] bs, int offset, int len) {
        spread(webSocket, ByteString.of(bs, offset, len));
    }

    public void spread(WebSocket webSocket, ByteString bs) {
        if (webSocket == null) {
            return;
        }
        if (sendMessageByteHandler != null) {
            sendMessageByteHandler.send(webSocket, bs);
        }
        webSocket.send(bs);
        sendCount++;
    }

    public void spread(String sessionId, String text) {
        if (sessionId == null) {
            return;
        }
        for (Map.Entry<WebSocket, String> entry : clients.entrySet()) {
            if (entry.getValue().equals(sessionId)) {
                WebSocket webSocket = entry.getKey();
                if (sendMessageHandler != null) {
                    sendMessageHandler.send(webSocket, text);
                }
                webSocket.send(text);
                sendCount++;
            }
        }
    }

    public void spread(String sessionId, byte[] bs) {
        spread(sessionId, ByteString.of(bs));
    }

    public void spread(String sessionId, byte[] bs, int offset, int len) {
        spread(sessionId, ByteString.of(bs, offset, len));
    }

    public void spread(String sessionId, ByteString bs) {
        if (sessionId == null) {
            return;
        }
        for (Map.Entry<WebSocket, String> entry : clients.entrySet()) {
            if (entry.getValue().equals(sessionId)) {
                WebSocket webSocket = entry.getKey();
                if (sendMessageByteHandler != null) {
                    sendMessageByteHandler.send(webSocket, bs);
                }
                webSocket.send(bs);
                sendCount++;
            }
        }
    }

    /**
     * 初始化
     */
    private void initMockServer() {
        WebSocketListener webSocketListener = new WebSocketListener() {

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                if (maxClientCount != 0 && clients.size() >= maxClientCount) {
                    webSocket.close(MockWebSocketConst.SERVER_CLIENT_FULL_CLOSE_CODE, MockWebSocketConst.SERVER_CLIENT_FULL_CLOSE_REASON);
                    LOGGER.warn("WebSocketServer-Client建立连接失败-超出最大数量 response: [{}]", response);
                    MOCK_WEB_SERVER.enqueue(new MockResponse().withWebSocketUpgrade(this));
                    return;
                }
                LOGGER.info("WebSocketServer-Client已建立连接 response: [{}]", response);
                if (openHandler != null) {
                    openHandler.open(webSocket, response);
                }
                String clientSessionId = response.request().header(MockWebSocketConst.CLIENT_SESSION_ID_HEADER);
                if (clientSessionId == null) {
                    clientSessionId = (MockWebSocketConst.SERVER_TEMP_SESSION_ID + Sequences.createId());
                }
                clients.put(webSocket, clientSessionId);
                LOGGER.info("WebSocketServer-连接ClientSessionId: [{}], 当前连接数量: [{}]", clientSessionId, clients.size());
                MOCK_WEB_SERVER.enqueue(new MockResponse().withWebSocketUpgrade(this));
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                LOGGER.info("WebSocketServer-收到Client Text信息 msg: '{}'", text);
                if (messageHandler != null) {
                    messageHandler.message(webSocket, text);
                }
                receiveCount++;
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                LOGGER.info("WebSocketServer收到Client Byte信息 size: '{}'", bytes.size());
                if (messageByteHandler != null) {
                    messageByteHandler.message(webSocket, bytes);
                }
                receiveCount++;
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                LOGGER.info("WebSocketServer-准备关闭Client连接 code: {}, reason: '{}'", code, reason);
                if (closeHandler != null) {
                    closeHandler.close(webSocket, code, reason);
                }
                String remove = clients.remove(webSocket);
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
                String remove = clients.remove(webSocket);
                boolean close = webSocket.close(MockWebSocketConst.SERVER_FAIL_CLOSE_CODE, MockWebSocketConst.SERVER_FAIL_CLOSE_REASON);
                LOGGER.error("WebSocketServer-处理Client连接失败 close: {}, remove: {}, error: {}-{}, response: {}", close, remove, t.getClass().getName(), t.getMessage(), response);
            }

        };
        MOCK_WEB_SERVER.enqueue(new MockResponse().withWebSocketUpgrade(webSocketListener));
    }

    public MockWebSocketServer openHandler(OpenHandler openHandler) {
        this.openHandler = openHandler;
        return this;
    }

    public MockWebSocketServer messageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        return this;
    }

    public MockWebSocketServer messageByteHandler(MessageByteHandler messageByteHandler) {
        this.messageByteHandler = messageByteHandler;
        return this;
    }

    public MockWebSocketServer sendMessageHandler(SendMessageHandler sendMessageHandler) {
        this.sendMessageHandler = sendMessageHandler;
        return this;
    }

    public MockWebSocketServer sendMessageByteHandler(SendMessageByteHandler sendMessageByteHandler) {
        this.sendMessageByteHandler = sendMessageByteHandler;
        return this;
    }

    public MockWebSocketServer closeHandler(CloseHandler closeHandler) {
        this.closeHandler = closeHandler;
        return this;
    }

    public MockWebSocketServer failureHandler(FailureHandler failureHandler) {
        this.failureHandler = failureHandler;
        return this;
    }

    public MockWebSocketServer shutdownHandler(ShutdownHandler shutdownHandler) {
        this.shutdownHandler = shutdownHandler;
        return this;
    }

    public int getReceiveCount() {
        return receiveCount;
    }

    public int getSendCount() {
        return sendCount;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public int getClientCount() {
        return this.clients.size();
    }

    /**
     * 获取webSocket对应的sessionId
     *
     * @param webSocket webSocket
     * @return sessionId
     */
    public String getSessionId(WebSocket webSocket) {
        for (Map.Entry<WebSocket, String> entry : clients.entrySet()) {
            if (entry.getKey().equals(webSocket)) {
                return entry.getValue();
            }
        }
        return null;
    }

}

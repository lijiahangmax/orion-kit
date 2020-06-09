package com.orion.http.ok.ws;

import com.orion.id.Sequences;
import com.orion.http.ok.MockClient;
import com.orion.http.ok.ws.handler.*;
import com.orion.utils.Threads;
import okhttp3.*;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketException;

/**
 * Mock WebSocket客户端
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/9 15:58
 */
@SuppressWarnings("ALL")
public class MockWebSocketClient {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MockWebSocketClient.class);

    /**
     * wsURL
     */
    private final String URL;

    /**
     * ws id
     */
    private final String SESSION_ID;

    /**
     * OkHttpClient
     */
    private final OkHttpClient CLIENT = MockClient.getClient();

    /**
     * webSocket
     */
    private WebSocket webSocket;

    /**
     * 接收数量
     */
    private int receiveCount;

    /**
     * 发送数量
     */
    private int sendCount;

    /**
     * 异常断开连接重试次数
     */
    private int errorReconnectionTimes;

    /**
     * 当前重试次数
     */
    private volatile int nowReconnectionTimes;

    /**
     * 当前连接状态 0未连接 1已连接 2重试中 3已重连
     */
    private volatile int connectionState;

    /**
     * 异常断开连接重试间隔 ms
     */
    private int errorReconnectionInterval = 30000;

    /**
     * 最后一次关闭码
     */
    private int lastCloseCode;

    /**
     * 最后一次关闭信息
     */
    private String lastCloseReason;

    /**
     * 最后一次异常信息
     */
    private Throwable lastThrowable;

    /**
     * 打开连接接口
     */
    private OpenHandler openHandler;

    /**
     * 接收消息接口
     */
    private MessageHandler messageHandler;

    /**
     * 接收byte消息接口
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
     * 发送Text消息接口
     */
    private SendMessageHandler sendMessageHandler;

    /**
     * 发送Byte消息接口
     */
    private SendMessageByteHandler sendMessageByteHandler;

    public MockWebSocketClient(String url) {
        this.URL = url;
        this.SESSION_ID = Sequences.createId() + "";
    }

    /**
     * 启动Client
     */
    public void start() {
        connectionState = 1;
        initMockClient();
    }

    /**
     * 发送消息
     *
     * @param text text
     */
    public void send(String text) {
        if (webSocket != null) {
            if (sendMessageHandler != null) {
                sendMessageHandler.send(this.webSocket, text);
            }
            webSocket.send(text);
            sendCount++;
        }
    }

    /**
     * 发送消息
     *
     * @param byteString byteString
     */
    public void send(ByteString byteString) {
        if (webSocket != null) {
            if (sendMessageByteHandler != null) {
                sendMessageByteHandler.send(this.webSocket, byteString);
            }
            webSocket.send(byteString);
            sendCount++;
        }
    }

    public void send(byte[] bs) {
        send(ByteString.of(bs));
    }

    public void send(byte[] bs, int offset, int len) {
        send(ByteString.of(bs, offset, len));
    }

    /**
     * 关闭client连接
     */
    public void close() {
        if (webSocket != null) {
            webSocket.close(MockWebSocketConst.CLIENT_CLOSE_CODE, MockWebSocketConst.CLIENT_CLOSE_REASON);
        }
    }

    /**
     * 关闭client连接
     *
     * @param code   code
     * @param reason reason
     */
    public void close(String reason) {
        if (webSocket != null) {
            webSocket.close(MockWebSocketConst.CLIENT_CLOSE_CODE, reason);
        }
    }

    /**
     * 异常关闭重连
     *
     * @param times 重试次数
     * @return this
     */
    public MockWebSocketClient reconnection(int times) {
        this.errorReconnectionTimes = times;
        return this;
    }

    /**
     * 异常关闭重连
     *
     * @param times    重试次数
     * @param interval 重试间隔ms
     * @return this
     */
    public MockWebSocketClient reconnection(int times, int interval) {
        this.errorReconnectionTimes = times;
        this.errorReconnectionInterval = interval;
        return this;
    }

    private void initMockClient() {
        Request request = new Request.Builder().url(this.URL).addHeader(MockWebSocketConst.CLIENT_SESSION_ID_HEADER, SESSION_ID).build();
        WebSocketListener webSocketListener = new WebSocketListener() {

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                if (connectionState == 2) {
                    LOGGER.warn("WebSocketClient 连接异常断开-重连成功-尝试次数 {}次", nowReconnectionTimes);
                    nowReconnectionTimes = 0;
                    connectionState = 3;
                } else {
                    LOGGER.info("WebSocketClient 已建立连接 response: [{}]", response);
                    if (openHandler != null) {
                        openHandler.open(webSocket, response);
                    }
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                LOGGER.info("WebSocketClient 收到Server Text信息 msg: '{}'", text);
                if (messageHandler != null) {
                    messageHandler.message(webSocket, text);
                }
                receiveCount++;
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                LOGGER.info("WebSocketClient 收到Server Byte信息 size: '{}'", bytes.size());
                if (messageByteHandler != null) {
                    messageByteHandler.message(webSocket, bytes);
                }
                receiveCount++;
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                LOGGER.info("WebSocketClient 准备关闭连接 code: {}, reason: '{}'", code, reason);
                if (closeHandler != null) {
                    closeHandler.close(webSocket, code, reason);
                }
                boolean close = webSocket.close(code, reason);
                LOGGER.info("WebSocketClient 关闭连接完成 code: {}, reason: '{}', close: {}", code, reason, close);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                LOGGER.info("WebSocketClient 连接已关闭, code: {}, reason: '{}'", code, reason);
                lastCloseCode = code;
                lastCloseReason = reason;
                connectionState = 0;
            }

            @Override
            public void onFailure(WebSocket failWebSocket, Throwable t, Response response) {
                if (failureHandler != null) {
                    failureHandler.failure(failWebSocket, t, response);
                }
                // 需要重连并且未重连
                if ((connectionState == 1 || connectionState == 3) && nowReconnectionTimes == 0 && errorReconnectionTimes != 0) {
                    connectionState = 2;
                }
                boolean close = failWebSocket.close(MockWebSocketConst.CLIENT_FAIL_CLOSE_CODE, MockWebSocketConst.CLIENT_FAIL_CLOSE_REASON);
                lastCloseCode = MockWebSocketConst.CLIENT_FAIL_CLOSE_CODE;
                lastCloseReason = MockWebSocketConst.CLIENT_FAIL_CLOSE_REASON;
                lastThrowable = t;
                LOGGER.error("WebSocketClient 处理连接失败 close: {}, error: {}-{}, response: {}", close, t.getClass().getName(), t.getMessage(), response);
                if (connectionState == 2 && (t instanceof SocketException || t instanceof EOFException || t instanceof ConnectException)) {
                    // 异常断开
                    if (nowReconnectionTimes < errorReconnectionTimes && errorReconnectionTimes - nowReconnectionTimes > 0) {
                        Threads.sleep(errorReconnectionInterval);
                        webSocket = CLIENT.newWebSocket(request, this);
                        ++nowReconnectionTimes;
                        LOGGER.warn("WebSocketClient 连接异常断开-第{}次尝试重新连接", nowReconnectionTimes);
                    } else {
                        LOGGER.warn("WebSocketClient 连接异常断开-无法重连");
                    }
                }
            }

        };
        this.webSocket = CLIENT.newWebSocket(request, webSocketListener);
    }

    public MockWebSocketClient openHandler(OpenHandler openHandler) {
        this.openHandler = openHandler;
        return this;
    }

    public MockWebSocketClient messageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        return this;
    }

    public MockWebSocketClient messageByteHandler(MessageByteHandler messageByteHandler) {
        this.messageByteHandler = messageByteHandler;
        return this;
    }

    public MockWebSocketClient closeHandler(CloseHandler closeHandler) {
        this.closeHandler = closeHandler;
        return this;
    }

    public MockWebSocketClient failureHandler(FailureHandler failureHandler) {
        this.failureHandler = failureHandler;
        return this;
    }

    public MockWebSocketClient sendMessageHandler(SendMessageHandler sendMessageHandler) {
        this.sendMessageHandler = sendMessageHandler;
        return this;
    }

    public MockWebSocketClient sendMessageByteHandler(SendMessageByteHandler sendMessageByteHandler) {
        this.sendMessageByteHandler = sendMessageByteHandler;
        return this;
    }

    public int getReceiveCount() {
        return receiveCount;
    }

    public int getSendCount() {
        return sendCount;
    }

    public String getURL() {
        return URL;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public String getSessionId() {
        return SESSION_ID;
    }

    public int getErrorReconnectionTimes() {
        return errorReconnectionTimes;
    }

    public int getErrorReconnectionInterval() {
        return errorReconnectionInterval;
    }

    public int getLastCloseCode() {
        return lastCloseCode;
    }

    public String getLastCloseReason() {
        return lastCloseReason;
    }

    public Throwable getLastThrowable() {
        return lastThrowable;
    }

    public int getNowReconnectionTimes() {
        return nowReconnectionTimes;
    }

    public int getConnectionState() {
        return connectionState;
    }

}

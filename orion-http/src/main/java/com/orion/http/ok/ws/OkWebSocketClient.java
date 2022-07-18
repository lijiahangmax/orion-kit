package com.orion.http.ok.ws;

import com.orion.http.ok.OkRequests;
import com.orion.http.ok.ws.handler.*;
import com.orion.lang.constant.Const;
import com.orion.lang.id.Sequences;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Threads;
import okhttp3.*;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.net.SocketException;

/**
 * OkHttp WebSocket 客户端
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/9 15:58
 */
public class OkWebSocketClient extends WebSocketListener {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OkWebSocketClient.class);

    /**
     * url
     */
    private final String url;

    /**
     * ws id
     */
    private final String sessionId;

    /**
     * OkHttpClient
     */
    private final OkHttpClient client;

    /**
     * webSocket
     */
    private WebSocket webSocket;

    /**
     * 异常断开连接重试次数
     */
    private int errorReconnectionTimes;

    /**
     * 当前重试次数
     */
    private int nowReconnectionTimes;

    /**
     * 当前连接状态 0未连接 1已连接 2重试中 3已重连
     */
    private volatile int connectionState;

    /**
     * 异常断开连接重试间隔 ms
     */
    private int errorReconnectionInterval;

    /**
     * 最后一次关闭码
     */
    private int closeCode;

    /**
     * 最后一次关闭信息
     */
    private String closeReason;

    /**
     * 最后一次异常信息
     */
    private Throwable throwable;

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
    private Request request;

    public OkWebSocketClient(String url) {
        this.url = url;
        this.sessionId = Sequences.nextId() + Strings.EMPTY;
        this.client = OkRequests.getClient();
    }

    /**
     * 启动 client
     */
    public void start() {
        this.connectionState = 1;
        this.errorReconnectionInterval = Const.MS_S_30;
        this.request = new Request.Builder()
                .url(url)
                .addHeader(OkWebSocketConst.CLIENT_SESSION_ID_HEADER, sessionId)
                .build();
        this.webSocket = client.newWebSocket(request, this);
    }

    /**
     * 发送消息
     *
     * @param text text
     */
    public void send(String text) {
        if (webSocket != null) {
            if (sendMessageHandler != null) {
                sendMessageHandler.send(webSocket, text);
            }
            webSocket.send(text);
        }
    }

    public void send(byte[] bs) {
        this.send(ByteString.of(bs));
    }

    public void send(byte[] bs, int offset, int len) {
        this.send(ByteString.of(bs, offset, len));
    }

    /**
     * 发送消息
     *
     * @param byteString byteString
     */
    public void send(ByteString byteString) {
        if (webSocket != null) {
            if (sendMessageByteHandler != null) {
                sendMessageByteHandler.send(webSocket, byteString);
            }
            webSocket.send(byteString);
        }
    }

    /**
     * 关闭 client 连接
     */
    public void close() {
        this.close(OkWebSocketConst.CLIENT_CLOSE_CODE, OkWebSocketConst.CLIENT_CLOSE_REASON);
    }

    /**
     * 关闭 client 连接
     *
     * @param code   code
     * @param reason reason
     */
    public void close(int code, String reason) {
        if (webSocket != null) {
            webSocket.close(code, reason);
        }
    }

    /**
     * 异常关闭重连
     *
     * @param times 重试次数
     * @return this
     */
    public OkWebSocketClient reconnection(int times) {
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
    public OkWebSocketClient reconnection(int times, int interval) {
        this.errorReconnectionTimes = times;
        this.errorReconnectionInterval = interval;
        return this;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        if (connectionState == 2) {
            LOGGER.warn("WebSocketClient 连接异常断开-重连成功-尝试次数 {}次", nowReconnectionTimes);
            this.nowReconnectionTimes = 0;
            this.connectionState = 3;
        } else {
            LOGGER.info("WebSocketClient 已建立连接 response: [{}]", response);
            if (openHandler != null) {
                openHandler.open(webSocket, response);
            }
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        LOGGER.debug("WebSocketClient 收到Server Text信息 msg: '{}'", text);
        if (messageHandler != null) {
            messageHandler.message(webSocket, text);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        LOGGER.debug("WebSocketClient 收到Server Byte信息 size: '{}'", bytes.size());
        if (messageByteHandler != null) {
            messageByteHandler.message(webSocket, bytes);
        }
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
        this.closeCode = code;
        this.closeReason = reason;
        this.connectionState = 0;
    }

    @Override
    public void onFailure(WebSocket failWebSocket, Throwable t, Response response) {
        if (failureHandler != null) {
            failureHandler.failure(failWebSocket, t, response);
        }
        // 需要重连并且未重连
        if ((connectionState == 1 || connectionState == 3) && nowReconnectionTimes == 0 && errorReconnectionTimes != 0) {
            this.connectionState = 2;
        }
        boolean close = failWebSocket.close(OkWebSocketConst.CLIENT_FAIL_CLOSE_CODE, OkWebSocketConst.CLIENT_FAIL_CLOSE_REASON);
        this.closeCode = OkWebSocketConst.CLIENT_FAIL_CLOSE_CODE;
        this.closeReason = OkWebSocketConst.CLIENT_FAIL_CLOSE_REASON;
        this.throwable = t;
        LOGGER.error("WebSocketClient 处理连接失败 close: {}, error: {}-{}, response: {}", close, t.getClass().getName(), t.getMessage(), response);
        if (connectionState == 2 && (t instanceof SocketException || t instanceof EOFException)) {
            // 异常断开
            if (nowReconnectionTimes < errorReconnectionTimes && errorReconnectionTimes - nowReconnectionTimes > 0) {
                Threads.sleep(errorReconnectionInterval);
                webSocket = client.newWebSocket(request, this);
                ++nowReconnectionTimes;
                LOGGER.warn("WebSocketClient 连接异常断开-第{}次尝试重新连接", nowReconnectionTimes);
            } else {
                LOGGER.warn("WebSocketClient 连接异常断开-无法重连");
            }
        }
    }

    public OkWebSocketClient openHandler(OpenHandler openHandler) {
        this.openHandler = openHandler;
        return this;
    }

    public OkWebSocketClient messageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        return this;
    }

    public OkWebSocketClient messageByteHandler(MessageByteHandler messageByteHandler) {
        this.messageByteHandler = messageByteHandler;
        return this;
    }

    public OkWebSocketClient closeHandler(CloseHandler closeHandler) {
        this.closeHandler = closeHandler;
        return this;
    }

    public OkWebSocketClient failureHandler(FailureHandler failureHandler) {
        this.failureHandler = failureHandler;
        return this;
    }

    public OkWebSocketClient sendMessageHandler(SendMessageHandler sendMessageHandler) {
        this.sendMessageHandler = sendMessageHandler;
        return this;
    }

    public OkWebSocketClient sendMessageByteHandler(SendMessageByteHandler sendMessageByteHandler) {
        this.sendMessageByteHandler = sendMessageByteHandler;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getErrorReconnectionTimes() {
        return errorReconnectionTimes;
    }

    public int getErrorReconnectionInterval() {
        return errorReconnectionInterval;
    }

    public int getCloseCode() {
        return closeCode;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public int getNowReconnectionTimes() {
        return nowReconnectionTimes;
    }

    public int getConnectionState() {
        return connectionState;
    }

}

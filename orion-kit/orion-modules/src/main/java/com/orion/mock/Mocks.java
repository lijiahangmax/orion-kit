package com.orion.mock;

import com.orion.mock.file.MockDownload;
import com.orion.mock.file.MockUpload;
import com.orion.mock.ws.MockWebSocketClient;
import com.orion.mock.ws.MockWebSocketServer;

import java.net.InetAddress;
import java.util.Map;

/**
 * Mocks
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/7 20:17
 */
public class Mocks {

    /**
     * get
     *
     * @param url url
     * @return response
     */
    public static MockResult get(String url) {
        return new MockRequest(url).await();
    }

    /**
     * get
     *
     * @param url    url
     * @param params params
     * @return response
     */
    public static MockResult get(String url, Map<String, String> params) {
        return new MockRequest(url).queryParams(params).await();
    }

    /**
     * post
     *
     * @param url url
     * @return response
     */
    static MockResult post(String url) {
        return new MockRequest(url).method(MockMethod.POST).await();
    }

    /**
     * post application/json
     *
     * @param url  url
     * @param body body
     * @return response
     */
    public static MockResult post(String url, byte[] body) {
        return new MockRequest(url).method(MockMethod.POST).contentType(MockContent.APPLICATION_JSON).body(body).await();
    }

    /**
     * post application/json
     *
     * @param url  url
     * @param body body
     * @return response
     */
    public static MockResult post(String url, String body) {
        return new MockRequest(url).method(MockMethod.POST).contentType(MockContent.APPLICATION_JSON).body(body).await();
    }

    /**
     * post
     *
     * @param url         url
     * @param contentType contentType
     * @param body        body
     * @return response
     */
    public static MockResult post(String url, String contentType, byte[] body) {
        return new MockRequest(url).method(MockMethod.POST).contentType(contentType).body(body).await();
    }

    /**
     * post
     *
     * @param url         url
     * @param contentType contentType
     * @param body        body
     * @return response
     */
    public static MockResult post(String url, String contentType, String body) {
        return new MockRequest(url).method(MockMethod.POST).contentType(contentType).body(body).await();
    }

    /**
     * 获取get请求
     *
     * @return get
     */
    public static MockRequest get() {
        return new MockRequest();
    }

    /**
     * 获取post请求
     *
     * @return post
     */
    public static MockRequest post() {
        return new MockRequest().method(MockMethod.POST);
    }

    /**
     * 下载文件
     *
     * @param url url
     * @return ignore
     */
    public static MockDownload download(String url) {
        return new MockDownload(url);
    }

    /**
     * 上传文件
     *
     * @param url url
     * @return ignore
     */
    public static MockUpload upload(String url) {
        return new MockUpload(url);
    }

    /**
     * 获取webSocket client
     *
     * @param url url
     * @return client
     */
    public static MockWebSocketClient getWebSocketClient(String url) {
        return new MockWebSocketClient(url);
    }

    /**
     * 获取webSocket server
     *
     * @param port port
     * @return server
     */
    public static MockWebSocketServer getWebSocketServer(int port) {
        return new MockWebSocketServer(port);
    }

    /**
     * 获取webSocket server
     *
     * @param address address
     * @param port    port
     * @return server
     */
    public static MockWebSocketServer getWebSocketServer(InetAddress address, int port) {
        return new MockWebSocketServer(address, port);
    }

}

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
package com.orion.http.ok;

import com.orion.http.ok.file.OkAsyncDownload;
import com.orion.http.ok.file.OkAsyncUpload;
import com.orion.http.ok.file.OkDownload;
import com.orion.http.ok.file.OkUpload;
import com.orion.http.ok.ws.OkWebSocketClient;
import com.orion.http.ok.ws.OkWebSocketServer;
import com.orion.http.support.HttpContentType;
import com.orion.http.support.HttpMethod;
import okhttp3.OkHttpClient;

import java.net.InetAddress;
import java.util.Map;

/**
 * OkHttp 调用工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/7 20:17
 */
public class OkRequests {

    /**
     * 默认请求 client
     */
    private static OkHttpClient client;

    private OkRequests() {
    }

    static {
        OkRequests.client = OkClientBuilder.create()
                .logInterceptor()
                .build();
    }

    /**
     * get
     *
     * @param url url
     * @return response
     */
    public static OkResponse get(String url) {
        return new OkRequest(url).await();
    }

    /**
     * get
     *
     * @param url    url
     * @param params params
     * @return response
     */
    public static OkResponse get(String url, Map<String, ?> params) {
        OkRequest request = new OkRequest(url);
        request.queryParams(params);
        return request.await();
    }

    /**
     * post
     *
     * @param url url
     * @return response
     */
    public static OkResponse post(String url) {
        OkRequest request = new OkRequest(url);
        request.method(HttpMethod.POST);
        return request.await();
    }

    /**
     * post application/json
     *
     * @param url  url
     * @param body body
     * @return response
     */
    public static OkResponse post(String url, byte[] body) {
        OkRequest request = new OkRequest(url);
        request.method(HttpMethod.POST)
                .contentType(HttpContentType.APPLICATION_JSON)
                .body(body);
        return request.await();
    }

    /**
     * post application/json
     *
     * @param url  url
     * @param body body
     * @return response
     */
    public static OkResponse post(String url, String body) {
        OkRequest request = new OkRequest(url);
        request.method(HttpMethod.POST)
                .contentType(HttpContentType.APPLICATION_JSON)
                .body(body);
        return request.await();
    }

    /**
     * post
     *
     * @param url         url
     * @param contentType contentType
     * @param body        body
     * @return response
     */
    public static OkResponse post(String url, String contentType, byte[] body) {
        OkRequest request = new OkRequest(url);
        request.method(HttpMethod.POST)
                .contentType(contentType)
                .body(body);
        return request.await();
    }

    /**
     * post
     *
     * @param url         url
     * @param contentType contentType
     * @param body        body
     * @return response
     */
    public static OkResponse post(String url, String contentType, String body) {
        OkRequest request = new OkRequest(url);
        request.method(HttpMethod.POST)
                .contentType(contentType)
                .body(body);
        return request.await();
    }

    /**
     * post x-www-form-urlencoded
     *
     * @param url       url
     * @param formParts formParts
     * @return response
     */
    public static OkResponse post(String url, Map<String, String> formParts) {
        OkRequest request = new OkRequest(url);
        request.method(HttpMethod.POST)
                .formParts(formParts);
        return request.await();
    }

    /**
     * 获取get请求
     *
     * @return get
     */
    public static OkRequest get() {
        return new OkRequest();
    }

    /**
     * 获取post请求
     *
     * @return post
     */
    public static OkRequest post() {
        OkRequest request = new OkRequest();
        request.method(HttpMethod.POST);
        return request;
    }

    /**
     * 异步下载文件
     *
     * @param url url
     * @return ignore
     */
    public static OkAsyncDownload downloadAsync(String url) {
        return new OkAsyncDownload(url);
    }

    /**
     * 异步下载文件
     *
     * @param url    url
     * @param client client
     * @return ignore
     */
    public static OkAsyncDownload downloadAsync(String url, OkHttpClient client) {
        return new OkAsyncDownload(url, client);
    }

    /**
     * 同步下载文件
     *
     * @param url url
     * @return ignore
     */
    public static OkDownload download(String url) {
        return new OkDownload(url);
    }

    /**
     * 同步下载文件
     *
     * @param url    url
     * @param client client
     * @return ignore
     */
    public static OkDownload download(String url, OkHttpClient client) {
        return new OkDownload(url, client);
    }

    /**
     * 异步上传文件
     *
     * @param url url
     * @return ignore
     */
    public static OkAsyncUpload uploadAsync(String url) {
        return new OkAsyncUpload(url);
    }

    /**
     * 异步上传文件
     *
     * @param url    url
     * @param client client
     * @return ignore
     */
    public static OkAsyncUpload uploadAsync(String url, OkHttpClient client) {
        return new OkAsyncUpload(url, client);
    }

    /**
     * 同步上传文件
     *
     * @param url url
     * @return ignore
     */
    public static OkUpload upload(String url) {
        return new OkUpload(url);
    }

    /**
     * 同步上传文件
     *
     * @param url    url
     * @param client client
     * @return ignore
     */
    public static OkUpload upload(String url, OkHttpClient client) {
        return new OkUpload(url, client);
    }

    /**
     * 获取webSocket client
     *
     * @param url url
     * @return client
     */
    public static OkWebSocketClient getWebSocketClient(String url) {
        return new OkWebSocketClient(url);
    }

    /**
     * 获取webSocket server
     *
     * @param port port
     * @return server
     */
    public static OkWebSocketServer getWebSocketServer(int port) {
        return new OkWebSocketServer(port);
    }

    /**
     * 获取webSocket server
     *
     * @param address address
     * @param port    port
     * @return server
     */
    public static OkWebSocketServer getWebSocketServer(InetAddress address, int port) {
        return new OkWebSocketServer(address, port);
    }

    public static OkHttpClient getClient() {
        return client;
    }

    public static void setClient(OkHttpClient client) {
        OkRequests.client = client;
    }

}

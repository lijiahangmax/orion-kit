/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.http.apache;

import cn.orionsec.kit.http.apache.file.ApacheDownload;
import cn.orionsec.kit.http.apache.file.ApacheUpload;
import cn.orionsec.kit.http.support.HttpContentType;
import cn.orionsec.kit.http.support.HttpMethod;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Map;

/**
 * Apache 调用工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/12 15:08
 */
public class ApacheRequests {

    /**
     * 默认请求 client
     */
    private static CloseableHttpClient client;

    private ApacheRequests() {
    }

    static {
        ApacheRequests.client = ApacheClientBuilder.create()
                .logInterceptor()
                .build();
    }

    /**
     * get
     *
     * @param url url
     * @return response
     */
    public static ApacheResponse get(String url) {
        return new ApacheRequest(url).await();
    }

    /**
     * get
     *
     * @param url    url
     * @param params params
     * @return response
     */
    public static ApacheResponse get(String url, Map<String, ?> params) {
        ApacheRequest request = new ApacheRequest(url);
        request.queryParams(params);
        return request.await();
    }

    /**
     * post
     *
     * @param url url
     * @return response
     */
    public static ApacheResponse post(String url) {
        ApacheRequest request = new ApacheRequest(url);
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
    public static ApacheResponse post(String url, byte[] body) {
        ApacheRequest request = new ApacheRequest(url);
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
    public static ApacheResponse post(String url, String body) {
        ApacheRequest request = new ApacheRequest(url);
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
    public static ApacheResponse post(String url, String contentType, byte[] body) {
        ApacheRequest request = new ApacheRequest(url);
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
    public static ApacheResponse post(String url, String contentType, String body) {
        ApacheRequest request = new ApacheRequest(url);
        request.method(HttpMethod.POST)
                .contentType(contentType)
                .body(body);
        return request.await();
    }

    /**
     * 获取get请求
     *
     * @return get
     */
    public static ApacheRequest get() {
        return new ApacheRequest();
    }

    /**
     * 获取post请求
     *
     * @return post
     */
    public static ApacheRequest post() {
        ApacheRequest request = new ApacheRequest();
        request.method(HttpMethod.POST);
        return request;
    }

    /**
     * 同步下载文件
     *
     * @param url url
     * @return ignore
     */
    public static ApacheDownload download(String url) {
        return new ApacheDownload(url);
    }

    /**
     * 同步下载文件
     *
     * @param url    url
     * @param client client
     * @return ignore
     */
    public static ApacheDownload download(String url, CloseableHttpClient client) {
        return new ApacheDownload(url, client);
    }

    /**
     * 上传文件
     *
     * @param url url
     * @return ignore
     */
    public static ApacheUpload upload(String url) {
        return new ApacheUpload(url);
    }

    /**
     * 上传文件
     *
     * @param url    url
     * @param client client
     * @return ignore
     */
    public static ApacheUpload upload(String url, CloseableHttpClient client) {
        return new ApacheUpload(url, client);
    }

    public static CloseableHttpClient getClient() {
        return client;
    }

    public static void setClient(CloseableHttpClient client) {
        ApacheRequests.client = client;
    }

}

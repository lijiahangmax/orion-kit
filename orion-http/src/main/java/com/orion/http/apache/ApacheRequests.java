package com.orion.http.apache;

import com.orion.http.apache.file.ApacheDownload;
import com.orion.http.apache.file.ApacheUpload;
import com.orion.http.support.HttpContentType;
import com.orion.http.support.HttpMethod;
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

    private ApacheRequests() {
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
    public static ApacheResponse get(String url, Map<String, String> params) {
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

}

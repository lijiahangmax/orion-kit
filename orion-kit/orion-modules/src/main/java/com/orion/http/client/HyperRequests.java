package com.orion.http.client;

import com.orion.http.client.file.HyperDownload;
import com.orion.http.client.file.HyperUpload;
import com.orion.http.common.HttpContent;
import com.orion.http.common.HttpMethod;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Map;

/**
 * Hyper 调用工具
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/12 15:08
 */
public class HyperRequests {

    /**
     * get
     *
     * @param url url
     * @return response
     */
    public static HyperResponse get(String url) {
        return new HyperRequest(url).await();
    }

    /**
     * get
     *
     * @param url    url
     * @param params params
     * @return response
     */
    public static HyperResponse get(String url, Map<String, String> params) {
        return new HyperRequest(url).queryParams(params).await();
    }

    /**
     * post
     *
     * @param url url
     * @return response
     */
    public static HyperResponse post(String url) {
        return new HyperRequest(url).method(HttpMethod.POST).await();
    }

    /**
     * post application/json
     *
     * @param url  url
     * @param body body
     * @return response
     */
    public static HyperResponse post(String url, byte[] body) {
        return new HyperRequest(url).method(HttpMethod.POST).contentType(HttpContent.APPLICATION_JSON).body(body).await();
    }

    /**
     * post application/json
     *
     * @param url  url
     * @param body body
     * @return response
     */
    public static HyperResponse post(String url, String body) {
        return new HyperRequest(url).method(HttpMethod.POST).contentType(HttpContent.APPLICATION_JSON).body(body).await();
    }

    /**
     * post
     *
     * @param url         url
     * @param contentType contentType
     * @param body        body
     * @return response
     */
    public static HyperResponse post(String url, String contentType, byte[] body) {
        return new HyperRequest(url).method(HttpMethod.POST).contentType(contentType).body(body).await();
    }

    /**
     * post
     *
     * @param url         url
     * @param contentType contentType
     * @param body        body
     * @return response
     */
    public static HyperResponse post(String url, String contentType, String body) {
        return new HyperRequest(url).method(HttpMethod.POST).contentType(contentType).body(body).await();
    }

    /**
     * 获取get请求
     *
     * @return get
     */
    public static HyperRequest get() {
        return new HyperRequest();
    }

    /**
     * 获取post请求
     *
     * @return post
     */
    public static HyperRequest post() {
        return new HyperRequest().method(HttpMethod.POST);
    }

    /**
     * 同步下载文件
     *
     * @param url url
     * @return ignore
     */
    public static HyperDownload download(String url) {
        return new HyperDownload(url);
    }

    /**
     * 同步下载文件
     *
     * @param url    url
     * @param client client
     * @return ignore
     */
    public static HyperDownload download(String url, CloseableHttpClient client) {
        return new HyperDownload(url, client);
    }

    /**
     * 上传文件
     *
     * @param url url
     * @return ignore
     */
    public static HyperUpload upload(String url) {
        return new HyperUpload(url);
    }

    /**
     * 上传文件
     *
     * @param url    url
     * @param client client
     * @return ignore
     */
    public static HyperUpload upload(String url, CloseableHttpClient client) {
        return new HyperUpload(url, client);
    }

}

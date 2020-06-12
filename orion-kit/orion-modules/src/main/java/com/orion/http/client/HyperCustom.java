package com.orion.http.client;

import com.orion.http.common.HttpContent;
import com.orion.http.common.HttpMethod;

import java.util.Map;

/**
 * Hyper 调用工具
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/6/12 15:08
 */
public class HyperCustom {

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
    static HyperResponse post(String url) {
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

}

package com.orion.http.parse;

import com.orion.http.support.HttpContentType;
import com.orion.http.support.HttpMethod;

import java.util.Map;

/**
 * ParseRequest 调用工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/8 15:59
 */
public class ParseRequests {

    private ParseRequests() {
    }

    /**
     * get
     *
     * @param url url
     * @return response
     */
    public static ParseResponse get(String url) {
        return new ParseRequest(url).await();
    }

    /**
     * get
     *
     * @param url    url
     * @param params params
     * @return response
     */
    public static ParseResponse get(String url, Map<String, ?> params) {
        ParseRequest request = new ParseRequest(url);
        request.queryParams(params);
        return request.await();
    }

    /**
     * post
     *
     * @param url url
     * @return response
     */
    public static ParseResponse post(String url) {
        ParseRequest request = new ParseRequest(url);
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
    public static ParseResponse post(String url, byte[] body) {
        ParseRequest request = new ParseRequest(url);
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
    public static ParseResponse post(String url, String body) {
        ParseRequest request = new ParseRequest(url);
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
    public static ParseResponse post(String url, String contentType, byte[] body) {
        ParseRequest request = new ParseRequest(url);
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
    public static ParseResponse post(String url, String contentType, String body) {
        ParseRequest request = new ParseRequest(url);
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
    public static ParseResponse post(String url, Map<String, String> formParts) {
        ParseRequest request = new ParseRequest(url);
        request.method(HttpMethod.POST)
                .formParts(formParts);
        return request.await();
    }

    /**
     * 获取get请求
     *
     * @return get
     */
    public static ParseRequest get() {
        return new ParseRequest();
    }

    /**
     * 获取post请求
     *
     * @return post
     */
    public static ParseRequest post() {
        ParseRequest request = new ParseRequest();
        request.method(HttpMethod.POST);
        return request;
    }

}

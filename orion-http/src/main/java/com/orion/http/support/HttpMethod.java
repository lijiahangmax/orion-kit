package com.orion.http.support;

import com.orion.utils.Exceptions;

/**
 * HTTP Method
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/8 10:22
 */
public enum HttpMethod {

    /**
     * get
     */
    GET("GET"),

    /**
     * post
     */
    POST("POST"),

    /**
     * put
     */
    PUT("PUT"),

    /**
     * delete
     */
    DELETE("DELETE"),

    /**
     * patch
     */
    PATCH("PATCH"),

    /**
     * head
     */
    HEAD("HEAD"),

    /**
     * options ok不支持
     */
    OPTIONS("OPTIONS"),

    /**
     * trace ok不支持
     */
    TRACE("TRACE");

    private String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String method() {
        return method;
    }

    /**
     * 方法是否支持
     *
     * @param method method
     */
    public static void valid(String method) {
        valid(method, values().length);
    }

    /**
     * 方法是否支持
     *
     * @param method method
     * @param length 判断的数量
     */
    public static void valid(String method, int length) {
        boolean opt = false;
        method = method.trim().toUpperCase();
        for (int i = 0; i < length; i++) {
            HttpMethod m = values()[i];
            if (m.method().equals(method)) {
                opt = true;
                break;
            }
        }
        if (!opt) {
            throw Exceptions.httpUnsupportedMethod("the method is not supported");
        }
    }

}

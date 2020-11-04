package com.orion.http.common;

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

    String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    /**
     * 方法是否支持
     *
     * @param method method
     * @param skip   是否跳过 options trace
     */
    public static void validMethod(String method, boolean skip) {
        boolean opt = false;
        int length = values().length;
        if (skip) {
            length -= 2;
        }
        method = method.trim().toUpperCase();
        for (int i = 0; i < length; i++) {
            HttpMethod m = values()[i];
            if (m.getMethod().equals(method)) {
                opt = true;
                break;
            }
        }
        if (!opt) {
            throw Exceptions.unSupport("the method is not supported");
        }
    }

}

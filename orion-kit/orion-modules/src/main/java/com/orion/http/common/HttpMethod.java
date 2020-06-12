package com.orion.http.common;

import com.orion.utils.Exceptions;

/**
 * HTTP Method
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/8 10:22
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
     * @param method    method
     * @param skipTrace 是否跳过trace
     */
    public static void validHethod(String method, boolean skipTrace) {
        boolean opt = false;
        int length = values().length;
        if (skipTrace) {
            length--;
        }
        method = method.trim().toUpperCase();
        for (int i = 0; i < length; i++) {
            HttpMethod m = values()[i];
            if (m.getMethod().equals(method)) {
                opt = true;
            }
        }
        if (!opt) {
            throw Exceptions.unsupport("the method is not supported");
        }
    }

}

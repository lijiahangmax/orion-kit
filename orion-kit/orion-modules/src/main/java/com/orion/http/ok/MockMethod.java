package com.orion.http.ok;

/**
 * Mock Method
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/8 10:22
 */
public enum MockMethod {

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
    HEAD("HEAD");

    String method;

    MockMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

}

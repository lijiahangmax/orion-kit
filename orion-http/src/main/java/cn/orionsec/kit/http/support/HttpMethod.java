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
package cn.orionsec.kit.http.support;

import cn.orionsec.kit.lang.utils.Exceptions;

/**
 * HTTP Method
 *
 * @author Jiahang Li
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

    private final String method;

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

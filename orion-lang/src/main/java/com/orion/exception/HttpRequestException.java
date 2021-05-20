package com.orion.exception;

/**
 * HTTP 请求异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/6 21:21
 */
public class HttpRequestException extends RuntimeException {

    public HttpRequestException() {
    }

    public HttpRequestException(String message) {
        super(message);
    }

    public HttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRequestException(Throwable cause) {
        super(cause);
    }

}

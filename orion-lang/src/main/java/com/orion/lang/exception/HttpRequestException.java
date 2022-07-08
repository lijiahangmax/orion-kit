package com.orion.lang.exception;

/**
 * HTTP 请求异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/6 21:21
 */
public class HttpRequestException extends RuntimeException {

    private final String url;

    public HttpRequestException(String url) {
        this.url = url;
    }

    public HttpRequestException(String url, String message) {
        super(message);
        this.url = url;
    }

    public HttpRequestException(String url, String message, Throwable cause) {
        super(message, cause);
        this.url = url;
    }

    public HttpRequestException(String url, Throwable cause) {
        super(cause);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

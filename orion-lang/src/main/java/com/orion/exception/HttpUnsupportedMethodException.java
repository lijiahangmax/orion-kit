package com.orion.exception;

/**
 * 不支持的HTTP方法异常
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/6 21:07
 */
public class HttpUnsupportedMethodException extends RuntimeException {

    public HttpUnsupportedMethodException() {
    }

    public HttpUnsupportedMethodException(String message) {
        super(message);
    }

    public HttpUnsupportedMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpUnsupportedMethodException(Throwable cause) {
        super(cause);
    }

}

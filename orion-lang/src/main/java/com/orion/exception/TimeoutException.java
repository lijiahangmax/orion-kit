package com.orion.exception;

/**
 * 超时异常
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/12 18:10
 */
public class TimeoutException extends RuntimeException {

    public TimeoutException() {
    }

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeoutException(Throwable cause) {
        super(cause);
    }

}

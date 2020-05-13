package com.orion.exception;

/**
 * 超时异常
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/12 18:10
 */
public class TimeoutRuntimeException extends RuntimeException {

    public TimeoutRuntimeException() {
    }

    public TimeoutRuntimeException(String message) {
        super(message);
    }

    public TimeoutRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeoutRuntimeException(Throwable cause) {
        super(cause);
    }

}

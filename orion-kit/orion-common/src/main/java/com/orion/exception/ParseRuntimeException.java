package com.orion.exception;

/**
 * 解析异常
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/23 14:38
 */
public class ParseRuntimeException extends RuntimeException {

    public ParseRuntimeException() {
    }

    public ParseRuntimeException(String message) {
        super(message);
    }

    public ParseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseRuntimeException(Throwable cause) {
        super(cause);
    }

}

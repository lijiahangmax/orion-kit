package com.orion.exception;

/**
 * 连接异常
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/17 16:19
 */
public class ConnectionRuntimeException extends RuntimeException {

    public ConnectionRuntimeException() {
    }

    public ConnectionRuntimeException(String message) {
        super(message);
    }

    public ConnectionRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionRuntimeException(Throwable cause) {
        super(cause);
    }

}

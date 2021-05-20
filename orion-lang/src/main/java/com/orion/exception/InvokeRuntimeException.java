package com.orion.exception;

/**
 * 反射调用异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/1/7 10:35
 */
public class InvokeRuntimeException extends RuntimeException {

    public InvokeRuntimeException() {
    }

    public InvokeRuntimeException(String message) {
        super(message);
    }

    public InvokeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvokeRuntimeException(Throwable cause) {
        super(cause);
    }

}

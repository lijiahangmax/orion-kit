package com.orion.lang.exception;

/**
 * 中断运行时异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/9 17:34
 */
public class InterruptedRuntimeException extends RuntimeException {

    public InterruptedRuntimeException() {
    }

    public InterruptedRuntimeException(String message) {
        super(message);
    }

    public InterruptedRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterruptedRuntimeException(Throwable cause) {
        super(cause);
    }

}

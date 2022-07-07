package com.orion.lang.exception;

/**
 * IO运行时异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/12 1:00
 */
public class IORuntimeException extends RuntimeException {

    public IORuntimeException() {
    }

    public IORuntimeException(String message) {
        super(message);
    }

    public IORuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IORuntimeException(Throwable cause) {
        super(cause);
    }

}

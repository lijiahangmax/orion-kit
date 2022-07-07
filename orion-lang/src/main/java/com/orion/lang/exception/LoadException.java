package com.orion.lang.exception;

/**
 * 加载异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/25 15:27
 */
public class LoadException extends RuntimeException {

    public LoadException() {
    }

    public LoadException(String message) {
        super(message);
    }

    public LoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadException(Throwable cause) {
        super(cause);
    }

}

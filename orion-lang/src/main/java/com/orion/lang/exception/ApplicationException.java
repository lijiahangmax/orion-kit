package com.orion.lang.exception;

/**
 * 应用异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/10/28 21:09
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException() {
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

}

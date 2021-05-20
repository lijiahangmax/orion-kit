package com.orion.exception;

/**
 * 验签失败异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/2 18:21
 */
public class SignInvalidException extends RuntimeException {

    public SignInvalidException() {
    }

    public SignInvalidException(String message) {
        super(message);
    }

    public SignInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignInvalidException(Throwable cause) {
        super(cause);
    }

}

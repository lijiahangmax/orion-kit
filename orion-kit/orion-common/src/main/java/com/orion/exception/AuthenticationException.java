package com.orion.exception;

/**
 * 认证异常
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/15 21:27
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
    }

    public AuthenticationException(String info) {
        super(info);
    }

    public AuthenticationException(Throwable res) {
        super(res);
    }

    public AuthenticationException(String info, Throwable res) {
        super(info, res);
    }

}

package com.orion.lang.exception;

/**
 * 数据加密异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/29 22:22
 */
public class EncryptException extends RuntimeException {

    public EncryptException() {
    }

    public EncryptException(String message) {
        super(message);
    }

    public EncryptException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncryptException(Throwable cause) {
        super(cause);
    }

}

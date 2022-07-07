package com.orion.lang.exception;

/**
 * 数据解密异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/29 22:22
 */
public class DecryptException extends RuntimeException {

    public DecryptException() {
    }

    public DecryptException(String message) {
        super(message);
    }

    public DecryptException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecryptException(Throwable cause) {
        super(cause);
    }

}

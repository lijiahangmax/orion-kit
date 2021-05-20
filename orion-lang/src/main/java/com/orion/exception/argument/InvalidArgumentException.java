package com.orion.exception.argument;

/**
 * 对象验证不合法异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see com.orion.utils.Valid
 * @since 2020/10/18 21:37
 */
public class InvalidArgumentException extends RuntimeException {

    public InvalidArgumentException() {
    }

    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidArgumentException(Throwable cause) {
        super(cause);
    }

}

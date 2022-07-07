package com.orion.lang.exception;

/**
 * 类型转化异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/23 10:56
 */
public class ConvertException extends RuntimeException {

    public ConvertException() {
    }

    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConvertException(Throwable cause) {
        super(cause);
    }

}

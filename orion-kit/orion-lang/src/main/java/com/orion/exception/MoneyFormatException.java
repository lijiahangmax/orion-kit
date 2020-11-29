package com.orion.exception;

/**
 * 金额转化异常
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/12/9 18:10
 */
public class MoneyFormatException extends RuntimeException {

    public MoneyFormatException() {
    }

    public MoneyFormatException(String message) {
        super(message);
    }

    public MoneyFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public MoneyFormatException(Throwable cause) {
        super(cause);
    }
}

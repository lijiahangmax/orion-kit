package com.orion.exception;

/**
 * 时间转化运行时异常
 *
 * @author:Li
 * @time: 2019/12/9 18:08
 * @version: 1.0.0
 */
public class ParseDateException extends RuntimeException {

    public ParseDateException() {
    }

    public ParseDateException(String message) {
        super(message);
    }

    public ParseDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseDateException(Throwable cause) {
        super(cause);
    }
}

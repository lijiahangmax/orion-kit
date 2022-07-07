package com.orion.lang.exception;

/**
 * 执行失败异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/16 17:38
 */
public class ExecuteException extends Exception {

    public ExecuteException() {
    }

    public ExecuteException(String message) {
        super(message);
    }

    public ExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecuteException(Throwable cause) {
        super(cause);
    }

}

package com.orion.exception.argument;

/**
 * 对象验证索引异常
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/18 21:45
 */
public class IndexArgumentException extends InvalidArgumentException {

    public IndexArgumentException() {
    }

    public IndexArgumentException(String message) {
        super(message);
    }

    public IndexArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexArgumentException(Throwable cause) {
        super(cause);
    }

}

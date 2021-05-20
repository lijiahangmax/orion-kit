package com.orion.exception.argument;

/**
 * 验证对象为空异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/18 21:37
 */
public class NullArgumentException extends InvalidArgumentException {

    public NullArgumentException() {
    }

    public NullArgumentException(String message) {
        super(message);
    }

    public NullArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullArgumentException(Throwable cause) {
        super(cause);
    }

}

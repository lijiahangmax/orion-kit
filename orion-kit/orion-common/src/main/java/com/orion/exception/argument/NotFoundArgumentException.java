package com.orion.exception.argument;

/**
 * 未找到异常
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/3 2:07
 */
public class NotFoundArgumentException extends InvalidArgumentException {

    public NotFoundArgumentException() {
    }

    public NotFoundArgumentException(String info) {
        super(info);
    }

    public NotFoundArgumentException(Throwable res) {
        super(res);
    }

    public NotFoundArgumentException(String info, Throwable res) {
        super(info, res);
    }

}

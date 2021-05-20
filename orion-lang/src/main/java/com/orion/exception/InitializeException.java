package com.orion.exception;

/**
 * 初始化异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/3 2:05
 */
public class InitializeException extends RuntimeException {

    public InitializeException() {
    }

    public InitializeException(String info) {
        super(info);
    }

    public InitializeException(Throwable res) {
        super(res);
    }

    public InitializeException(String info, Throwable res) {
        super(info, res);
    }

}

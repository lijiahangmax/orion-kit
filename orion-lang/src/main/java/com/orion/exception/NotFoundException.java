package com.orion.exception;

/**
 * 未找到异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/3 2:07
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
    }

    public NotFoundException(String info) {
        super(info);
    }

    public NotFoundException(Throwable res) {
        super(res);
    }

    public NotFoundException(String info, Throwable res) {
        super(info, res);
    }

}

package com.orion.lang.exception;

/**
 * 锁定异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/3 2:26
 */
public class LockException extends RuntimeException {

    public LockException() {
    }

    public LockException(String info) {
        super(info);
    }

    public LockException(Throwable res) {
        super(res);
    }

    public LockException(String info, Throwable res) {
        super(info, res);
    }

}

package com.orion.exception;

/**
 * FTP 异常
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/17 18:46
 */
public class FTPException extends RuntimeException {

    public FTPException() {
    }

    public FTPException(String message) {
        super(message);
    }

    public FTPException(String message, Throwable cause) {
        super(message, cause);
    }

    public FTPException(Throwable cause) {
        super(cause);
    }

}

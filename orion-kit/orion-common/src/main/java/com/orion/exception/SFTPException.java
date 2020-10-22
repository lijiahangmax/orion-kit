package com.orion.exception;

/**
 * SFTP 异常
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/8 22:18
 */
public class SFTPException extends RuntimeException {

    public SFTPException() {
    }

    public SFTPException(String message) {
        super(message);
    }

    public SFTPException(String message, Throwable cause) {
        super(message, cause);
    }

    public SFTPException(Throwable cause) {
        super(cause);
    }

}

package com.orion.exception;

/**
 * SFTP 异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/8 22:18
 */
public class SftpException extends RuntimeException {

    public SftpException() {
    }

    public SftpException(String message) {
        super(message);
    }

    public SftpException(String message, Throwable cause) {
        super(message, cause);
    }

    public SftpException(Throwable cause) {
        super(cause);
    }

}

package com.orion.lang.exception;

/**
 * 版本控制异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/1 23:15
 */
public class VcsException extends RuntimeException {

    private static final long serialVersionUID = 8247610319171014183L;

    public VcsException() {
    }

    public VcsException(String message) {
        super(message);
    }

    public VcsException(String message, Throwable cause) {
        super(message, cause);
    }

    public VcsException(Throwable cause) {
        super(cause);
    }


}

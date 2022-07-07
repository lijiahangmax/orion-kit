package com.orion.lang.exception.argument;

/**
 * 带code的exception
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/11 18:04
 */
public class CodeArgumentException extends InvalidArgumentException {

    private int code;

    public CodeArgumentException() {
    }

    public CodeArgumentException(String message) {
        super(message);
    }

    public CodeArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeArgumentException(Throwable cause) {
        super(cause);
    }

    public CodeArgumentException(int code) {
        this.code = code;
    }

    public CodeArgumentException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CodeArgumentException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public CodeArgumentException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}

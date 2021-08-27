package com.orion.exception;

/**
 * 解析异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/23 14:38
 */
public class ParseRuntimeException extends RuntimeException {

    private int errorOffset;

    public ParseRuntimeException() {
    }

    public ParseRuntimeException(int errorOffset) {
        this.errorOffset = errorOffset;
    }

    public ParseRuntimeException(String message) {
        super(message);
    }

    public ParseRuntimeException(String message, int errorOffset) {
        super(message);
        this.errorOffset = errorOffset;
    }

    public ParseRuntimeException(Throwable t) {
        super(t);
    }

    public ParseRuntimeException(int errorOffset, Throwable t) {
        super(t);
        this.errorOffset = errorOffset;
    }

    public ParseRuntimeException(String message, Throwable t) {
        super(message, t);
    }

    public ParseRuntimeException(String message, int errorOffset, Throwable t) {
        super(message, t);
        this.errorOffset = errorOffset;
    }

    public int getErrorOffset() {
        return errorOffset;
    }

}

package com.orion.exception;

/**
 * 运行异常 (日志打印)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/7/17 11:36
 */
public class LogException extends RuntimeException {

    private static final long serialVersionUID = 8247610319171014183L;

    public LogException() {
    }

    public LogException(String message) {
        super(message);
    }

    public LogException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogException(Throwable cause) {
        super(cause);
    }

    public boolean hasCause() {
        return this.getCause() != null;
    }

}

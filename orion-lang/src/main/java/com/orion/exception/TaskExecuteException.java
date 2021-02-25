package com.orion.exception;

/**
 * 任务执行异常
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/2/25 17:15
 */
public class TaskExecuteException extends RuntimeException {

    public TaskExecuteException() {
    }

    public TaskExecuteException(String message) {
        super(message);
    }

    public TaskExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskExecuteException(Throwable cause) {
        super(cause);
    }

}

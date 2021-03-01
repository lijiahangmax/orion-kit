package com.orion.exception;

import javax.script.ScriptException;

/**
 * 脚本执行异常
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/1 23:15
 */
public class ScriptExecuteException extends RuntimeException {

    private static final long serialVersionUID = 8247610319171014183L;

    private String fileName;

    private int lineNumber = -1;

    private int columnNumber = -1;

    public ScriptExecuteException() {
    }

    public ScriptExecuteException(String message) {
        super(message);
    }

    public ScriptExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptExecuteException(Throwable cause) {
        super(cause);
    }

    public ScriptExecuteException(ScriptException e) {
        super(e);
        this.fileName = e.getFileName();
        this.lineNumber = e.getLineNumber();
        this.columnNumber = e.getColumnNumber();
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        if (fileName != null) {
            msg += (" in " + fileName);
            if (lineNumber != -1) {
                msg += " at line number " + lineNumber;
            }
            if (columnNumber != -1) {
                msg += " at column number " + columnNumber;
            }
        }
        return msg;
    }

}

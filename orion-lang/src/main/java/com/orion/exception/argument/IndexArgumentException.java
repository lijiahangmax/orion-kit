package com.orion.exception.argument;

/**
 * 对象验证索引异常
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/18 21:45
 */
public class IndexArgumentException extends InvalidArgumentException {

    private int index;

    private int size;

    public IndexArgumentException() {
    }

    public IndexArgumentException(String message) {
        super(message);
    }

    public IndexArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexArgumentException(Throwable cause) {
        super(cause);
    }

    public IndexArgumentException(int index) {
        this.index = index;
    }

    public IndexArgumentException(int index, String message) {
        super(message);
        this.index = index;
    }

    public IndexArgumentException(int index, String message, Throwable cause) {
        super(message, cause);
        this.index = index;
    }

    public IndexArgumentException(int index, Throwable cause) {
        super(cause);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}

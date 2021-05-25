package com.orion.exception.argument;

import com.orion.lang.wrapper.HttpWrapper;

/**
 * HttpWrapper的exception
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see com.orion.lang.wrapper.HttpWrapper
 * @since 2021/5/25 16:05
 */
public class HttpWrapperException extends InvalidArgumentException {

    private HttpWrapper<?> wrapper;

    public HttpWrapperException(HttpWrapper<?> wrapper) {
        this.wrapper = wrapper;
    }

    public HttpWrapperException(HttpWrapper<?> wrapper, String message) {
        super(message);
        this.wrapper = wrapper;
    }

    public HttpWrapperException(HttpWrapper<?> wrapper, String message, Throwable cause) {
        super(message, cause);
        this.wrapper = wrapper;
    }

    public HttpWrapperException(HttpWrapper<?> wrapper, Throwable cause) {
        super(cause);
        this.wrapper = wrapper;
    }

    public HttpWrapper<?> getWrapper() {
        return wrapper;
    }

    public void setWrapper(HttpWrapper<?> wrapper) {
        this.wrapper = wrapper;
    }

}

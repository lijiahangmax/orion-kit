package com.orion.lang.exception.argument;

import com.orion.lang.define.wrapper.HttpWrapper;

/**
 * HttpWrapper 的 exception
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see com.orion.lang.define.wrapper.HttpWrapper
 * @since 2021/5/25 16:05
 */
public class HttpWrapperException extends WrapperException {

    public HttpWrapperException(HttpWrapper<?> wrapper) {
        super(wrapper);
    }

    public HttpWrapperException(HttpWrapper<?> wrapper, String message) {
        super(wrapper, message);
    }

    public HttpWrapperException(HttpWrapper<?> wrapper, String message, Throwable cause) {
        super(wrapper, message, cause);
    }

    public HttpWrapperException(HttpWrapper<?> wrapper, Throwable cause) {
        super(wrapper, cause);
    }

    @Override
    @SuppressWarnings("unchecked")
    public HttpWrapper<?> getWrapper() {
        return super.getWrapper();
    }

}

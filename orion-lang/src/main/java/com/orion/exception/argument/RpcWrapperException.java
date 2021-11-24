package com.orion.exception.argument;

import com.orion.lang.wrapper.RpcWrapper;

/**
 * RpcWrapper 的 exception
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see com.orion.lang.wrapper.RpcWrapper
 * @since 2021/5/25 16:06
 */
public class RpcWrapperException extends WrapperException {

    public RpcWrapperException(RpcWrapper<?> wrapper) {
        super(wrapper);
    }

    public RpcWrapperException(RpcWrapper<?> wrapper, String message) {
        super(wrapper, message);
    }

    public RpcWrapperException(RpcWrapper<?> wrapper, String message, Throwable cause) {
        super(wrapper, message, cause);
    }

    public RpcWrapperException(RpcWrapper<?> wrapper, Throwable cause) {
        super(wrapper, cause);
    }

    @Override
    @SuppressWarnings("unchecked")
    public RpcWrapper<?> getWrapper() {
        return super.getWrapper();
    }

}

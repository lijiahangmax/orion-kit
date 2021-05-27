package com.orion.exception.argument;

import com.orion.lang.wrapper.RpcWrapper;
import com.orion.utils.Valid;

/**
 * RpcWrapper的exception
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see com.orion.lang.wrapper.RpcWrapper
 * @since 2021/5/25 16:06
 */
public class RpcWrapperException extends InvalidArgumentException {

    private RpcWrapper<?> wrapper;

    public RpcWrapperException(RpcWrapper<?> wrapper) {
        Valid.notNull(wrapper, "rpc wrapper is null");
        this.wrapper = wrapper;
    }

    public RpcWrapperException(RpcWrapper<?> wrapper, String message) {
        super(message);
        Valid.notNull(wrapper, "rpc wrapper is null");
        this.wrapper = wrapper;
    }

    public RpcWrapperException(RpcWrapper<?> wrapper, String message, Throwable cause) {
        super(message, cause);
        Valid.notNull(wrapper, "rpc wrapper is null");
        this.wrapper = wrapper;
    }

    public RpcWrapperException(RpcWrapper<?> wrapper, Throwable cause) {
        super(cause);
        Valid.notNull(wrapper, "rpc wrapper is null");
        this.wrapper = wrapper;
    }

    public RpcWrapper<?> getWrapper() {
        return wrapper;
    }

    public void setWrapper(RpcWrapper<?> wrapper) {
        this.wrapper = wrapper;
    }

}

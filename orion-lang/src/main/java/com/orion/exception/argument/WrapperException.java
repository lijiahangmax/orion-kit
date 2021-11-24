package com.orion.exception.argument;

import com.orion.lang.wrapper.Wrapper;
import com.orion.utils.Valid;

/**
 * Wrapper 的 exception
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see com.orion.lang.wrapper.Wrapper
 * @since 2021/11/20 0:20
 */
public class WrapperException extends InvalidArgumentException {

    private Wrapper<?> wrapper;

    public WrapperException(Wrapper<?> wrapper) {
        Valid.notNull(wrapper, "wrapper is null");
        this.wrapper = wrapper;
    }

    public WrapperException(Wrapper<?> wrapper, String message) {
        super(message);
        Valid.notNull(wrapper, "wrapper is null");
        this.wrapper = wrapper;
    }

    public WrapperException(Wrapper<?> wrapper, String message, Throwable cause) {
        super(message, cause);
        Valid.notNull(wrapper, "wrapper is null");
        this.wrapper = wrapper;
    }

    public WrapperException(Wrapper<?> wrapper, Throwable cause) {
        super(cause);
        Valid.notNull(wrapper, "wrapper is null");
        this.wrapper = wrapper;
    }

    @SuppressWarnings("unchecked")
    public <T extends Wrapper<?>> T getWrapper() {
        return (T) wrapper;
    }

}

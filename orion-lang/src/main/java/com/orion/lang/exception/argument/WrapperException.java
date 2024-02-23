package com.orion.lang.exception.argument;

import com.orion.lang.define.wrapper.Wrapper;
import com.orion.lang.utils.Valid;

/**
 * Wrapper 的 exception
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see com.orion.lang.define.wrapper.Wrapper
 * @since 2021/11/20 0:20
 */
public class WrapperException extends InvalidArgumentException {

    private final Wrapper<?> wrapper;

    public WrapperException(Wrapper<?> wrapper) {
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

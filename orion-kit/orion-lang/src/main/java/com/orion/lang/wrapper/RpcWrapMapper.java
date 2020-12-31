package com.orion.lang.wrapper;

import java.util.function.Supplier;

/**
 * rpc返回值
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/8/8 19:31
 */
public enum RpcWrapMapper implements Supplier<RpcWrapper<?>> {

    /**
     * 成功
     */
    SUCCESS(Wrapper.RPC_SUCCESS_CODE, Wrapper.RPC_SUCCESS_MESSAGE),

    /**
     * 失败
     */
    ERROR(Wrapper.RPC_ERROR_CODE, Wrapper.RPC_ERROR_MESSAGE),

    ;

    private int code;

    private String msg;

    RpcWrapMapper(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public RpcWrapper<?> get() {
        return new RpcWrapper<>(code, msg);
    }

}

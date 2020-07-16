package com.orion.lang.wrapper;

import java.util.function.Supplier;

/**
 * rpc返回值
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/8/8 19:31
 */
public enum RpcWrapMapper implements Supplier<RpcWrapper<Object>> {

    /**
     * 成功
     */
    OK {
        @Override
        public RpcWrapper<Object> get() {
            return RpcWrapper.ok();
        }
    },

    /**
     * 失败
     */
    ERROR {
        @Override
        public RpcWrapper<Object> get() {
            return RpcWrapper.error();
        }
    }

}

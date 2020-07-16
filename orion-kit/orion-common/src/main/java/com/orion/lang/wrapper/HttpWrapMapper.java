package com.orion.lang.wrapper;

import java.util.function.Supplier;

/**
 * restful返回值
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/8/8 17:25
 */
public enum HttpWrapMapper implements Supplier<HttpWrapper<Object>> {

    /**
     * 成功
     */
    OK {
        @Override
        public HttpWrapper<Object> get() {
            return HttpWrapper.ok();
        }
    },

    /**
     * 失败
     */
    ERROR {
        @Override
        public HttpWrapper<Object> get() {
            return HttpWrapper.error();
        }
    },

    /**
     * 未登录
     */
    NO_LOGIN {
        @Override
        public HttpWrapper<Object> get() {
            return HttpWrapper.wrap(700, "Not signed in");
        }
    },

    /**
     * 无权限
     */
    No_PERMISSION {
        @Override
        public HttpWrapper<Object> get() {
            return HttpWrapper.wrap(701, "No permission");
        }
    }

}

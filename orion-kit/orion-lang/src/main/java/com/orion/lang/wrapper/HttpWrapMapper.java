package com.orion.lang.wrapper;

import java.util.function.Supplier;

/**
 * restful返回值
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/8/8 17:25
 */
public enum HttpWrapMapper implements Supplier<HttpWrapper<?>> {

    /**
     * 成功
     */
    OK(Wrapper.HTTP_OK_CODE, Wrapper.HTTP_OK_MESSAGE),

    /**
     * 失败
     */
    ERROR(Wrapper.HTTP_ERROR_CODE, Wrapper.HTTP_ERROR_MESSAGE),

    /**
     * 未登录
     */
    NO_LOGIN(700, "Not signed in"),

    /**
     * 无权限
     */
    No_PERMISSION(701, "No permission"),

    ;

    private int code;

    private String msg;

    HttpWrapMapper(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public HttpWrapper<?> get() {
        return new HttpWrapper<>(code, msg);
    }

}

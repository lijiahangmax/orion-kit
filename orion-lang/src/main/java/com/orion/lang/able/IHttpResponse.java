package com.orion.lang.able;

/**
 * http 请求接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/7/8 16:47
 */
public interface IHttpResponse {

    /**
     * 获取 url
     *
     * @return url
     */
    String getUrl();

    /**
     * 获取 状态码
     *
     * @return 状态码
     */
    int getCode();

    /**
     * 是否成功
     *
     * @return 是否成功
     */
    default boolean isOk() {
        int code = this.getCode();
        return code >= 200 && code < 300;
    }

}

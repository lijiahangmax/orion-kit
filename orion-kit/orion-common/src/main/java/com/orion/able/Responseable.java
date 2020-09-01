package com.orion.able;

/**
 * 定义回调响应接口
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/11/18 18:22
 */
public interface Responseable<T> {

    void onResponse(T resp);

}

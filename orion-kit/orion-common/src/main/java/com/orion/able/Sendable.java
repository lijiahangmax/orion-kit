package com.orion.able;

/**
 * 发送信息接口
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/11/18 18:16
 */
public interface Sendable<T> {

    boolean send(T msg);

}

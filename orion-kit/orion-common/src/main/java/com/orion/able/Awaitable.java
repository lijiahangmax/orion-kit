package com.orion.able;

/**
 * 同步执行接口
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/17 13:40
 */
public interface Awaitable {

    /**
     * 同步执行
     */
    void await();

}

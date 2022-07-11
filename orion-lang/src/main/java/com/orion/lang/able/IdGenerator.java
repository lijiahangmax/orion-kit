package com.orion.lang.able;

/**
 * id 生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/7/11 17:23
 */
public interface IdGenerator<T> {

    /**
     * 获取下一个id
     *
     * @return id
     */
    T nextId();

}

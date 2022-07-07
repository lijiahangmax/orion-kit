package com.orion.lang.able;

/**
 * 适配器接口
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/11/18 18:12
 */
public interface Adaptable<N> {

    /**
     * 适配为新对象
     *
     * @return N
     */
    N forNew();

}

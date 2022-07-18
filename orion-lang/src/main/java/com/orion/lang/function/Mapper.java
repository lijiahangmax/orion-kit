package com.orion.lang.function;

/**
 * 映射接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/9/9 14:16
 */
@FunctionalInterface
public interface Mapper<I, O> {

    /**
     * 映射
     *
     * @param in input
     * @return output
     */
    O map(I in);

}

package com.orion.lang.define.barrier;

/**
 * 标准屏障对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/18 18:19
 */
public interface GenericsBarrier<O> {

    /**
     * 检测是否需要添加屏障对象 防止穿透
     *
     * @param obj obj
     */
    void check(O obj);

    /**
     * 移除屏障对象
     *
     * @param obj obj
     */
    void remove(O obj);

}

package com.orion.able;

/**
 * 锁接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/20 22:19
 */
public interface Lockable {

    /**
     * 尝试锁定
     *
     * @return true成功
     */
    boolean tryLock();

    /**
     * 解锁
     */
    void unLock();

    /**
     * 是否被锁定
     *
     * @return true锁定
     */
    boolean isLocked();

}

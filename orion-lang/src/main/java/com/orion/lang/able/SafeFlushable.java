package com.orion.lang.able;

import java.io.Flushable;

/**
 * 安全刷新接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/12 13:58
 */
public interface SafeFlushable extends Flushable {

    /**
     * 安全刷新以及强制写出
     */
    @Override
    void flush();

}

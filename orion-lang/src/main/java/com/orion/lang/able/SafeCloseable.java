package com.orion.lang.able;

import java.io.Closeable;

/**
 * 安全关闭接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/22 13:46
 */
public interface SafeCloseable extends Closeable {

    /**
     * 安全关闭
     */
    @Override
    void close();

}

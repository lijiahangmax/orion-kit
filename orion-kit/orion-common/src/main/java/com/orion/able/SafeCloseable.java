package com.orion.able;

import java.io.Closeable;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/22 13:46
 */
public interface SafeCloseable extends Closeable {

    @Override
    void close();

}

package com.orion.lang.utils.io.compress;

import java.util.function.Supplier;

/**
 * 压缩类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 15:52
 */
public interface CompressType<C extends FileCompressor, D extends FileDecompressor> {

    /**
     * @return 压缩器
     */
    Supplier<C> compressor();

    /**
     * @return 解压器
     */
    Supplier<D> decompressor();

    /**
     * @return 后缀
     */
    String suffix();

}

/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.utils.io.compress;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.io.compress.bz2.Bz2Compressor;
import cn.orionsec.kit.lang.utils.io.compress.bz2.Bz2Decompressor;
import cn.orionsec.kit.lang.utils.io.compress.gz.GzCompressor;
import cn.orionsec.kit.lang.utils.io.compress.gz.GzDecompressor;
import cn.orionsec.kit.lang.utils.io.compress.jar.JarCompressor;
import cn.orionsec.kit.lang.utils.io.compress.jar.JarDecompressor;
import cn.orionsec.kit.lang.utils.io.compress.mix.TarBz2Compressor;
import cn.orionsec.kit.lang.utils.io.compress.mix.TarBz2Decompressor;
import cn.orionsec.kit.lang.utils.io.compress.mix.TarGzCompressor;
import cn.orionsec.kit.lang.utils.io.compress.mix.TarGzDecompressor;
import cn.orionsec.kit.lang.utils.io.compress.tar.TarCompressor;
import cn.orionsec.kit.lang.utils.io.compress.tar.TarDecompressor;
import cn.orionsec.kit.lang.utils.io.compress.z7.Z7Compressor;
import cn.orionsec.kit.lang.utils.io.compress.z7.Z7Decompressor;
import cn.orionsec.kit.lang.utils.io.compress.zip.ZipCompressor;
import cn.orionsec.kit.lang.utils.io.compress.zip.ZipDecompressor;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 常用压缩类型枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 15:55
 */
public enum CompressTypeEnum implements CompressType<FileCompressor, FileDecompressor> {

    /**
     * zip
     */
    ZIP(Const.SUFFIX_ZIP, ZipCompressor::new, ZipDecompressor::new),

    /**
     * jar
     */
    JAR(Const.SUFFIX_JAR, JarCompressor::new, JarDecompressor::new),

    /**
     * 7z
     */
    Z7(Const.SUFFIX_7Z, Z7Compressor::new, Z7Decompressor::new),

    /**
     * tar.gz
     */
    TAR_GZ(Const.SUFFIX_TAR_GZ, TarGzCompressor::new, TarGzDecompressor::new),

    /**
     * tar.bz2
     */
    TAR_BZ2(Const.SUFFIX_TAR_BZ2, TarBz2Compressor::new, TarBz2Decompressor::new),

    /**
     * tar
     */
    TAR(Const.SUFFIX_TAR, TarCompressor::new, TarDecompressor::new),

    /**
     * gz
     */
    GZ(Const.SUFFIX_GZ, GzCompressor::new, GzDecompressor::new),

    /**
     * bz2
     */
    BZ2(Const.SUFFIX_BZ2, Bz2Compressor::new, Bz2Decompressor::new),

    ;

    private final String suffix;

    private final Supplier<FileCompressor> compressor;

    private final Supplier<FileDecompressor> decompressor;

    CompressTypeEnum(String suffix, Supplier<FileCompressor> compressor, Supplier<FileDecompressor> decompressor) {
        this.suffix = suffix;
        this.compressor = compressor;
        this.decompressor = decompressor;
    }

    @Override
    public Supplier<FileCompressor> compressor() {
        return compressor;
    }

    @Override
    public Supplier<FileDecompressor> decompressor() {
        return decompressor;
    }

    @Override
    public String suffix() {
        return suffix;
    }

    public static CompressTypeEnum of(String suffix) {
        if (suffix == null) {
            return null;
        }
        for (CompressTypeEnum value : values()) {
            if (suffix.endsWith(value.suffix)) {
                return value;
            }
        }
        return null;
    }

    public static FileCompressor getCompress(String suffix) {
        return Optional.ofNullable(of(suffix))
                .map(CompressTypeEnum::compressor)
                .map(Supplier::get)
                .orElseThrow(() -> Exceptions.unsupported("unsupported compress type"));
    }

    public static FileDecompressor getDecompress(String suffix) {
        return Optional.ofNullable(of(suffix))
                .map(CompressTypeEnum::decompressor)
                .map(Supplier::get)
                .orElseThrow(() -> Exceptions.unsupported("unsupported decompress type"));
    }

}

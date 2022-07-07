package com.orion.lang.utils.io.compress;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.io.compress.bz2.Bz2Compressor;
import com.orion.lang.utils.io.compress.bz2.Bz2Decompressor;
import com.orion.lang.utils.io.compress.gz.GzCompressor;
import com.orion.lang.utils.io.compress.gz.GzDecompressor;
import com.orion.lang.utils.io.compress.jar.JarCompressor;
import com.orion.lang.utils.io.compress.jar.JarDecompressor;
import com.orion.lang.utils.io.compress.mix.TarBz2Compressor;
import com.orion.lang.utils.io.compress.mix.TarBz2Decompressor;
import com.orion.lang.utils.io.compress.mix.TarGzCompressor;
import com.orion.lang.utils.io.compress.mix.TarGzDecompressor;
import com.orion.lang.utils.io.compress.tar.TarCompressor;
import com.orion.lang.utils.io.compress.tar.TarDecompressor;
import com.orion.lang.utils.io.compress.z7.Z7Compressor;
import com.orion.lang.utils.io.compress.z7.Z7Decompressor;
import com.orion.lang.utils.io.compress.zip.ZipCompressor;
import com.orion.lang.utils.io.compress.zip.ZipDecompressor;

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

    CompressTypeEnum(String suffix, Supplier<FileCompressor> compressor, Supplier<FileDecompressor> decompressor) {
        this.suffix = suffix;
        this.compressor = compressor;
        this.decompressor = decompressor;
    }

    private final String suffix;

    private final Supplier<FileCompressor> compressor;

    private final Supplier<FileDecompressor> decompressor;

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

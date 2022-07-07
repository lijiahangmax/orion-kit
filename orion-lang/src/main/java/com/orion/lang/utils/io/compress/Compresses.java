package com.orion.lang.utils.io.compress;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.io.compress.zip.ZipCompressor;
import com.orion.lang.utils.io.compress.zip.ZipDecompressor;

import java.io.File;

/**
 * 压缩解压工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/28 16:22
 */
public class Compresses {

    private Compresses() {
    }

    public static void zip(String dir, String target) {
        zip(new File(dir), target);
    }

    /**
     * 压缩文件
     *
     * @param dir    压缩文件夹
     * @param target 压缩文件
     */
    public static void zip(File dir, String target) {
        try {
            ZipCompressor c = new ZipCompressor();
            c.addFile(dir);
            c.setAbsoluteCompressPath(target);
            c.compress();
        } catch (Exception e) {
            throw Exceptions.runtime("compress file error", e);
        }
    }

    public static void unzip(String target, String unzipDir) {
        unzip(new File(target), new File(unzipDir));
    }

    /**
     * 解压文件
     *
     * @param target   目标文件
     * @param unzipDir 解压文件夹
     */
    public static void unzip(File target, File unzipDir) {
        try {
            ZipDecompressor c = new ZipDecompressor();
            c.setDecompressFile(target);
            c.setDecompressTargetPath(unzipDir);
            c.decompress();
        } catch (Exception e) {
            throw Exceptions.runtime("decompress file error", e);
        }
    }

}

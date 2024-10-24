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

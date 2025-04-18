/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.lang.utils.io.compress.bz2;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.id.ObjectIds;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.lang.utils.io.compress.BaseFileDecompressor;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

/**
 * bzip2 解压器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 21:08
 */
public class Bz2Decompressor extends BaseFileDecompressor {

    private BZip2CompressorInputStream inputStream;

    /**
     * 解压文件输入流
     */
    private InputStream decompressInputStream;

    /**
     * 解压产物输出流
     */
    private OutputStream decompressTargetOutputStream;

    /**
     * 解压产物名称
     */
    private String decompressTargetFileName;

    /**
     * 解压产物路径
     */
    private File decompressTargetFile;

    public Bz2Decompressor() {
        this(Const.SUFFIX_BZ2);
    }

    public Bz2Decompressor(String suffix) {
        super(suffix);
    }

    @Override
    public void decompress() throws Exception {
        if (decompressFile != null) {
            Valid.isTrue(Files1.isFile(decompressFile), "decompress file is not a file");
        } else {
            Valid.notNull(decompressInputStream, "decompress file and decompress input stream is null");
        }
        if (decompressTargetPath == null) {
            Valid.notNull(decompressTargetOutputStream, "decompress target path and is decompress target output stream null");
        }
        this.doDecompress();
    }

    /**
     * 设置解压文件输入流
     *
     * @param decompressInputStream 解压文件输入流
     */
    public void setDecompressInputStream(InputStream decompressInputStream) {
        this.decompressInputStream = decompressInputStream;
    }

    /**
     * 设置解压文件输出流
     *
     * @param decompressTargetOutputStream 解压文件输出流
     */
    public void setDecompressTargetOutputStream(OutputStream decompressTargetOutputStream) {
        this.decompressTargetOutputStream = decompressTargetOutputStream;
    }

    /**
     * 设置解压产物名称
     *
     * @param decompressTargetFileName decompressTargetFileName
     */
    public void setDecompressTargetFileName(String decompressTargetFileName) {
        this.decompressTargetFileName = decompressTargetFileName;
    }

    @Override
    public void doDecompress() throws Exception {
        OutputStream out = null;
        try {
            if (decompressInputStream != null) {
                this.inputStream = new BZip2CompressorInputStream(decompressInputStream);
            } else {
                this.inputStream = new BZip2CompressorInputStream(Files1.openInputStreamFast(decompressFile));
            }
            if (decompressTargetFileName == null) {
                // 配置 > file > objectId
                this.decompressTargetFileName = Optional.ofNullable(decompressFile)
                        .map(File::getName)
                        .map(s -> s.substring(0, s.length() - suffix.length() - 1))
                        .orElseGet(ObjectIds::nextId);
            }
            if (decompressTargetOutputStream != null) {
                out = decompressTargetOutputStream;
            } else {
                this.decompressTargetFile = new File(decompressTargetPath, decompressTargetFileName);
                out = Files1.openOutputStream(decompressTargetFile);
            }
            Streams.transfer(inputStream, out);
        } finally {
            Streams.close(inputStream);
            if (decompressTargetOutputStream == null) {
                Streams.close(out);
            }
        }
    }

    public File getDecompressTargetFile() {
        return decompressTargetFile;
    }

    @Override
    public BZip2CompressorInputStream getCloseable() {
        return inputStream;
    }

}

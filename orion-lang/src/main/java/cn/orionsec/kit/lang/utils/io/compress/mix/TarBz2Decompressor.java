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
package cn.orionsec.kit.lang.utils.io.compress.mix;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.lang.utils.io.compress.BaseFileDecompressor;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.OutputStream;

/**
 * tar.bz2 解压器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 21:08
 */
public class TarBz2Decompressor extends BaseFileDecompressor {

    private TarArchiveInputStream inputStream;

    public TarBz2Decompressor() {
        this(Const.SUFFIX_TAR_BZ2);
    }

    public TarBz2Decompressor(String suffix) {
        super(suffix);
    }

    @Override
    public void doDecompress() throws Exception {
        try (BufferedInputStream bi = new BufferedInputStream(Files1.openInputStreamFast(decompressFile));
             BZip2CompressorInputStream bz2In = new BZip2CompressorInputStream(bi)) {
            this.inputStream = new TarArchiveInputStream(bz2In);
            ArchiveEntry entry;
            while ((entry = inputStream.getNextEntry()) != null) {
                File file = new File(decompressTargetPath, entry.getName());
                if (entry.isDirectory()) {
                    Files1.mkdirs(file);
                } else {
                    try (OutputStream out = Files1.openOutputStreamFast(file)) {
                        Streams.transfer(inputStream, out);
                    }
                }
            }
        } finally {
            Streams.close(inputStream);
        }
    }

    @Override
    public TarArchiveInputStream getCloseable() {
        return inputStream;
    }
}

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
package cn.orionsec.kit.lang.utils.io.compress.zip;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.lang.utils.io.compress.BaseFileCompressor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * zip 压缩器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 16:37
 */
public class ZipCompressor extends BaseFileCompressor {

    private ZipOutputStream outputStream;

    public ZipCompressor() {
        this(Const.SUFFIX_ZIP);
    }

    public ZipCompressor(String suffix) {
        super(suffix);
    }

    @Override
    public void doCompress() throws Exception {
        try {
            this.outputStream = new ZipOutputStream(new BufferedOutputStream(Files1.openOutputStreamFast(this.getAbsoluteCompressPath())));
            // 设置压缩文件
            for (Map.Entry<String, File> fileEntity : compressFiles.entrySet()) {
                try (InputStream in = Files1.openInputStreamFast(fileEntity.getValue())) {
                    outputStream.putNextEntry(new ZipEntry(fileEntity.getKey()));
                    Streams.transfer(in, outputStream);
                    outputStream.closeEntry();
                    super.notify(fileEntity.getKey());
                }
            }
            for (Map.Entry<String, InputStream> fileEntity : compressStreams.entrySet()) {
                outputStream.putNextEntry(new ZipEntry(fileEntity.getKey()));
                Streams.transfer(fileEntity.getValue(), outputStream);
                outputStream.closeEntry();
                super.notify(fileEntity.getKey());
            }
            outputStream.finish();
        } finally {
            Streams.close(outputStream);
        }
    }

    @Override
    public ZipOutputStream getCloseable() {
        return outputStream;
    }

}

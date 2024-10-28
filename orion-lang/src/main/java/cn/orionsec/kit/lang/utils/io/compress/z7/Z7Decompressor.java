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
package cn.orionsec.kit.lang.utils.io.compress.z7;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.lang.utils.io.compress.BaseFileDecompressor;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 7z 解压器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 21:08
 */
public class Z7Decompressor extends BaseFileDecompressor {

    private SevenZFile z7File;

    public Z7Decompressor() {
        this(Const.SUFFIX_7Z);
    }

    public Z7Decompressor(String suffix) {
        super(suffix);
    }

    @Override
    public void doDecompress() throws Exception {
        try {
            this.z7File = new SevenZFile(decompressFile);
            ArchiveEntry entry;
            while ((entry = z7File.getNextEntry()) != null) {
                File file = new File(decompressTargetPath, entry.getName());
                if (entry.isDirectory()) {
                    Files1.mkdirs(file);
                } else {
                    try (OutputStream out = Files1.openOutputStreamFast(file)) {
                        transfer(z7File, out);
                    }
                }
            }
        } finally {
            Streams.close(z7File);
        }
    }

    /**
     * 传输
     */
    public static void transfer(SevenZFile archive, OutputStream output) throws IOException {
        byte[] buffer = new byte[Const.BUFFER_KB_8];
        int n;
        while ((n = archive.read(buffer)) != -1) {
            output.write(buffer, 0, n);
        }
    }

    @Override
    public SevenZFile getCloseable() {
        return z7File;
    }

}

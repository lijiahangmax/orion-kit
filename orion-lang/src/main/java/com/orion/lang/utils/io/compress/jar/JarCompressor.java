package com.orion.lang.utils.io.compress.jar;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.lang.utils.io.compress.BaseFileCompressor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.zip.ZipOutputStream;

/**
 * jar压缩器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/28 8:58
 */
public class JarCompressor extends BaseFileCompressor {

    private ZipOutputStream outputStream;

    public JarCompressor() {
        this(Const.SUFFIX_JAR);
    }

    public JarCompressor(String suffix) {
        super(suffix);
    }

    @Override
    public void doCompress() throws Exception {
        try {
            this.outputStream = new ZipOutputStream(new BufferedOutputStream(Files1.openOutputStreamFast(this.getAbsoluteCompressPath())));
            // 设置压缩文件
            for (Map.Entry<String, File> fileEntity : compressFiles.entrySet()) {
                try (InputStream in = Files1.openInputStreamFast(fileEntity.getValue())) {
                    outputStream.putNextEntry(new JarEntry(fileEntity.getKey()));
                    Streams.transfer(in, outputStream);
                    outputStream.closeEntry();
                    super.notify(fileEntity.getKey());
                }
            }
            for (Map.Entry<String, InputStream> fileEntity : compressStreams.entrySet()) {
                outputStream.putNextEntry(new JarEntry(fileEntity.getKey()));
                Streams.transfer(fileEntity.getValue(), outputStream);
                outputStream.closeEntry();
                super.notify(fileEntity.getKey());
            }
            outputStream.flush();
        } finally {
            Streams.close(outputStream);
        }
    }

    @Override
    public ZipOutputStream getCloseable() {
        return outputStream;
    }

}

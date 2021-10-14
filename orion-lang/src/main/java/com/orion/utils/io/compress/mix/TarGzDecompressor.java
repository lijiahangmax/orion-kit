package com.orion.utils.io.compress.mix;

import com.orion.constant.Const;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import com.orion.utils.io.compress.BaseFileDecompressor;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.OutputStream;

/**
 * tar.gz解压器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 21:08
 */
public class TarGzDecompressor extends BaseFileDecompressor {

    public TarGzDecompressor() {
        this(Const.SUFFIX_TAR_GZ);
    }

    public TarGzDecompressor(String suffix) {
        super(suffix);
    }

    @Override
    public void doDecompress() throws Exception {
        try (BufferedInputStream bi = new BufferedInputStream(Files1.openInputStreamFast(decompressFile));
             GzipCompressorInputStream gzIn = new GzipCompressorInputStream(bi);
             TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn)) {
            ArchiveEntry entry;
            while ((entry = tarIn.getNextEntry()) != null) {
                File file = new File(decompressTargetPath, entry.getName());
                if (entry.isDirectory()) {
                    Files1.mkdirs(file);
                } else {
                    try (OutputStream out = Files1.openOutputStreamFast(file)) {
                        Streams.transfer(tarIn, out);
                    }
                }
            }
        }
    }

}

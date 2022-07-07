package com.orion.lang.utils.io.compress.tar;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.lang.utils.io.compress.BaseFileDecompressor;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.File;
import java.io.OutputStream;

/**
 * tar解压器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 21:08
 */
public class TarDecompressor extends BaseFileDecompressor {

    private TarArchiveInputStream inputStream;

    public TarDecompressor() {
        this(Const.SUFFIX_TAR);
    }

    public TarDecompressor(String suffix) {
        super(suffix);
    }

    @Override
    public void doDecompress() throws Exception {
        try {
            this.inputStream = new TarArchiveInputStream(Files1.openInputStreamFast(decompressFile));
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

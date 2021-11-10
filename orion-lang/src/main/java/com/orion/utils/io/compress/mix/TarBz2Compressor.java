package com.orion.utils.io.compress.mix;

import com.orion.constant.Const;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import com.orion.utils.io.compress.BaseFileCompressor;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * tar.bz2压缩器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 16:37
 */
public class TarBz2Compressor extends BaseFileCompressor {

    private TarArchiveOutputStream outputStream;

    public TarBz2Compressor() {
        this(Const.SUFFIX_TAR_BZ2);
    }

    public TarBz2Compressor(String suffix) {
        super(suffix);
    }

    @Override
    public void doCompress() throws Exception {
        try (BufferedOutputStream fileOut = new BufferedOutputStream(Files1.openOutputStreamFast(this.getAbsoluteCompressPath()));
             BZip2CompressorOutputStream bz2Out = new BZip2CompressorOutputStream(fileOut)) {
            this.outputStream = new TarArchiveOutputStream(bz2Out);
            // 设置压缩文件
            for (Map.Entry<String, File> fileEntity : compressFiles.entrySet()) {
                ArchiveEntry entity = outputStream.createArchiveEntry(fileEntity.getValue(), fileEntity.getKey());
                outputStream.putArchiveEntry(entity);
                try (InputStream in = Files1.openInputStreamFast(fileEntity.getValue())) {
                    Streams.transfer(in, outputStream);
                }
                outputStream.closeArchiveEntry();
                super.notify(fileEntity.getKey());
            }
            for (Map.Entry<String, InputStream> fileEntity : compressStreams.entrySet()) {
                TarArchiveEntry entity = new TarArchiveEntry(fileEntity.getKey());
                entity.setSize(fileEntity.getValue().available());
                outputStream.putArchiveEntry(entity);
                Streams.transfer(fileEntity.getValue(), outputStream);
                outputStream.closeArchiveEntry();
                super.notify(fileEntity.getKey());
            }
            outputStream.finish();
        } finally {
            Streams.close(outputStream);
        }
    }

    @Override
    public TarArchiveOutputStream getCloseable() {
        return outputStream;
    }

}

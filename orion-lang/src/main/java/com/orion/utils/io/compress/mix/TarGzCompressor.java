package com.orion.utils.io.compress.mix;

import com.orion.constant.Const;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import com.orion.utils.io.compress.BaseFileCompressor;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * tar.gz压缩器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 16:37
 */
public class TarGzCompressor extends BaseFileCompressor {

    public TarGzCompressor() {
        this(Const.SUFFIX_TAR_GZ);
    }

    public TarGzCompressor(String suffix) {
        super(suffix);
    }

    @Override
    public void doCompress() throws Exception {
        try (BufferedOutputStream fileOut = new BufferedOutputStream(Files1.openOutputStreamFast(this.getAbsoluteCompressPath()));
             GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(fileOut);
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(gzOut)) {

            // 设置压缩文件
            for (Map.Entry<String, File> fileEntity : compressFiles.entrySet()) {
                ArchiveEntry entity = tarOut.createArchiveEntry(fileEntity.getValue(), fileEntity.getKey());
                tarOut.putArchiveEntry(entity);
                try (InputStream in = Files1.openInputStreamFast(fileEntity.getValue())) {
                    Streams.transfer(in, tarOut);
                }
                tarOut.closeArchiveEntry();
            }
            for (Map.Entry<String, InputStream> fileEntity : compressStreams.entrySet()) {
                TarArchiveEntry entity = new TarArchiveEntry(fileEntity.getKey());
                entity.setSize(fileEntity.getValue().available());
                tarOut.putArchiveEntry(entity);
                Streams.transfer(fileEntity.getValue(), tarOut);
                tarOut.closeArchiveEntry();
            }
            tarOut.finish();
        }
    }

}

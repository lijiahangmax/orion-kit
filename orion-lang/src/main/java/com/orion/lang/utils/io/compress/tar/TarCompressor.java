package com.orion.lang.utils.io.compress.tar;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.lang.utils.io.compress.BaseFileCompressor;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * tar压缩器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 16:37
 */
public class TarCompressor extends BaseFileCompressor {

    private TarArchiveOutputStream outputStream;

    public TarCompressor() {
        this(Const.SUFFIX_TAR);
    }

    public TarCompressor(String suffix) {
        super(suffix);
    }

    @Override
    public void doCompress() throws Exception {
        try {
            this.outputStream = new TarArchiveOutputStream(Files1.openOutputStreamFast(new File(this.getAbsoluteCompressPath())));
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

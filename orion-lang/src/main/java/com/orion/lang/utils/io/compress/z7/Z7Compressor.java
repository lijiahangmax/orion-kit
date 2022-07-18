package com.orion.lang.utils.io.compress.z7;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.lang.utils.io.compress.BaseFileCompressor;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 7z 压缩器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 16:37
 */
public class Z7Compressor extends BaseFileCompressor {

    private SevenZOutputFile z7OutputFile;

    public Z7Compressor() {
        this(Const.SUFFIX_7Z);
    }

    public Z7Compressor(String suffix) {
        super(suffix);
    }

    @Override
    public void doCompress() throws Exception {
        try {
            this.z7OutputFile = new SevenZOutputFile(new File(this.getAbsoluteCompressPath()));
            // 设置压缩文件
            for (Map.Entry<String, File> fileEntity : compressFiles.entrySet()) {
                SevenZArchiveEntry entity = z7OutputFile.createArchiveEntry(fileEntity.getValue(), fileEntity.getKey());
                z7OutputFile.putArchiveEntry(entity);
                try (InputStream in = Files1.openInputStreamFast(fileEntity.getValue())) {
                    transfer(in, z7OutputFile);
                }
                z7OutputFile.closeArchiveEntry();
                super.notify(fileEntity.getKey());
            }
            for (Map.Entry<String, InputStream> fileEntity : compressStreams.entrySet()) {
                SevenZArchiveEntry entity = new SevenZArchiveEntry();
                entity.setName(fileEntity.getKey());
                entity.setSize(fileEntity.getValue().available());
                z7OutputFile.putArchiveEntry(entity);
                transfer(fileEntity.getValue(), z7OutputFile);
                z7OutputFile.closeArchiveEntry();
                super.notify(fileEntity.getKey());
            }
            z7OutputFile.finish();
        } finally {
            Streams.close(z7OutputFile);
        }
    }

    /**
     * 传输
     */
    protected static void transfer(InputStream input, SevenZOutputFile out) throws IOException {
        byte[] buffer = new byte[Const.BUFFER_KB_8];
        int n;
        while ((n = input.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
    }

    @Override
    public SevenZOutputFile getCloseable() {
        return z7OutputFile;
    }

}

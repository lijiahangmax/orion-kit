package com.orion.utils.io.compress.z7;

import com.orion.constant.Const;
import com.orion.utils.io.Files1;
import com.orion.utils.io.compress.BaseFileCompressor;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 7z压缩器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 16:37
 */
public class Z7Compressor extends BaseFileCompressor {

    public Z7Compressor() {
        this(Const.SUFFIX_7Z);
    }

    public Z7Compressor(String suffix) {
        super(suffix);
    }

    @Override
    public void doCompress() throws Exception {
        try (SevenZOutputFile out = new SevenZOutputFile(new File(this.getAbsoluteCompressPath()))) {
            // 设置压缩文件
            for (Map.Entry<String, File> fileEntity : compressFiles.entrySet()) {
                SevenZArchiveEntry entity = out.createArchiveEntry(fileEntity.getValue(), fileEntity.getKey());
                out.putArchiveEntry(entity);
                try (InputStream in = Files1.openInputStreamFast(fileEntity.getValue())) {
                    transfer(in, out);
                }
                out.closeArchiveEntry();
            }
            for (Map.Entry<String, InputStream> fileEntity : compressStreams.entrySet()) {
                SevenZArchiveEntry entity = new SevenZArchiveEntry();
                entity.setName(fileEntity.getKey());
                entity.setSize(fileEntity.getValue().available());
                out.putArchiveEntry(entity);
                transfer(fileEntity.getValue(), out);
                out.closeArchiveEntry();
            }
            out.finish();
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

}

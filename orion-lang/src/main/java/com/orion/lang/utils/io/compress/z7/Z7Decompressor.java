package com.orion.lang.utils.io.compress.z7;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.lang.utils.io.compress.BaseFileDecompressor;
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

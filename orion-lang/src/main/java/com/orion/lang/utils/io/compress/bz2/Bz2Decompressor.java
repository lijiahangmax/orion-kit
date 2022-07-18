package com.orion.lang.utils.io.compress.bz2;

import com.orion.lang.constant.Const;
import com.orion.lang.id.ObjectIds;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.lang.utils.io.compress.BaseFileDecompressor;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

/**
 * bzip2 解压器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 21:08
 */
public class Bz2Decompressor extends BaseFileDecompressor {

    private BZip2CompressorInputStream inputStream;

    /**
     * 解压文件输入流
     */
    private InputStream decompressInputStream;

    /**
     * 解压产物输出流
     */
    private OutputStream decompressTargetOutputStream;

    /**
     * 解压产物名称
     */
    private String decompressTargetFileName;

    /**
     * 解压产物路径
     */
    private File decompressTargetFile;

    public Bz2Decompressor() {
        this(Const.SUFFIX_BZ2);
    }

    public Bz2Decompressor(String suffix) {
        super(suffix);
    }

    @Override
    public void decompress() throws Exception {
        if (decompressFile != null) {
            Valid.isTrue(Files1.isFile(decompressFile), "decompress file is not a file");
        } else {
            Valid.notNull(decompressInputStream, "decompress file and decompress input stream is null");
        }
        if (decompressTargetPath == null) {
            Valid.notNull(decompressTargetOutputStream, "decompress target path and is decompress target output stream null");
        }
        this.doDecompress();
    }

    /**
     * 设置解压文件输入流
     *
     * @param decompressInputStream 解压文件输入流
     */
    public void setDecompressInputStream(InputStream decompressInputStream) {
        this.decompressInputStream = decompressInputStream;
    }

    /**
     * 设置解压文件输出流
     *
     * @param decompressTargetOutputStream 解压文件输出流
     */
    public void setDecompressTargetOutputStream(OutputStream decompressTargetOutputStream) {
        this.decompressTargetOutputStream = decompressTargetOutputStream;
    }

    /**
     * 设置解压产物名称
     *
     * @param decompressTargetFileName decompressTargetFileName
     */
    public void setDecompressTargetFileName(String decompressTargetFileName) {
        this.decompressTargetFileName = decompressTargetFileName;
    }

    @Override
    public void doDecompress() throws Exception {
        OutputStream out = null;
        try {
            if (decompressInputStream != null) {
                this.inputStream = new BZip2CompressorInputStream(decompressInputStream);
            } else {
                this.inputStream = new BZip2CompressorInputStream(Files1.openInputStreamFast(decompressFile));
            }
            if (decompressTargetFileName == null) {
                // 配置 > file > objectId
                this.decompressTargetFileName = Optional.ofNullable(decompressFile)
                        .map(File::getName)
                        .map(s -> s.substring(0, s.length() - suffix.length() - 1))
                        .orElseGet(ObjectIds::nextId);
            }
            if (decompressTargetOutputStream != null) {
                out = decompressTargetOutputStream;
            } else {
                this.decompressTargetFile = new File(decompressTargetPath, decompressTargetFileName);
                out = Files1.openOutputStream(decompressTargetFile);
            }
            Streams.transfer(inputStream, out);
        } finally {
            Streams.close(inputStream);
            if (decompressTargetOutputStream == null) {
                Streams.close(out);
            }
        }
    }

    public File getDecompressTargetFile() {
        return decompressTargetFile;
    }

    @Override
    public BZip2CompressorInputStream getCloseable() {
        return inputStream;
    }

}

package com.orion.lang.utils.io.compress.gz;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.lang.utils.io.compress.BaseFileCompressor;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * gzip 压缩器
 * gzip 只能压缩单个文件
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 16:37
 */
public class GzCompressor extends BaseFileCompressor {

    private GzipCompressorOutputStream outputStream;

    /**
     * 压缩文件
     *
     * @see #compressInputStream
     */
    private File compressFile;

    /**
     * 压缩输入流
     *
     * @see #compressFile
     */
    private InputStream compressInputStream;

    /**
     * 压缩输出流
     *
     * @see #setCompressOutputStream(OutputStream)
     */
    private OutputStream compressOutputStream;

    /**
     * 压缩文件名称
     */
    private String compressEntityName;

    public GzCompressor() {
        this(Const.SUFFIX_GZ);
    }

    public GzCompressor(String suffix) {
        super(suffix);
    }

    @Override
    public void addFile(String file) {
        this.unsupportedOperation();
    }

    @Override
    public void addFile(File file) {
        this.unsupportedOperation();
    }

    @Override
    public void addFile(String name, String file) {
        this.unsupportedOperation();
    }

    @Override
    public void addFile(String name, File file) {
        this.unsupportedOperation();
    }

    @Override
    public void addFilePrefix(String prefix, String file) {
        this.unsupportedOperation();
    }

    @Override
    public void addFilePrefix(String prefix, File file) {
        this.unsupportedOperation();
    }

    @Override
    public void addFile(String name, byte[] bs) {
        this.unsupportedOperation();
    }

    @Override
    public void addFile(String name, InputStream in) {
        this.unsupportedOperation();
    }

    public void setCompressFile(String file) {
        Valid.notBlank(file, "compress file is null");
        this.setCompressFile(new File(file));
    }

    /**
     * 设置压缩文件
     *
     * @param file 压缩文件
     */
    public void setCompressFile(File file) {
        Valid.notNull(file, "compress file is null");
        Valid.isTrue(Files1.isFile(file), "compress file must be a normal file");
        this.compressFile = file;
        this.compressEntityName = file.getName();
        this.setFileName(file.getName());
    }

    public void setCompressFile(byte[] bs) {
        this.setCompressFile(null, bs);
    }

    public void setCompressFile(String name, byte[] bs) {
        this.compressEntityName = name;
        this.compressInputStream = Streams.toInputStream(bs);
        if (name != null) {
            this.setFileName(name);
        }
    }

    public void setCompressFile(InputStream in) {
        this.setCompressFile(null, in);
    }

    /**
     * 设置压缩文件
     *
     * @param name name
     * @param in   in
     */
    public void setCompressFile(String name, InputStream in) {
        this.compressEntityName = name;
        this.compressInputStream = in;
        if (name != null) {
            this.setFileName(name);
        }
    }

    /**
     * 设置压缩输出流
     *
     * @param compressOutputStream compressOutputStream
     */
    public void setCompressOutputStream(OutputStream compressOutputStream) {
        this.compressOutputStream = compressOutputStream;
    }

    @Override
    public void compress() throws Exception {
        Valid.isFalse(compressFile == null && compressInputStream == null, "gzip compress file just support compress one file");
        this.doCompress();
    }

    @Override
    public void doCompress() throws Exception {
        GzipParameters params = new GzipParameters();
        params.setFilename(compressEntityName);
        InputStream in = null;
        try {
            if (compressOutputStream != null) {
                this.outputStream = new GzipCompressorOutputStream(compressOutputStream, params);
            } else {
                this.outputStream = new GzipCompressorOutputStream(Files1.openOutputStreamFast(new File(this.getAbsoluteCompressPath())), params);
            }
            if (compressInputStream != null) {
                in = compressInputStream;
            } else {
                in = Files1.openInputStreamFast(compressFile);
            }
            Streams.transfer(in, outputStream);
            super.notify(compressEntityName);
        } finally {
            Streams.close(outputStream);
            if (compressInputStream == null) {
                Streams.close(in);
            }
        }
    }

    @Override
    public GzipCompressorOutputStream getCloseable() {
        return outputStream;
    }

}

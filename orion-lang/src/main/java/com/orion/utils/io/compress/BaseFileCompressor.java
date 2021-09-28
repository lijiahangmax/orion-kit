package com.orion.utils.io.compress;

import com.orion.id.ObjectIds;
import com.orion.utils.Systems;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件压缩器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 16:47
 */
public abstract class BaseFileCompressor implements FileCompressor {

    /**
     * 需要压缩的文件
     *
     * @see #addFile(File)
     * @see #addFile(String)
     */
    protected Map<String, File> compressFiles;

    /**
     * 需要压缩的文件
     *
     * @see #addFile(String, byte[])
     */
    protected Map<String, InputStream> compressStreams;

    /**
     * 后缀
     */
    protected String suffix;

    /**
     * 压缩产物文件目录
     */
    private String compressPath;

    /**
     * 压缩产物文件名称
     */
    private String fileName;

    public BaseFileCompressor(String suffix) {
        this.compressFiles = new LinkedHashMap<>();
        this.compressStreams = new LinkedHashMap<>();
        this.compressPath = Systems.TEMP_DIR;
        this.fileName = ObjectIds.next();
        this.suffix = suffix;
    }

    @Override
    public void addFile(String file) {
        this.addFile(new File(file));
    }

    @Override
    public void addFile(File file) {
        if (file.isDirectory()) {
            int len = Files1.getPath(file.getParent()).length();
            List<File> files = Files1.listFiles(file, true);
            for (File dirFile : files) {
                compressFiles.put(Files1.getPath(dirFile.getAbsolutePath()).substring(len + 1), dirFile);
            }
        } else {
            compressFiles.put(file.getName(), file);
        }
    }

    @Override
    public void addFile(String name, byte[] bs) {
        compressStreams.put(name, Streams.toInputStream(bs));
    }

    @Override
    public void addFile(String name, InputStream in) {
        compressStreams.put(name, in);
    }

    @Override
    public void compress() throws Exception {
        Valid.isFalse(compressFiles.isEmpty() && compressStreams.isEmpty(), "compress entities is empty");
        this.doCompress();
    }

    /**
     * 执行压缩
     *
     * @throws Exception Exception
     */
    protected abstract void doCompress() throws Exception;

    @Override
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getAbsoluteCompressPath() {
        return Files1.getPath(compressPath + "/" + fileName + "." + suffix);
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

}

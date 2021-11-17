package com.orion.utils.io.compress;

import com.orion.id.ObjectIds;
import com.orion.utils.Exceptions;
import com.orion.utils.Systems;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
     * @see #addFile(String, String)
     * @see #addFile(String, File)
     * @see #addFilePrefix(String, String)
     * @see #addFilePrefix(String, File)
     */
    protected Map<String, File> compressFiles;

    /**
     * 需要压缩的文件
     *
     * @see #addFile(String, byte[])
     * @see #addFile(String, InputStream)
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

    /**
     * 压缩产物绝对路径
     */
    private String absoluteCompressPath;

    /**
     * 压缩通知
     */
    private Consumer<String> notify;

    public BaseFileCompressor(String suffix) {
        this.compressFiles = new LinkedHashMap<>();
        this.compressStreams = new LinkedHashMap<>();
        this.compressPath = Systems.TEMP_DIR;
        this.fileName = ObjectIds.next();
        this.suffix = suffix;
    }

    @Override
    public void addFile(String file) {
        Valid.notBlank(file, "file is null");
        this.addFile(new File(file));
    }

    @Override
    public void addFile(File file) {
        Valid.notNull(file, "file is null");
        if (!file.exists()) {
            throw Exceptions.notFound(file + " compress file not found");
        }
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
    public void addFile(String name, String file) {
        Valid.notBlank(file, "file is null");
        this.addFile(name, new File(file));
    }

    @Override
    public void addFile(String name, File file) {
        Valid.notBlank(name, "name is null");
        Valid.notNull(file, "file is null");
        if (!file.exists()) {
            throw Exceptions.notFound(file + " compress file not found");
        }
        if (file.isDirectory()) {
            throw Exceptions.unsupported("set name unsupported add directory");
        }
        compressFiles.put(name, file);
    }

    @Override
    public void addFilePrefix(String prefix, String file) {
        Valid.notNull(file, "file is null");
        this.addFilePrefix(prefix, new File(file));
    }

    @Override
    public void addFilePrefix(String prefix, File file) {
        Valid.notBlank(prefix, "prefix is null");
        Valid.notNull(file, "file is null");
        if (!file.exists()) {
            throw Exceptions.notFound(file + " compress file not found");
        }
        if (!prefix.endsWith("/")) {
            prefix += "/";
        }
        if (file.isDirectory()) {
            int len = Files1.getPath(file.getParent()).length();
            List<File> files = Files1.listFiles(file, true);
            for (File dirFile : files) {
                compressFiles.put(prefix + Files1.getPath(dirFile.getAbsolutePath()).substring(len + 1), dirFile);
            }
        } else {
            compressFiles.put(prefix + file.getName(), file);
        }
    }

    @Override
    public void addFile(String name, byte[] bs) {
        Valid.notBlank(name, "name is null");
        Valid.notNull(bs, "byte array is null");
        compressStreams.put(name, Streams.toInputStream(bs));
    }

    @Override
    public void addFile(String name, InputStream in) {
        Valid.notBlank(name, "name is null");
        Valid.notNull(in, "input stream is null");
        compressStreams.put(name, in);
    }

    @Override
    public void compressNotify(Consumer<String> notify) {
        this.notify = notify;
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
    public void setAbsoluteCompressPath(String path) {
        this.absoluteCompressPath = path;
    }

    @Override
    public String getAbsoluteCompressPath() {
        if (absoluteCompressPath != null) {
            return absoluteCompressPath;
        }
        return Files1.getPath(compressPath, fileName + "." + suffix);
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    /**
     * 不支持操作异常
     */
    protected void unsupportedOperation() {
        throw Exceptions.unsupported("compress file not support operation");
    }

    /**
     * 压缩通知
     *
     * @param name name
     */
    protected void notify(String name) {
        if (notify != null) {
            notify.accept(name);
        }
    }

}

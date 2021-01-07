package com.orion.utils.io;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 文件属性
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/27 14:43
 */
public class FileAttribute implements Serializable {

    private static final long serialVersionUID = 90456092839304098L;

    public FileAttribute(Path path, BasicFileAttributes attr) {
        this.path = path;
        this.attr = attr;
        this.filePath = path.toString();
        this.fileName = Files1.getFileName(filePath);
        this.size = attr.size();
        this.symbolicLink = attr.isSymbolicLink();
        this.directory = attr.isDirectory();
        this.regularFile = attr.isRegularFile();
        this.other = attr.isOther();
        this.createTime = attr.creationTime().toMillis();
        this.accessTime = attr.lastAccessTime().toMillis();
        this.modifiedTime = attr.lastModifiedTime().toMillis();
    }

    /**
     * path
     */
    private Path path;

    /**
     * attr
     */
    private BasicFileAttributes attr;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 是否为连接文件
     */
    private boolean symbolicLink;

    /**
     * 是否为文件夹
     */
    private boolean directory;

    /**
     * 是否为常规文件
     */
    private boolean regularFile;

    /**
     * 是否为其他文件
     */
    private boolean other;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 最后访问时间
     */
    private long accessTime;

    /**
     * 最后修改时间
     */
    private long modifiedTime;

    public Path getPath() {
        return path;
    }

    public FileAttribute setPath(Path path) {
        this.path = path;
        return this;
    }

    public BasicFileAttributes getAttr() {
        return attr;
    }

    public String getFileName() {
        return fileName;
    }

    public FileAttribute setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public FileAttribute setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public long getSize() {
        return size;
    }

    public FileAttribute setSize(long size) {
        this.size = size;
        return this;
    }

    public boolean isSymbolicLink() {
        return symbolicLink;
    }

    public FileAttribute setSymbolicLink(boolean symbolicLink) {
        this.symbolicLink = symbolicLink;
        return this;
    }

    public boolean isDirectory() {
        return directory;
    }

    public FileAttribute setDirectory(boolean directory) {
        this.directory = directory;
        return this;
    }

    public boolean isRegularFile() {
        return regularFile;
    }

    public FileAttribute setRegularFile(boolean regularFile) {
        this.regularFile = regularFile;
        return this;
    }

    public boolean isOther() {
        return other;
    }

    public FileAttribute setOther(boolean other) {
        this.other = other;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public FileAttribute setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public long getAccessTime() {
        return accessTime;
    }

    public FileAttribute setAccessTime(long accessTime) {
        this.accessTime = accessTime;
        return this;
    }

    public long getModifiedTime() {
        return modifiedTime;
    }

    public FileAttribute setModifiedTime(long modifiedTime) {
        this.modifiedTime = modifiedTime;
        return this;
    }

    public File getFile() {
        return path.toFile();
    }

    public URI getUri() {
        return path.toUri();
    }

    @Override
    public String toString() {
        return filePath;
    }

}

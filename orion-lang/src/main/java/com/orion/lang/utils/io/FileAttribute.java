/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.orion.lang.utils.io;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 文件属性
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/27 14:43
 */
public class FileAttribute implements Serializable {

    private static final long serialVersionUID = 90456092839304098L;

    /**
     * attr
     */
    private final BasicFileAttributes attr;

    /**
     * path
     */
    private Path path;

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

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public BasicFileAttributes getAttr() {
        return attr;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isSymbolicLink() {
        return symbolicLink;
    }

    public void setSymbolicLink(boolean symbolicLink) {
        this.symbolicLink = symbolicLink;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public boolean isRegularFile() {
        return regularFile;
    }

    public void setRegularFile(boolean regularFile) {
        this.regularFile = regularFile;
    }

    public boolean isOther() {
        return other;
    }

    public void setOther(boolean other) {
        this.other = other;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }

    public long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(long modifiedTime) {
        this.modifiedTime = modifiedTime;
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

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
package com.orion.lang.utils.io.compress;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 文件压缩器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 15:57
 */
public interface FileCompressor {

    /**
     * 添加文件
     *
     * @param file file
     */
    void addFile(String file);

    /**
     * 添加文件
     *
     * @param file file
     */
    void addFile(File file);

    /**
     * 添加文件
     *
     * @param name name
     * @param file file
     */
    void addFile(String name, String file);

    /**
     * 添加文件
     *
     * @param name name
     * @param file file
     */
    void addFile(String name, File file);

    /**
     * 添加文件
     *
     * @param prefix prefix
     * @param file   file
     */
    void addFilePrefix(String prefix, String file);

    /**
     * 添加文件
     *
     * @param prefix prefix
     * @param file   file
     */
    void addFilePrefix(String prefix, File file);

    /**
     * 添加文件
     *
     * @param name name
     * @param bs   bs
     */
    void addFile(String name, byte[] bs);

    /**
     * 添加文件
     *
     * @param name name
     * @param in   in
     */
    void addFile(String name, InputStream in);

    /**
     * 压缩通知
     *
     * @param notify notify
     */
    void compressNotify(Consumer<String> notify);

    /**
     * 进行压缩
     *
     * @throws Exception compressException
     */
    void compress() throws Exception;

    /**
     * 设置后缀
     *
     * @param suffix 后缀
     */
    void setSuffix(String suffix);

    /**
     * 设置压缩产物文件目录
     *
     * @param compressPath 压缩文件目录
     */
    void setCompressPath(String compressPath);

    /**
     * 设置压缩产物文件名
     */
    void setFileName(String fileName);

    /**
     * 设置压缩产物文件绝对路径
     *
     * @param path 压缩产物文件绝对路径
     */
    void setAbsoluteCompressPath(String path);

    /**
     * 获取压缩产物文件绝对路径
     *
     * @return 压缩产物文件绝对路径
     */
    String getAbsoluteCompressPath();

    /**
     * 获取后缀
     *
     * @return 后缀
     */
    String getSuffix();

    /**
     * 获取可关闭接口
     *
     * @return closeable
     */
    Closeable getCloseable();

}

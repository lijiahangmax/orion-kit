/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.lang.utils.io.compress;

import java.io.Closeable;
import java.io.File;

/**
 * 文件解压器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 15:57
 */
public interface FileDecompressor {

    /**
     * 设置解压文件
     *
     * @param decompressFile decompressFile
     */
    void setDecompressFile(String decompressFile);

    /**
     * 设置解压文件
     *
     * @param decompressFile decompressFile
     */
    void setDecompressFile(File decompressFile);

    /**
     * 设置解压路径
     *
     * @param decompressPath 解压路径
     */
    void setDecompressTargetPath(String decompressPath);

    /**
     * 设置解压路径
     *
     * @param decompressPath 解压路径
     */
    void setDecompressTargetPath(File decompressPath);

    /**
     * 设置后缀
     *
     * @param suffix 后缀
     */
    void setSuffix(String suffix);

    /**
     * 解压
     *
     * @throws Exception decompressException
     */
    void decompress() throws Exception;

    /**
     * 获取解压文件
     *
     * @return 解压文件
     */
    File getDecompressFile();

    /**
     * 获取解压路径
     *
     * @return 解压路径
     */
    File getDecompressTargetPath();

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

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

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.io.Files1;

import java.io.File;

/**
 * 文件解压器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 21:02
 */
public abstract class BaseFileDecompressor implements FileDecompressor {

    /**
     * 解压文件
     */
    protected File decompressFile;

    /**
     * 解压路径
     */
    protected File decompressTargetPath;

    /**
     * 后缀
     */
    protected String suffix;

    public BaseFileDecompressor(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public void setDecompressFile(String decompressFile) {
        this.setDecompressFile(new File(decompressFile));
    }

    @Override
    public void setDecompressFile(File decompressFile) {
        this.decompressFile = decompressFile;
    }

    @Override
    public void setDecompressTargetPath(String decompressTargetPath) {
        this.setDecompressTargetPath(new File(decompressTargetPath));
    }

    @Override
    public void setDecompressTargetPath(File decompressPath) {
        this.decompressTargetPath = decompressPath;
    }

    @Override
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public void decompress() throws Exception {
        Assert.notNull(decompressFile, "decompress file is null");
        Assert.notNull(decompressTargetPath, "decompress target path is null");
        Assert.isTrue(Files1.isFile(decompressFile), "decompress file is not a file");
        this.doDecompress();
    }

    /**
     * 执行解压
     *
     * @throws Exception Exception
     */
    protected abstract void doDecompress() throws Exception;

    @Override
    public File getDecompressFile() {
        return decompressFile;
    }

    @Override
    public File getDecompressTargetPath() {
        return decompressTargetPath;
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

}

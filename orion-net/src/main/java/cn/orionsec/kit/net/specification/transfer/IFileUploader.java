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
package cn.orionsec.kit.net.specification.transfer;

import java.io.IOException;

/**
 * 文件上传器 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/4/11 18:08
 */
public interface IFileUploader extends IFileTransfer {

    /**
     * 强制覆盖上传 (不检测文件锁定/不检测文件大小/不走断点续传)
     *
     * @param forceOverride 是否覆盖
     */
    void forceOverride(boolean forceOverride);

    /**
     * 文件大小相同则覆盖上传
     *
     * @param fileSizeEqualOverride fileSizeEqualOverride
     */
    void fileSizeEqualOverride(boolean fileSizeEqualOverride);

    /**
     * 获取远程文件大小
     *
     * @return fileSize 文件不存在则返回 -1
     * @throws IOException IOException
     */
    long getRemoteFileLength() throws IOException;

    /**
     * 检查远程文件存在并且大小相同
     *
     * @return 是否相等
     * @throws IOException IOException
     */
    boolean checkRemoteFilePresentSizeEqual() throws IOException;

}

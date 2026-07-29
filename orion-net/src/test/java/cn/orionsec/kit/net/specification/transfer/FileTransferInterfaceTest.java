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

import cn.orionsec.kit.lang.able.SafeCloseable;
import cn.orionsec.kit.lang.support.progress.ByteTransferRateProgress;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

/**
 * 文件传输接口定义测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class FileTransferInterfaceTest {

    @Test
    public void testIFileTransferDefinition() throws NoSuchMethodException {
        assertTrue(IFileTransfer.class.isInterface());
        // 继承关系
        assertTrue(Runnable.class.isAssignableFrom(IFileTransfer.class));
        assertTrue(SafeCloseable.class.isAssignableFrom(IFileTransfer.class));
        // 方法定义
        assertNotNull(IFileTransfer.class.getMethod("abort"));
        Method getProgress = IFileTransfer.class.getMethod("getProgress");
        assertEquals(ByteTransferRateProgress.class, getProgress.getReturnType());
    }

    @Test
    public void testIFileDownloaderDefinition() throws NoSuchMethodException {
        assertTrue(IFileDownloader.class.isInterface());
        assertTrue(IFileTransfer.class.isAssignableFrom(IFileDownloader.class));
        // 方法定义
        assertNotNull(IFileDownloader.class.getMethod("forceOverride", boolean.class));
        assertNotNull(IFileDownloader.class.getMethod("fileSizeEqualOverride", boolean.class));
        assertEquals(long.class, IFileDownloader.class.getMethod("getRemoteFileLength").getReturnType());
        assertEquals(boolean.class, IFileDownloader.class.getMethod("checkRemoteFilePresentSizeEqual").getReturnType());
    }

    @Test
    public void testIFileUploaderDefinition() throws NoSuchMethodException {
        assertTrue(IFileUploader.class.isInterface());
        assertTrue(IFileTransfer.class.isAssignableFrom(IFileUploader.class));
        // 方法定义
        assertNotNull(IFileUploader.class.getMethod("forceOverride", boolean.class));
        assertNotNull(IFileUploader.class.getMethod("fileSizeEqualOverride", boolean.class));
        assertEquals(long.class, IFileUploader.class.getMethod("getRemoteFileLength").getReturnType());
        assertEquals(boolean.class, IFileUploader.class.getMethod("checkRemoteFilePresentSizeEqual").getReturnType());
    }

    @Test
    public void testBaseClassDefinition() {
        // 抽象基类
        assertTrue(Modifier.isAbstract(BaseFileDownloader.class.getModifiers()));
        assertTrue(Modifier.isAbstract(BaseFileUploader.class.getModifiers()));
        // 实现关系
        assertTrue(IFileDownloader.class.isAssignableFrom(BaseFileDownloader.class));
        assertTrue(IFileUploader.class.isAssignableFrom(BaseFileUploader.class));
    }

}

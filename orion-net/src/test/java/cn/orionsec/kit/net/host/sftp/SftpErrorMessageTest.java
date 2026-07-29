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
package cn.orionsec.kit.net.host.sftp;

import org.junit.Assert;
import org.junit.Test;

/**
 * SftpErrorMessage 单元测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class SftpErrorMessageTest {

    @Test
    public void testValues() {
        SftpErrorMessage[] values = SftpErrorMessage.values();
        Assert.assertEquals(2, values.length);
        Assert.assertEquals(SftpErrorMessage.NO_SUCH_FILE, values[0]);
        Assert.assertEquals(SftpErrorMessage.BAD_MESSAGE, values[1]);
    }

    @Test
    public void testGetMessage() {
        Assert.assertEquals("no such file", SftpErrorMessage.NO_SUCH_FILE.getMessage());
        Assert.assertEquals("bad message", SftpErrorMessage.BAD_MESSAGE.getMessage());
    }

    @Test
    public void testIsCause() {
        Assert.assertTrue(SftpErrorMessage.NO_SUCH_FILE.isCause(new Exception("no such file")));
        Assert.assertTrue(SftpErrorMessage.BAD_MESSAGE.isCause(new Exception("bad message")));
    }

    @Test
    public void testIsCauseIgnoreCase() {
        // 消息比较忽略大小写
        Assert.assertTrue(SftpErrorMessage.NO_SUCH_FILE.isCause(new Exception("No Such File")));
        Assert.assertTrue(SftpErrorMessage.NO_SUCH_FILE.isCause(new Exception("2: NO SUCH FILE")));
    }

    @Test
    public void testIsCauseContains() {
        // 包含匹配
        Assert.assertTrue(SftpErrorMessage.NO_SUCH_FILE.isCause(new Exception("sftp error: no such file or directory")));
    }

    @Test
    public void testIsNotCause() {
        Assert.assertFalse(SftpErrorMessage.NO_SUCH_FILE.isCause(new Exception("permission denied")));
        Assert.assertFalse(SftpErrorMessage.BAD_MESSAGE.isCause(new Exception("no such file")));
    }

    @Test
    public void testIsCauseEmptyMessage() {
        // 空消息返回 false
        Assert.assertFalse(SftpErrorMessage.NO_SUCH_FILE.isCause(new Exception()));
        Assert.assertFalse(SftpErrorMessage.NO_SUCH_FILE.isCause(new Exception("")));
    }

}

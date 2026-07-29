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
package cn.orionsec.kit.net.ftp.client;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * FtpMessage 测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class FtpMessageTest {

    @Test
    public void testReplyCodeNotEmpty() {
        assertFalse(FtpMessage.REPLY_CODE.isEmpty());
        assertEquals(34, FtpMessage.REPLY_CODE.size());
    }

    @Test
    public void testCommonReplyCode() {
        assertEquals("服务就绪, 可以执行新用户的请求", FtpMessage.REPLY_CODE.get(220));
        assertEquals("用户已登录, 继续进行", FtpMessage.REPLY_CODE.get(230));
        assertEquals("请求的文件操作正确, 已完成", FtpMessage.REPLY_CODE.get(250));
        assertEquals("进入被动模式", FtpMessage.REPLY_CODE.get(227));
        assertEquals("未登录", FtpMessage.REPLY_CODE.get(530));
    }

    @Test
    public void testUnknownReplyCode() {
        assertNull(FtpMessage.REPLY_CODE.get(0));
        assertNull(FtpMessage.REPLY_CODE.get(999));
    }

}

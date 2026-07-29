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
package cn.orionsec.kit.net.ftp.server;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * FtpUser 测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class FtpUserTest {

    @Test
    public void testNoArgsConstructor() {
        FtpUser user = new FtpUser();
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getHomePath());
        assertEquals(0, user.getMaxIdleTime());
        assertFalse(user.isWritePermission());
        assertEquals(0, user.getMaxUploadRate());
        assertEquals(0, user.getMaxDownloadRate());
    }

    @Test
    public void testTwoArgsConstructor() {
        FtpUser user = new FtpUser("orion", "123456");
        assertEquals("orion", user.getUsername());
        assertEquals("123456", user.getPassword());
        assertEquals("/home/orion", user.getHomePath());
    }

    @Test
    public void testThreeArgsConstructor() {
        FtpUser user = new FtpUser("orion", "123456", "/data/ftp");
        assertEquals("orion", user.getUsername());
        assertEquals("123456", user.getPassword());
        assertEquals("/data/ftp", user.getHomePath());
    }

    @Test
    public void testSetter() {
        FtpUser user = new FtpUser();
        user.setUsername("user1");
        user.setPassword("pwd1");
        user.setHomePath("/home/user1");
        user.setMaxIdleTime(300);
        user.setWritePermission(true);
        user.setMaxUploadRate(1024);
        user.setMaxDownloadRate(2048);

        assertEquals("user1", user.getUsername());
        assertEquals("pwd1", user.getPassword());
        assertEquals("/home/user1", user.getHomePath());
        assertEquals(300, user.getMaxIdleTime());
        assertTrue(user.isWritePermission());
        assertEquals(1024, user.getMaxUploadRate());
        assertEquals(2048, user.getMaxDownloadRate());
    }

    @Test
    public void testToString() {
        assertEquals("orion", new FtpUser("orion", "123456").toString());
    }

}

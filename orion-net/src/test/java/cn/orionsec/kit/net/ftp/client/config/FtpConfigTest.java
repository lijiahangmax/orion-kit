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
package cn.orionsec.kit.net.ftp.client.config;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Charsets;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * FtpConfig 测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class FtpConfigTest {

    @Test
    public void testDefaultValue() {
        FtpConfig config = new FtpConfig("127.0.0.1");
        assertEquals("127.0.0.1", config.getHost());
        assertEquals(21, config.getPort());
        assertNull(config.getUsername());
        assertNull(config.getPassword());
        assertEquals(Const.SLASH, config.getRemoteRootDir());
        assertEquals(Const.UTF_8, config.getRemoteContentCharset());
        assertEquals(Const.UTF_8, config.getLocalContentCharset());
        assertEquals(Charsets.UTF_8, config.getRemoteFileNameCharset());
        assertEquals(Charsets.UTF_8, config.getLocalFileNameCharset());
        assertFalse(config.isShowHidden());
        assertEquals(Const.BUFFER_KB_8, config.getBuffSize());
        assertFalse(config.isPassiveMode());
        assertEquals(Const.MS_S_60, config.getDateTimeout());
        assertEquals(Const.MS_S_5, config.getConnTimeout());
    }

    @Test
    public void testCustomPort() {
        FtpConfig config = new FtpConfig("192.168.1.1", 2121);
        assertEquals("192.168.1.1", config.getHost());
        assertEquals(2121, config.getPort());
    }

    @Test
    public void testChainConfig() {
        FtpConfig config = new FtpConfig("127.0.0.1", 2121)
                .auth("user", "password")
                .rootDir("/base/");
        assertEquals("user", config.getUsername());
        assertEquals("password", config.getPassword());
        assertEquals("/base/", config.getRemoteRootDir());
    }

    @Test
    public void testSetter() {
        FtpConfig config = new FtpConfig("127.0.0.1");
        config.setHost("10.0.0.1");
        config.setPort(2222);
        config.setRemoteRootDir("/data/");
        config.setRemoteContentCharset(Const.GBK);
        config.setLocalContentCharset(Const.GBK);
        config.setRemoteFileNameCharset("GBK");
        config.setLocalFileNameCharset("GBK");
        config.setShowHidden(true);
        config.setBuffSize(1024);
        config.setPassiveMode(true);
        config.setDateTimeout(2000);
        config.setConnTimeout(3000);

        assertEquals("10.0.0.1", config.getHost());
        assertEquals(2222, config.getPort());
        assertEquals("/data/", config.getRemoteRootDir());
        assertEquals(Const.GBK, config.getRemoteContentCharset());
        assertEquals(Const.GBK, config.getLocalContentCharset());
        assertEquals(Charsets.GBK, config.getRemoteFileNameCharset());
        assertEquals(Charsets.GBK, config.getLocalFileNameCharset());
        assertTrue(config.isShowHidden());
        assertEquals(1024, config.getBuffSize());
        assertTrue(config.isPassiveMode());
        assertEquals(2000, config.getDateTimeout());
        assertEquals(3000, config.getConnTimeout());
    }

    @Test
    public void testCharsetSetter() {
        FtpConfig config = new FtpConfig("127.0.0.1");
        config.setRemoteFileNameCharset(Charsets.ISO_8859_1);
        config.setLocalFileNameCharset(Charsets.ISO_8859_1);
        assertEquals(Charsets.ISO_8859_1, config.getRemoteFileNameCharset());
        assertEquals(Charsets.ISO_8859_1, config.getLocalFileNameCharset());
    }

    @Test
    public void testToString() {
        FtpConfig config = new FtpConfig("127.0.0.1").auth("user", "password");
        assertNotNull(config.toString());
    }

}

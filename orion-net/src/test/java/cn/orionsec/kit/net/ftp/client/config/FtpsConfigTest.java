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

import cn.orionsec.kit.lang.constant.StandardTlsVersion;
import org.junit.Test;

import javax.net.ssl.SSLSocketFactory;

import static org.junit.Assert.*;

/**
 * FtpsConfig 测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class FtpsConfigTest {

    @Test
    public void testDefaultValue() {
        FtpsConfig config = new FtpsConfig("127.0.0.1");
        assertEquals("127.0.0.1", config.getHost());
        assertEquals(21, config.getPort());
        assertEquals(StandardTlsVersion.TLS, config.getProtocol());
        assertFalse(config.isImplicit());
        assertEquals("P", config.getProtect());
        assertNull(config.getSocketFactory());
    }

    @Test
    public void testCustomPort() {
        FtpsConfig config = new FtpsConfig("127.0.0.1", 990);
        assertEquals(990, config.getPort());
    }

    @Test
    public void testSetter() {
        FtpsConfig config = new FtpsConfig("127.0.0.1");
        config.setProtocol(StandardTlsVersion.TLS_1_2);
        config.setImplicit(true);
        config.setProtect("C");
        SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        config.setSocketFactory(socketFactory);

        assertEquals(StandardTlsVersion.TLS_1_2, config.getProtocol());
        assertTrue(config.isImplicit());
        assertEquals("C", config.getProtect());
        assertSame(socketFactory, config.getSocketFactory());
    }

    @Test
    public void testExtendsFtpConfig() {
        // 继承 FtpConfig 的链式配置
        FtpsConfig config = new FtpsConfig("127.0.0.1", 990);
        config.auth("user", "password").rootDir("/base/");
        assertEquals("user", config.getUsername());
        assertEquals("password", config.getPassword());
        assertEquals("/base/", config.getRemoteRootDir());
        assertTrue(config instanceof FtpConfig);
    }

}

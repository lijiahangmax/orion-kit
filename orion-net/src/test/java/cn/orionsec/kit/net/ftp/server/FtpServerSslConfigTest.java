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

import cn.orionsec.kit.lang.utils.Systems;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * FtpServerSslConfig 测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class FtpServerSslConfigTest {

    @Test
    public void testStringConstructor() {
        FtpServerSslConfig config = new FtpServerSslConfig(Systems.HOME_DIR + "/orion-kit-test/key/store.jks", "123456");
        assertEquals(new File(Systems.HOME_DIR + "/orion-kit-test/key/store.jks"), config.getKeyStoreFile());
        assertEquals("123456", config.getKeyStorePassword());
        assertNull(config.getSslProtocol());
        assertNull(config.getKeyStoreAlgorithm());
        assertNull(config.getKeyAlias());
    }

    @Test
    public void testFileConstructor() {
        File file = new File(Systems.HOME_DIR + "/orion-kit-test/key/store.jks");
        FtpServerSslConfig config = new FtpServerSslConfig(file, "123456");
        assertSame(file, config.getKeyStoreFile());
        assertEquals("123456", config.getKeyStorePassword());
    }

    @Test
    public void testSetter() {
        FtpServerSslConfig config = new FtpServerSslConfig(Systems.HOME_DIR + "/orion-kit-test/key/store.jks", "123456");
        config.setSslProtocol("TLSv1.2");
        config.setKeyStorePassword("654321");
        config.setKeyStoreAlgorithm("SunX509");
        config.setKeyAlias("orion");
        config.setKeyStoreFile(Systems.HOME_DIR + "/orion-kit-test/key/other.jks");

        assertEquals("TLSv1.2", config.getSslProtocol());
        assertEquals("654321", config.getKeyStorePassword());
        assertEquals("SunX509", config.getKeyStoreAlgorithm());
        assertEquals("orion", config.getKeyAlias());
        assertEquals(new File(Systems.HOME_DIR + "/orion-kit-test/key/other.jks"), config.getKeyStoreFile());

        File file = new File(Systems.HOME_DIR + "/orion-kit-test/key/file.jks");
        config.setKeyStoreFile(file);
        assertSame(file, config.getKeyStoreFile());
    }

}

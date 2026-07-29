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

import cn.orionsec.kit.lang.constant.Const;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * FtpServerConfig 测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class FtpServerConfigTest {

    @Test
    public void testDefaultValue() {
        FtpServerConfig config = new FtpServerConfig();
        assertEquals(10, config.getMaxLogin());
        assertFalse(config.isAnonymousLogin());
        assertEquals(0, config.getMaxAnonymousLogin());
        assertEquals(3, config.getMaxLoginFailures());
        assertEquals(Const.MS_S_1, config.getLoginFailureDelay());
        assertEquals(5, config.getMaxThreads());
    }

    @Test
    public void testSetter() {
        FtpServerConfig config = new FtpServerConfig();
        config.setMaxLogin(20);
        config.setAnonymousLogin(true);
        config.setMaxAnonymousLogin(5);
        config.setMaxLoginFailures(10);
        config.setLoginFailureDelay(500);
        config.setMaxThreads(8);

        assertEquals(20, config.getMaxLogin());
        assertTrue(config.isAnonymousLogin());
        assertEquals(5, config.getMaxAnonymousLogin());
        assertEquals(10, config.getMaxLoginFailures());
        assertEquals(500, config.getLoginFailureDelay());
        assertEquals(8, config.getMaxThreads());
    }

}

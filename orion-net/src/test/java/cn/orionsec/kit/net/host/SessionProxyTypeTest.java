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
package cn.orionsec.kit.net.host;

import org.junit.Assert;
import org.junit.Test;

/**
 * SessionProxyType 单元测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class SessionProxyTypeTest {

    @Test
    public void testValues() {
        SessionProxyType[] values = SessionProxyType.values();
        Assert.assertEquals(3, values.length);
        Assert.assertEquals(SessionProxyType.HTTP, values[0]);
        Assert.assertEquals(SessionProxyType.SOCKS4, values[1]);
        Assert.assertEquals(SessionProxyType.SOCKS5, values[2]);
    }

    @Test
    public void testOf() {
        Assert.assertEquals(SessionProxyType.HTTP, SessionProxyType.of("HTTP"));
        Assert.assertEquals(SessionProxyType.SOCKS4, SessionProxyType.of("SOCKS4"));
        Assert.assertEquals(SessionProxyType.SOCKS5, SessionProxyType.of("SOCKS5"));
    }

    @Test
    public void testOfNull() {
        Assert.assertNull(SessionProxyType.of(null));
    }

    @Test
    public void testOfUnknown() {
        Assert.assertNull(SessionProxyType.of("UNKNOWN"));
        // 大小写敏感
        Assert.assertNull(SessionProxyType.of("http"));
        Assert.assertNull(SessionProxyType.of(""));
    }

    @Test
    public void testValueOf() {
        Assert.assertEquals(SessionProxyType.HTTP, SessionProxyType.valueOf("HTTP"));
        Assert.assertEquals(SessionProxyType.SOCKS5, SessionProxyType.valueOf("SOCKS5"));
    }

}

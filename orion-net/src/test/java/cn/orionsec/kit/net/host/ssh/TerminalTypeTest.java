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
package cn.orionsec.kit.net.host.ssh;

import org.junit.Assert;
import org.junit.Test;

/**
 * TerminalType 单元测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class TerminalTypeTest {

    @Test
    public void testGetType() {
        Assert.assertEquals("xterm", TerminalType.XTERM.getType());
        Assert.assertEquals("xterm-16color", TerminalType.XTERM_16_COLOR.getType());
        Assert.assertEquals("xterm-256color", TerminalType.XTERM_256_COLOR.getType());
        Assert.assertEquals("bash", TerminalType.BASH.getType());
        Assert.assertEquals("vt100", TerminalType.VT_100.getType());
        Assert.assertEquals("vt102", TerminalType.VT_102.getType());
        Assert.assertEquals("vt220", TerminalType.VT_220.getType());
        Assert.assertEquals("vt320", TerminalType.VT_320.getType());
        Assert.assertEquals("linux", TerminalType.LINUX.getType());
        Assert.assertEquals("ansi", TerminalType.ANSI.getType());
        Assert.assertEquals("dumb", TerminalType.DUMB.getType());
        Assert.assertEquals("scoansi", TerminalType.SCO_ANSI.getType());
    }

    @Test
    public void testValues() {
        Assert.assertEquals(12, TerminalType.values().length);
    }

    @Test
    public void testOfNullReturnXterm() {
        Assert.assertEquals(TerminalType.XTERM, TerminalType.of(null));
    }

    @Test
    public void testOfUnknownReturnXterm() {
        Assert.assertEquals(TerminalType.XTERM, TerminalType.of("unknown-type"));
        Assert.assertEquals(TerminalType.XTERM, TerminalType.of(""));
        // 大小写敏感
        Assert.assertEquals(TerminalType.XTERM, TerminalType.of("XTERM"));
    }

    @Test
    public void testOfRoundTrip() {
        for (TerminalType type : TerminalType.values()) {
            Assert.assertEquals(type, TerminalType.of(type.getType()));
        }
    }

}

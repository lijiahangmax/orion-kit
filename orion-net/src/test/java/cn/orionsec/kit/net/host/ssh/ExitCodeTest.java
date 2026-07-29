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
 * ExitCode 单元测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class ExitCodeTest {

    @Test
    public void testGetCode() {
        Assert.assertEquals(Integer.valueOf(0), ExitCode.SUCCESS.getCode());
        Assert.assertEquals(Integer.valueOf(1), ExitCode.ERROR.getCode());
        Assert.assertNull(ExitCode.NULL.getCode());
    }

    @Test
    public void testValues() {
        Assert.assertEquals(3, ExitCode.values().length);
        Assert.assertEquals(ExitCode.SUCCESS, ExitCode.valueOf("SUCCESS"));
        Assert.assertEquals(ExitCode.ERROR, ExitCode.valueOf("ERROR"));
        Assert.assertEquals(ExitCode.NULL, ExitCode.valueOf("NULL"));
    }

    @Test
    public void testIsSuccess() {
        Assert.assertTrue(ExitCode.isSuccess(0));
        Assert.assertFalse(ExitCode.isSuccess(1));
        Assert.assertFalse(ExitCode.isSuccess(-1));
        Assert.assertFalse(ExitCode.isSuccess(null));
    }

    @Test
    public void testIsFailed() {
        Assert.assertFalse(ExitCode.isFailed(0));
        Assert.assertTrue(ExitCode.isFailed(1));
        Assert.assertTrue(ExitCode.isFailed(-1));
        Assert.assertTrue(ExitCode.isFailed(null));
    }

}

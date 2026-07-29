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
 * SessionLogger 单元测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class SessionLoggerTest {

    @Test
    public void testValues() {
        SessionLogger[] values = SessionLogger.values();
        Assert.assertEquals(5, values.length);
        Assert.assertEquals(SessionLogger.DEBUG, values[0]);
        Assert.assertEquals(SessionLogger.INFO, values[1]);
        Assert.assertEquals(SessionLogger.WARN, values[2]);
        Assert.assertEquals(SessionLogger.ERROR, values[3]);
        Assert.assertEquals(SessionLogger.FATAL, values[4]);
    }

    @Test
    public void testLevel() {
        Assert.assertEquals(com.jcraft.jsch.Logger.DEBUG, SessionLogger.DEBUG.getLevel());
        Assert.assertEquals(com.jcraft.jsch.Logger.INFO, SessionLogger.INFO.getLevel());
        Assert.assertEquals(com.jcraft.jsch.Logger.WARN, SessionLogger.WARN.getLevel());
        Assert.assertEquals(com.jcraft.jsch.Logger.ERROR, SessionLogger.ERROR.getLevel());
        Assert.assertEquals(com.jcraft.jsch.Logger.FATAL, SessionLogger.FATAL.getLevel());
        // jsch 定义 0 ~ 4
        Assert.assertEquals(0, SessionLogger.DEBUG.getLevel());
        Assert.assertEquals(1, SessionLogger.INFO.getLevel());
        Assert.assertEquals(2, SessionLogger.WARN.getLevel());
        Assert.assertEquals(3, SessionLogger.ERROR.getLevel());
        Assert.assertEquals(4, SessionLogger.FATAL.getLevel());
    }

    @Test
    public void testGetLogger() {
        Assert.assertEquals(SessionLogger.DEBUG, SessionLogger.getLogger(0));
        Assert.assertEquals(SessionLogger.INFO, SessionLogger.getLogger(1));
        Assert.assertEquals(SessionLogger.WARN, SessionLogger.getLogger(2));
        Assert.assertEquals(SessionLogger.ERROR, SessionLogger.getLogger(3));
        Assert.assertEquals(SessionLogger.FATAL, SessionLogger.getLogger(4));
    }

    @Test(expected = RuntimeException.class)
    public void testGetLoggerUnknownLevel() {
        SessionLogger.getLogger(99);
    }

    @Test
    public void testValueOf() {
        Assert.assertEquals(SessionLogger.DEBUG, SessionLogger.valueOf("DEBUG"));
        Assert.assertEquals(SessionLogger.FATAL, SessionLogger.valueOf("FATAL"));
    }

    @Test
    public void testLog() {
        // 各级别日志输出不抛出异常
        SessionLogger.log(com.jcraft.jsch.Logger.DEBUG, "debug message");
        SessionLogger.log(com.jcraft.jsch.Logger.INFO, "info message");
        SessionLogger.log(com.jcraft.jsch.Logger.WARN, "warn message");
        SessionLogger.log(com.jcraft.jsch.Logger.ERROR, "error message");
        SessionLogger.log(com.jcraft.jsch.Logger.FATAL, "fatal message");
        // 未知级别不抛出异常
        SessionLogger.log(99, "unknown message");
    }

    @Test
    public void testSetTags() {
        try {
            SessionLogger.setDebugTag("[DEBUG] {}");
            SessionLogger.setInfoTag("[INFO] {}");
            SessionLogger.setWarnTag("[WARN] {}");
            SessionLogger.setErrorTag("[ERROR] {}");
            SessionLogger.setFatalTag("[FATAL] {}");
            SessionLogger.log(com.jcraft.jsch.Logger.INFO, "tagged message");
        } finally {
            // 还原默认 tag
            SessionLogger.setDebugTag("{}");
            SessionLogger.setInfoTag("{}");
            SessionLogger.setWarnTag("{}");
            SessionLogger.setErrorTag("{}");
            SessionLogger.setFatalTag("{}");
        }
    }

}

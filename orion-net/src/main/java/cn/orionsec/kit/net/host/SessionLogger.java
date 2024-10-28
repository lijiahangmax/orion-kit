/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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

import cn.orionsec.kit.lang.utils.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session logger
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/7 0:13
 */
public enum SessionLogger {

    /**
     * DEBUG 0
     */
    DEBUG(com.jcraft.jsch.Logger.DEBUG),

    /**
     * INFO 1
     */
    INFO(com.jcraft.jsch.Logger.INFO),

    /**
     * WARN 2
     */
    WARN(com.jcraft.jsch.Logger.WARN),

    /**
     * ERROR 3
     */
    ERROR(com.jcraft.jsch.Logger.ERROR),

    /**
     * FATAL 4
     */
    FATAL(com.jcraft.jsch.Logger.FATAL);

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionLogger.class);

    private static String debugTag = "{}";

    private static String infoTag = "{}";

    private static String warnTag = "{}";

    private static String errorTag = "{}";

    private static String fatalTag = "{}";

    private final int level;

    SessionLogger(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static SessionLogger getLogger(int level) {
        for (SessionLogger value : values()) {
            if (value.level == level) {
                return value;
            }
        }
        throw Exceptions.runtime();
    }

    protected static void log(int level, String msg) {
        switch (level) {
            case com.jcraft.jsch.Logger.DEBUG:
                LOGGER.debug(debugTag, msg);
                return;
            case com.jcraft.jsch.Logger.INFO:
                LOGGER.info(infoTag, msg);
                return;
            case com.jcraft.jsch.Logger.WARN:
                LOGGER.warn(warnTag, msg);
                return;
            case com.jcraft.jsch.Logger.ERROR:
                LOGGER.error(errorTag, msg);
                return;
            case com.jcraft.jsch.Logger.FATAL:
                LOGGER.error(fatalTag, msg);
                return;
            default:
        }
    }

    public static void setDebugTag(String debugTag) {
        SessionLogger.debugTag = debugTag;
    }

    public static void setInfoTag(String infoTag) {
        SessionLogger.infoTag = infoTag;
    }

    public static void setWarnTag(String warnTag) {
        SessionLogger.warnTag = warnTag;
    }

    public static void setErrorTag(String errorTag) {
        SessionLogger.errorTag = errorTag;
    }

    public static void setFatalTag(String fatalTag) {
        SessionLogger.fatalTag = fatalTag;
    }

}

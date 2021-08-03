package com.orion.remote.channel;

import com.orion.utils.Exceptions;
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

package com.orion.lang;

import java.io.File;

/**
 * 系统常量
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/1/16 16:56
 */
public class SystemConst {

    private SystemConst() {
    }

    /**
     * 行分隔符 windows: \r\n unix: \n
     */
    public static final String LINE_SEPARATOR;

    /**
     * 路径分隔符
     */
    public static final String FILE_SEPARATOR;

    /**
     * 是否是unix环境下
     */
    public static final boolean BE_UNIX;

    /**
     * 是否是windows环境下
     */
    public static final boolean BE_WINDOWS;

    /**
     * 是否是Android环境下
     */
    public static final boolean BE_ANDROID;

    /**
     * 当前用户名
     */
    public static final String USER_NAME;

    /**
     * 文件编码
     */
    public static final String FILE_ENCODING;

    /**
     * 用户目录
     */
    public static final String HOME_DIR;

    /**
     * 当前文件目录
     */
    public static final String THIS_DIR;

    /**
     * IO 临时目录
     */
    public static final String TEMP_DIR;

    /**
     * 系统版本
     */
    public static final String OS_NAME;

    /**
     * 主机名称
     */
    public static final String HOST_NAME;

    static {
        LINE_SEPARATOR = System.getProperty("line.separator", "\n");
        FILE_SEPARATOR = File.separator;
        BE_UNIX = "/".equals(File.separator);
        BE_WINDOWS = "\\".equals(File.separator);
        USER_NAME = System.getProperty("user.name", "unknown");
        FILE_ENCODING = System.getProperty("file.encoding", "UTF-8");
        HOME_DIR = System.getProperty("user.home", "/");
        THIS_DIR = System.getProperty("user.dir", "/");
        TEMP_DIR = System.getProperty("java.io.tmpdir", "/");
        OS_NAME = System.getProperty("os.name", "unknown");
        if (BE_WINDOWS) {
            HOST_NAME = System.getenv("COMPUTERNAME");
        } else {
            HOST_NAME = System.getenv("HOSTNAME");
        }
        boolean beAndroid = true;
        try {
            Class.forName("android.util.Log");
        } catch (Exception e) {
            beAndroid = false;
        }
        BE_ANDROID = beAndroid;
    }

    /**
     * 获取环境变量
     *
     * @param key key
     * @return ignore
     */
    public static String getEnv(String key) {
        return System.getenv(key);
    }

    /**
     * 获取系统属性
     *
     * @param key key
     * @return value
     */
    public static String getProperty(String key) {
        return System.getProperty(key);
    }

    /**
     * 获取系统属性
     *
     * @param key key
     * @param def 默认值
     * @return value
     */
    public static String getProperty(String key, String def) {
        return System.getProperty(key, def);
    }

}

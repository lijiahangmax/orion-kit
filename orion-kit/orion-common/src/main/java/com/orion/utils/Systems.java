package com.orion.utils;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 系统工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/1/16 16:56
 */
public class Systems {

    private Systems() {
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

    /**
     * 进程PID
     */
    public static final int PID;

    /**
     * JDK版本
     */
    public static final String SPEC_VERSION;

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
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String processName = runtime.getName();
        PID = Integer.parseInt(processName.substring(0, processName.indexOf('@')));
        SPEC_VERSION = runtime.getSpecVersion();
    }

    /**
     * 添加一个系统关闭的钩子 可以取消
     *
     * @param thread hook
     */
    public static void addShutdownHook(Thread thread) {
        Runtime.getRuntime().addShutdownHook(thread);
    }

    /**
     * 添加一个系统关闭的钩子 不可取消
     *
     * @param runnable hook
     */
    public static void addShutdownHook(Runnable runnable) {
        Runtime.getRuntime().addShutdownHook(new Thread(runnable));
    }

    /**
     * 移除一个系统关闭的钩子
     *
     * @param thread hook
     */
    public static void removeShutdownHook(Thread thread) {
        Runtime.getRuntime().removeShutdownHook(thread);
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
     * 获取环境变量
     *
     * @return ignore
     */
    public static Map<String, String> getEnv() {
        return System.getenv();
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

    /**
     * 获取系统属性
     *
     * @return value
     */
    public static Properties getProperties() {
        return System.getProperties();
    }

    /**
     * 设置系统属性
     *
     * @param key   key
     * @param value value
     * @return value
     */
    public static String setProperty(String key, String value) {
        return System.setProperty(key, value);
    }

    /**
     * 设置系统属性
     *
     * @param map 属性
     */
    public static void setProperty(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.setProperty(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 移除系统属性
     *
     * @param keys keys
     * @return keys values
     */
    public static Map<String, String> clearProperty(List<String> keys) {
        Map<String, String> map = new HashMap<>();
        for (String key : keys) {
            String value = System.clearProperty(key);
            map.put(key, value);
        }
        return map;
    }

}

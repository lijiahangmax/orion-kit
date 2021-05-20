package com.orion.utils;

import com.orion.constant.Const;
import com.orion.utils.math.Numbers;
import com.orion.utils.random.Randoms;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.NetworkInterface;
import java.util.*;

/**
 * 系统工具类
 *
 * @author Jiahang Li
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
    public static final String USER_DIR;

    /**
     * IO 临时目录
     */
    public static final String TEMP_DIR;

    /**
     * 系统名称
     */
    public static final String OS_NAME;

    /**
     * 系统版本
     */
    public static final String OS_VERSION;

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
    public static final String JAVA_SPEC_VERSION;

    /**
     * Java Home
     */
    public static final String JAVA_HOME;

    /**
     * 处理器数量
     */
    public static final int PROCESS_NUM;

    static {
        LINE_SEPARATOR = getProperty("line.separator", Const.LF);
        FILE_SEPARATOR = File.separator;
        BE_UNIX = Const.SLASH.equals(File.separator);
        BE_WINDOWS = Const.BACKSLASH.equals(File.separator);
        USER_NAME = getProperty("user.name", Const.UNKNOWN);
        FILE_ENCODING = getProperty("file.encoding", Const.UTF_8);
        HOME_DIR = getProperty("user.home", Const.ROOT);
        USER_DIR = getProperty("user.dir", Const.ROOT);
        TEMP_DIR = getProperty("java.io.tmpdir", Const.ROOT);
        OS_NAME = getProperty("os.name", Const.UNKNOWN);
        OS_VERSION = getProperty("os.version", Const.UNKNOWN);
        JAVA_HOME = getProperty("java.home", Const.ROOT);
        if (BE_WINDOWS) {
            HOST_NAME = getEnv("COMPUTERNAME");
        } else {
            HOST_NAME = getEnv("HOSTNAME");
        }
        boolean beAndroid = true;
        try {
            Class.forName("android.util.Log");
        } catch (Exception e) {
            beAndroid = false;
        }
        BE_ANDROID = beAndroid;
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        String processName = runtimeBean.getName();
        int si = processName.indexOf('@');
        if (si != -1) {
            processName = processName.substring(0, si);
        }
        PID = Integer.parseInt(processName);
        JAVA_SPEC_VERSION = runtimeBean.getSpecVersion();
        Runtime runtime = Runtime.getRuntime();
        PROCESS_NUM = runtime.availableProcessors();
    }

    /**
     * 获取机器码
     *
     * @param startRange 开始区间
     * @param endRange   结束区间
     * @return 机器码
     */
    public static int getMachineCode(int startRange, int endRange) {
        return Numbers.getRangeNum(getMachineCode(), startRange, endRange);
    }

    /**
     * 获取机器码
     *
     * @return 机器码
     */
    public static int getMachineCode() {
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                sb.append(ni.toString());
            }
            return sb.toString().hashCode() << 16;
        } catch (Throwable e) {
            return (Randoms.randomInt()) << 16;
        }
    }

    /**
     * 获取进程码
     *
     * @param startRange 开始区间
     * @param endRange   结束区间
     * @return 进程码
     */
    public static int getProcessCode(int startRange, int endRange) {
        return Numbers.getRangeNum(getProcessCode(), startRange, endRange);
    }

    /**
     * 获取进程码
     *
     * @return 进程码
     */
    public static int getProcessCode() {
        int loaderId = System.identityHashCode(Systems.class.getClassLoader());
        String processLoaderId = Integer.toHexString(PID) + Integer.toHexString(loaderId);
        return processLoaderId.hashCode() & 0xFFFF;
    }

    /**
     * 退出进程
     *
     * @param code exit code
     */
    public static void exit(int code) {
        System.exit(code);
    }

    /**
     * 强制 关闭进程
     *
     * @param status exit code
     */
    public static void halt(int status) {
        Runtime.getRuntime().halt(status);
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
     * @param key key
     * @param def def
     * @return ignore
     */
    public static String getEnv(String key, String def) {
        return Objects1.def(System.getenv(key), def);
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

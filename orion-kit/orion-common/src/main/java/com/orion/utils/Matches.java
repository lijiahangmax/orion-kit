package com.orion.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则相关工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/1/5 16:13
 */
public class Matches {

    private Matches() {
    }

    private static final Map<String, Pattern> MAP = new ConcurrentHashMap<>();

    /**
     * 空白行
     */
    public static final String SPACE_LINE = "\\n\\s*\\r";

    /**
     * 首尾空格
     */
    public static final String SPACE_POINT = "^\\s*|\\s*$";

    /**
     * 手机号
     */
    public static final String PHONE = "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$";

    /**
     * 邮箱正则
     */
    public static final String EMAIL = "^[A-Za-z0-9]+([_\\.][A-Za-z0-9]+)*@([A-Za-z0-9\\-]+\\.)+[A-Za-z]{2,6}$";

    /**
     * http正则
     */
    public static final String HTTP = "^(http|https):\\/\\/([\\w.]+\\/?)\\S*$";

    /**
     * http正则
     */
    public static final String INTEGER = "^[-\\+]?[\\d]*$";

    /**
     * http正则
     */
    public static final String DOUBLE = "^[-\\+]?\\d*[.]\\d+$";

    /**
     * IPV4正则
     */
    public static final String IPV4 = "^(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";

    /**
     * IPV6正则
     */
    public static final String IPV6 = "^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:)|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}(:[0-9A-Fa-f]{1,4}){1,2})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){1,3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){1,4})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){1,5})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){1,6})|(:(:[0-9A-Fa-f]{1,4}){1,7})|(([0-9A-Fa-f]{1,4}:){6}(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){5}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){0,4}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(:(:[0-9A-Fa-f]{1,4}){0,5}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}))$";

    /**
     * MD5 正则
     */
    public static final String MD5 = "^[a-f0-9]{32}|[A-F0-9]{32}$";

    /**
     * windows文件路径 正则
     */
    public static final String WINDOWS_PATH = "^[A-z]:\\\\([^|><?*\":/]*\\\\)*([^|><?*\":/]*)?$|^[A-z]:/([^|><?*\":/]*/)*([^|><?*\":/]*)?$";

    /**
     * linux文件路径 正则
     */
    public static final String LINUX_PATH = "^/([^|><?*\":/]*/)*([^|><?*\":/]*)?$";

    static {
        MAP.put(SPACE_LINE, Pattern.compile(SPACE_LINE));
        MAP.put(SPACE_POINT, Pattern.compile(SPACE_POINT));
        MAP.put(PHONE, Pattern.compile(PHONE));
        MAP.put(EMAIL, Pattern.compile(EMAIL));
        MAP.put(HTTP, Pattern.compile(HTTP));
        MAP.put(INTEGER, Pattern.compile(INTEGER));
        MAP.put(DOUBLE, Pattern.compile(DOUBLE));
        MAP.put(IPV4, Pattern.compile(IPV4));
        MAP.put(IPV6, Pattern.compile(IPV6));
        MAP.put(MD5, Pattern.compile(MD5));
        MAP.put(WINDOWS_PATH, Pattern.compile(WINDOWS_PATH));
        MAP.put(LINUX_PATH, Pattern.compile(LINUX_PATH));
    }

    /**
     * 获取正则对象
     *
     * @param pattern 表达式
     * @return 正则
     */
    public static Pattern getPattern(String pattern) {
        Pattern p = MAP.get(pattern);
        if (p == null) {
            p = Pattern.compile(pattern);
            MAP.put(pattern, p);
        }
        return p;
    }

    /**
     * 是否匹配
     *
     * @param s       字符
     * @param pattern 模式
     * @return true 匹配
     */
    public static boolean test(String s, String pattern) {
        return getPattern(pattern).matcher(s).matches();
    }

    /**
     * 是否匹配
     *
     * @param s       字符
     * @param pattern 模式
     * @return true 匹配
     */
    public static boolean test(String s, Pattern pattern) {
        return pattern.matcher(s).matches();
    }

    /**
     * 匹配字符出现次数
     *
     * @param s    源数据
     * @param find 匹配数据
     * @return 次数
     */
    public static int findNum(String s, String find) {
        int count = 0;
        Pattern p = Pattern.compile(find);
        Matcher m = p.matcher(s);
        while (m.find()) {
            count++;
        }
        return count;
    }

    /**
     * 匹配是否为浮点数
     *
     * @param str 待匹配的字符
     * @return true浮点数
     */
    public static boolean isDouble(String str) {
        return getPattern(DOUBLE).matcher(str).matches();
    }

    /**
     * 匹配是否为整数
     *
     * @param str 待匹配的字符
     * @return true整数
     */
    public static boolean isInteger(String str) {
        return getPattern(INTEGER).matcher(str).matches();
    }

    /**
     * 匹配是否为IPV4
     *
     * @param ip ip
     * @return true IPV4
     */
    public static boolean isIpv4(String ip) {
        return getPattern(IPV4).matcher(ip).matches();
    }

    /**
     * 匹配是否为IPV6
     *
     * @param ip ip
     * @return true IPV6
     */
    public static boolean isIpv6(String ip) {
        return getPattern(IPV6).matcher(ip).matches();
    }

    /**
     * 匹配是否为手机号
     *
     * @param phone 手机号
     * @return true 手机号
     */
    public static boolean isPhone(String phone) {
        return getPattern(PHONE).matcher(phone).matches();
    }

    /**
     * 匹配是否为邮箱
     *
     * @param email 邮箱
     * @return true 邮箱
     */
    public static boolean isEmail(String email) {
        return getPattern(EMAIL).matcher(email).matches();
    }

    /**
     * 匹配是否为http url
     *
     * @param http url
     * @return true HTTP url
     */
    public static boolean isHttpUrl(String http) {
        return getPattern(HTTP).matcher(http).matches();
    }

    /**
     * 匹配是否为 MD5
     *
     * @param s s
     * @return true MD5
     */
    public static boolean isMD5(String s) {
        return getPattern(MD5).matcher(s).matches();
    }

    /**
     * 匹配是否为 windows文件路径
     *
     * @param path 路径
     * @return true windows文件路径
     */
    public static boolean isWindowsPath(String path) {
        return getPattern(WINDOWS_PATH).matcher(path).matches();
    }

    /**
     * 匹配是否为 linux文件路径
     *
     * @param path 路径
     * @return true linux文件路径
     */
    public static boolean isLinuxPath(String path) {
        return getPattern(LINUX_PATH).matcher(path).matches();
    }

}

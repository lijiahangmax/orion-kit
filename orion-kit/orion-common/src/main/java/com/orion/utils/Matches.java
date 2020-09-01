package com.orion.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则相关工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/1/5 16:13
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
    public static final String EMAIL = "^[A-Za-z0-9]+([_.][A-Za-z0-9]+)*@([A-Za-z0-9\\-]+\\.)+[A-Za-z]{2,6}$";

    /**
     * http正则
     */
    public static final String HTTP = "^(http|https)://([\\w.]+/?)\\S*$";

    /**
     * uri正则
     */
    public static final String URI = "^[a-zA-z]+://[\\S]*$";

    /**
     * integer正则
     */
    public static final String INTEGER = "^[-+]?[\\d]*$";

    /**
     * double正则
     */
    public static final String DOUBLE = "^[-+]?\\d*[.]\\d+$";

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
        MAP.put(URI, Pattern.compile(URI));
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
     * 获取正则对象 忽略首尾 ^ $
     * 用于字符串提取
     *
     * @param pattern 表达式
     * @return 正则
     */
    public static Pattern getPatternExt(String pattern) {
        if (pattern.startsWith("^")) {
            pattern = pattern.substring(1, pattern.length());
        }
        if (pattern.endsWith("$")) {
            pattern = pattern.substring(0, pattern.length() - 1);
        }
        Pattern p = MAP.get(pattern);
        if (p == null) {
            p = Pattern.compile(pattern);
            MAP.put(pattern, p);
        }
        return p;
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

    // --------------- test ---------------

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
    public static boolean isHttp(String http) {
        return getPattern(HTTP).matcher(http).matches();
    }

    /**
     * 匹配是否为 uri
     *
     * @param uri uri
     * @return true uri
     */
    public static boolean isUri(String uri) {
        return getPattern(URI).matcher(uri).matches();
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

    // --------------- ext ---------------

    /**
     * 组提取
     *
     * @param s       字符
     * @param pattern 模式
     * @return 匹配到的组
     */
    public static String extGroup(String s, String pattern) {
        Matcher matcher = getPattern(pattern).matcher(s);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * 组提取
     *
     * @param s       字符
     * @param pattern 模式
     * @return 匹配到的组
     */
    public static String extGroup(String s, Pattern pattern) {
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * 组提取
     *
     * @param s       字符
     * @param pattern 模式
     * @return 匹配到的组
     */
    public static List<String> extGroups(String s, String pattern) {
        List<String> groups = new ArrayList<>();
        Matcher matcher = getPattern(pattern).matcher(s);
        while (matcher.find()) {
            groups.add(matcher.group());
        }
        return groups;
    }

    /**
     * 组提取
     *
     * @param s       字符
     * @param pattern 模式
     * @return 匹配到的组
     */
    public static List<String> extGroups(String s, Pattern pattern) {
        List<String> groups = new ArrayList<>();
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            groups.add(matcher.group());
        }
        return groups;
    }

    /**
     * 提取手机号
     *
     * @param s 字符
     * @return 提取到的第一个手机号
     */
    public static String extPhone(String s) {
        return extGroup(s, getPatternExt(PHONE));
    }

    /**
     * 提取手机号
     *
     * @param s 字符
     * @return 提取到的所有手机号
     */
    public static List<String> extPhoneList(String s) {
        return extGroups(s, getPatternExt(PHONE));
    }

    /**
     * 提取邮箱
     *
     * @param s 字符
     * @return 提取到的第一个邮箱
     */
    public static String extEmail(String s) {
        return extGroup(s, getPatternExt(EMAIL));
    }

    /**
     * 提取邮箱
     *
     * @param s 字符
     * @return 提取到的所有邮箱
     */
    public static List<String> extEmailList(String s) {
        return extGroups(s, getPatternExt(EMAIL));
    }

    /**
     * 提取HTTP url
     *
     * @param s 字符
     * @return 提取到的第一个HTTP url
     */
    public static String extHttp(String s) {
        return extGroup(s, getPatternExt(HTTP));
    }

    /**
     * 提取HTTP url
     *
     * @param s 字符
     * @return 提取到的所有HTTP url
     */
    public static List<String> extHttpList(String s) {
        return extGroups(s, getPatternExt(HTTP));
    }

    /**
     * 提取Uri
     *
     * @param s 字符
     * @return 提取到的第一个Uri
     */
    public static String extUri(String s) {
        return extGroup(s, getPatternExt(URI));
    }

    /**
     * 提取Uri
     *
     * @param s 字符
     * @return 提取到的所有Uri
     */
    public static List<String> extUriList(String s) {
        return extGroups(s, getPatternExt(URI));
    }

    /**
     * 提取整数
     *
     * @param s 字符
     * @return 提取到的第一个整数
     */
    public static String extInteger(String s) {
        return extGroup(s, getPatternExt(INTEGER));
    }

    /**
     * 提取整数
     *
     * @param s 字符
     * @return 提取到的所有整数
     */
    public static List<String> extIntegerList(String s) {
        return extGroups(s, getPatternExt(INTEGER));
    }

    /**
     * 提取浮点数
     *
     * @param s 字符
     * @return 提取到的第一个浮点数
     */
    public static String extDouble(String s) {
        return extGroup(s, getPatternExt(DOUBLE));
    }

    /**
     * 提取浮点数
     *
     * @param s 字符
     * @return 提取到的所有浮点数
     */
    public static List<String> extDoubleList(String s) {
        return extGroups(s, getPatternExt(DOUBLE));
    }

    /**
     * 提取ipv4
     *
     * @param s 字符
     * @return 提取到的第一个ip
     */
    public static String extIp(String s) {
        return extGroup(s, getPatternExt(IPV4));
    }

    /**
     * 提取ipv4
     *
     * @param s 字符
     * @return 提取到的所有ip
     */
    public static List<String> extIpList(String s) {
        return extGroups(s, getPatternExt(IPV4));
    }

}

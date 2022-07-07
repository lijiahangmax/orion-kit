package com.orion.lang.utils.regexp;

import com.orion.lang.define.cache.SoftCache;

import java.util.regex.Pattern;

/**
 * 常用表达式
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/27 11:16
 */
public class Patterns {

    private Patterns() {
    }

    private static final SoftCache<String, Pattern> CACHE = new SoftCache<>();

    /**
     * 空白行
     */
    public static final Pattern SPACE_LINE = Pattern.compile("\\n\\s*\\r");

    /**
     * 首尾空格
     */
    public static final Pattern SPACE_POINT = Pattern.compile("^\\s*|\\s*$");

    /**
     * 手机号
     */
    public static final Pattern PHONE = Pattern.compile("^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$");

    /**
     * 邮箱正则
     */
    // public static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9]+([_.][A-Za-z0-9]+)*@([A-Za-z0-9\\-]+\\.)+[A-Za-z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern EMAIL = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])", Pattern.CASE_INSENSITIVE);

    /**
     * http正则
     */
    public static final Pattern HTTP = Pattern.compile("^(http|https)://([\\w.]+/?)\\S*$");

    /**
     * uri正则
     */
    public static final Pattern URI = Pattern.compile("^[a-zA-z]+://([\\w.]+/?)\\S*$");

    /**
     * integer正则
     */
    public static final Pattern INTEGER = Pattern.compile("^[-+]?[\\d]+$");

    /**
     * double正则
     */
    public static final Pattern DOUBLE = Pattern.compile("^[-+]?\\d*[.]\\d+$");

    /**
     * number正则
     */
    public static final Pattern NUMBER = Pattern.compile("^([-+]?\\d*[.]\\d+)$|^([-+]?[\\d]+)$");

    /**
     * number正则 提取
     */
    public static final Pattern NUMBER_EXT = Pattern.compile("([-+]?\\d*[.]\\d+)|([-+]?[\\d]+)");

    /**
     * IPV4正则
     */
    public static final Pattern IPV4 = Pattern.compile("^(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$");

    /**
     * IPV6正则
     */
    public static final Pattern IPV6 = Pattern.compile("^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:)|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}(:[0-9A-Fa-f]{1,4}){1,2})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){1,3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){1,4})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){1,5})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){1,6})|(:(:[0-9A-Fa-f]{1,4}){1,7})|(([0-9A-Fa-f]{1,4}:){6}(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){5}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){0,4}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(:(:[0-9A-Fa-f]{1,4}){0,5}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}))$");

    /**
     * MD5 正则
     */
    public static final Pattern MD5 = Pattern.compile("^[a-f0-9]{32}|[A-F0-9]{32}$");

    /**
     * windows文件路径 正则
     */
    public static final Pattern WINDOWS_PATH = Pattern.compile("^[A-z]:\\\\([^|><?*\":/]*\\\\)*([^|><?*\":/]*)?$|^[A-z]:/([^|><?*\":/]*/)*([^|><?*\":/]*)?$");

    /**
     * linux文件路径 正则
     */
    public static final Pattern LINUX_PATH = Pattern.compile("^/([^><\"]*/)*([^><\"]*)?$");

    /**
     * 邮编
     */
    public static final Pattern ZIP_CODE = Pattern.compile("[1-9]\\d{5}(?!\\d)");

    /**
     * 中文字、英文字母、数字和下划线
     */
    public static final Pattern UTF = Pattern.compile("^[\u4E00-\u9FFF\\w]+$");

    /**
     * UUID 包含-
     */
    public static final Pattern UUID = Pattern.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$");

    /**
     * MAC地址
     */
    public static final Pattern MAC = Pattern.compile("((?:[A-F0-9]{1,2}[:-]){5}[A-F0-9]{1,2})|(?:0x)(\\d{12})(?:.+ETHER)", Pattern.CASE_INSENSITIVE);

    /**
     * 16进制字符串
     */
    public static final Pattern HEX = Pattern.compile("^[a-f0-9]+$", Pattern.CASE_INSENSITIVE);

    /**
     * 社会统一信用代码
     */
    public static final Pattern CREDIT_CODE = Pattern.compile("^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$");

    /**
     * 18位身份证号码
     */
    public static final Pattern ID_CARD = Pattern.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}(\\d|X|x)");

    /**
     * 中国车牌号码
     */
    public static final Pattern PLATE_NUMBER = Pattern.compile(
            "^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[ABCDEFGHJK])|([ABCDEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4})))|" +
                    "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]\\d{3}\\d{1,3}[领])|" +
                    "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$");

    /**
     * hex 颜色 #FFF
     */
    public static final Pattern HEX_COLOR = Pattern.compile("^#?([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");

    /**
     * 日期 2021-01-01
     */
    public static final Pattern DATE = Pattern.compile("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$");

    /**
     * 微信号
     */
    public static final Pattern WE_CHAT = Pattern.compile("^[a-zA-Z]([-_a-zA-Z0-9]{5,19})+$");

    /**
     * qq号
     */
    public static final Pattern QQ = Pattern.compile("^[1-9][0-9]{4,10}$");

    /**
     * 包含中文
     */
    public static final Pattern CHINESE = Pattern.compile("[\\u4E00-\\u9FA5]*");

    /**
     * 用户名称 6到16位 (字母 数字)
     */
    public static final Pattern USERNAME_1 = Pattern.compile("^[a-zA-Z0-9]{6,16}$");

    /**
     * 用户名称 6到16位 (字母 数字 下滑线)
     */
    public static final Pattern USERNAME_2 = Pattern.compile("^[a-zA-Z0-9_]{6,16}$");

    /**
     * 密码 8到20位置 (数字 字母) 都要满足
     */
    public static final Pattern PASSWORD_1 = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$");

    /**
     * 密码 8到20位置 (数字 字母 符号) 2种或2种以上
     */
    public static final Pattern PASSWORD_2 = Pattern.compile("^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{8,20}$");

    /**
     * 密码 8到20位置 (数字 大写字母 小写字母 符号) 都要满足
     */
    public static final Pattern PASSWORD_3 = Pattern.compile("^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*_=+/\\-?]).{8,20}$");

    /**
     * 获取正则对象
     *
     * @param pattern 表达式
     * @return 正则
     */
    public static Pattern getPattern(String pattern) {
        Pattern p = CACHE.get(pattern);
        if (p == null) {
            p = Pattern.compile(pattern);
            CACHE.put(pattern, p);
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
    public static Pattern getPatternExt(Pattern pattern) {
        return getPatternExt(pattern.pattern());
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
            pattern = pattern.substring(1);
        }
        if (pattern.endsWith("$")) {
            pattern = pattern.substring(0, pattern.length() - 1);
        }
        Pattern p = CACHE.get(pattern);
        if (p == null) {
            p = Pattern.compile(pattern);
            CACHE.put(pattern, p);
        }
        return p;
    }

}

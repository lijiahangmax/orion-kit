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
package cn.orionsec.kit.lang.utils.regexp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则相关工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/1/5 16:13
 */
public class Matches {

    private Matches() {
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
     * 是否匹配
     *
     * @param s       字符
     * @param pattern 模式
     * @return true 匹配
     */
    public static boolean test(String s, String pattern) {
        return Patterns.getPattern(pattern).matcher(s).matches();
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
     * 组提取
     *
     * @param s       字符
     * @param pattern 模式
     * @return 匹配到的组
     */
    public static String extGroup(String s, String pattern) {
        Matcher matcher = Patterns.getPattern(pattern).matcher(s);
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
        Matcher matcher = Patterns.getPattern(pattern).matcher(s);
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

    // -------------------- test --------------------

    /**
     * 匹配是否为空行
     *
     * @param str 待匹配的字符
     * @return true空行
     */
    public static boolean isSpaceLine(String str) {
        return Patterns.SPACE_LINE.matcher(str).matches();
    }

    /**
     * 匹配是否为首尾空格
     *
     * @param str 待匹配的字符
     * @return true首尾空格
     */
    public static boolean isSpacePoint(String str) {
        return Patterns.SPACE_POINT.matcher(str).matches();
    }

    /**
     * 匹配是否为浮点数
     *
     * @param str 待匹配的字符
     * @return true浮点数
     */
    public static boolean isDouble(String str) {
        return Patterns.DOUBLE.matcher(str).matches();
    }

    /**
     * 匹配是否为整数
     *
     * @param str 待匹配的字符
     * @return true整数
     */
    public static boolean isInteger(String str) {
        return Patterns.INTEGER.matcher(str).matches();
    }

    /**
     * 匹配是否为数字
     *
     * @param str 待匹配的字符
     * @return true数字
     */
    public static boolean isNumber(String str) {
        return Patterns.NUMBER.matcher(str).matches();
    }

    /**
     * 匹配是否为IPV4
     *
     * @param ip ip
     * @return true IPV4
     */
    public static boolean isIpv4(String ip) {
        return Patterns.IPV4.matcher(ip).matches();
    }

    /**
     * 匹配是否为IPV6
     *
     * @param ip ip
     * @return true IPV6
     */
    public static boolean isIpv6(String ip) {
        return Patterns.IPV6.matcher(ip).matches();
    }

    /**
     * 匹配是否为手机号
     *
     * @param phone 手机号
     * @return true 手机号
     */
    public static boolean isPhone(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    /**
     * 匹配是否为邮箱
     *
     * @param email 邮箱
     * @return true 邮箱
     */
    public static boolean isEmail(String email) {
        return Patterns.EMAIL.matcher(email).matches();
    }

    /**
     * 匹配是否为http url
     *
     * @param http url
     * @return true HTTP url
     */
    public static boolean isHttp(String http) {
        return Patterns.HTTP.matcher(http).matches();
    }

    /**
     * 匹配是否为 uri
     *
     * @param uri uri
     * @return true uri
     */
    public static boolean isUri(String uri) {
        return Patterns.URI.matcher(uri).matches();
    }

    /**
     * 匹配是否为 MD5
     *
     * @param s s
     * @return true MD5
     */
    public static boolean isMd5(String s) {
        return Patterns.MD5.matcher(s).matches();
    }

    /**
     * 匹配是否为 windows文件路径
     *
     * @param path 路径
     * @return true windows文件路径
     */
    public static boolean isWindowsPath(String path) {
        return Patterns.WINDOWS_PATH.matcher(path).matches();
    }

    /**
     * 匹配是否为 linux文件路径
     *
     * @param path 路径
     * @return true linux文件路径
     */
    public static boolean isLinuxPath(String path) {
        return Patterns.LINUX_PATH.matcher(path).matches();
    }

    /**
     * 匹配是否为 操作系统文件路径
     *
     * @param path 路径
     * @return true 文件路径
     */
    public static boolean isPath(String path) {
        return Matches.isWindowsPath(path) || Matches.isLinuxPath(path);
    }

    /**
     * 匹配是否为 邮编
     *
     * @param s str
     * @return true 邮编
     */
    public static boolean isZipCode(String s) {
        return Patterns.ZIP_CODE.matcher(s).matches();
    }

    /**
     * 匹配是否为 中文字、英文字母、数字和下划线
     *
     * @param s str
     * @return true 中文字、英文字母、数字和下划线
     */
    public static boolean isUtf(String s) {
        return Patterns.UTF.matcher(s).matches();
    }

    /**
     * 匹配是否为 邮编
     *
     * @param s str
     * @return true 邮编
     */
    public static boolean isUuid(String s) {
        return Patterns.UUID.matcher(s).matches();
    }

    /**
     * 匹配是否为 MAC地址
     *
     * @param s str
     * @return true MAC地址
     */
    public static boolean isMac(String s) {
        return Patterns.MAC.matcher(s).matches();
    }

    /**
     * 匹配是否为 邮编
     *
     * @param s str
     * @return true 邮编
     */
    public static boolean isHex(String s) {
        return Patterns.HEX.matcher(s).matches();
    }

    /**
     * 匹配是否为 社会统一信用代码
     * <p>
     * 第一部分: 登记管理部门代码1位 (数字或大写英文字母)
     * 第二部分: 机构类别代码1位 (数字或大写英文字母)
     * 第三部分: 登记管理机关行政区划码6位 (数字)
     * 第四部分: 主体标识码 (组织机构代码) 9位 (数字或大写英文字母)
     * 第五部分: 校验码1位 (数字或大写英文字母)
     *
     * @param s 社会统一信用代码
     * @return true 社会统一信用代码
     */
    public static boolean isCreditCode(String s) {
        return Patterns.CREDIT_CODE.matcher(s).matches();
    }

    /**
     * 匹配是否为 18位身份证号码
     *
     * @param s str
     * @return true 18位身份证号码
     */
    public static boolean isIdCard(String s) {
        return Patterns.ID_CARD.matcher(s).matches();
    }

    /**
     * 匹配是否为 中国车牌号码
     *
     * @param s str
     * @return true 中国车牌号码
     */
    public static boolean isPlateNumber(String s) {
        return Patterns.PLATE_NUMBER.matcher(s).matches();
    }

    // -------------------- ext --------------------

    /**
     * 提取手机号
     *
     * @param s 字符
     * @return 提取到的第一个手机号
     */
    public static String extPhone(String s) {
        return extGroup(s, Patterns.getPatternExt(Patterns.PHONE));
    }

    /**
     * 提取手机号
     *
     * @param s 字符
     * @return 提取到的所有手机号
     */
    public static List<String> extPhoneList(String s) {
        return extGroups(s, Patterns.getPatternExt(Patterns.PHONE));
    }

    /**
     * 提取邮箱
     *
     * @param s 字符
     * @return 提取到的第一个邮箱
     */
    public static String extEmail(String s) {
        return extGroup(s, Patterns.getPatternExt(Patterns.EMAIL));
    }

    /**
     * 提取邮箱
     *
     * @param s 字符
     * @return 提取到的所有邮箱
     */
    public static List<String> extEmailList(String s) {
        return extGroups(s, Patterns.getPatternExt(Patterns.EMAIL));
    }

    /**
     * 提取HTTP url
     *
     * @param s 字符
     * @return 提取到的第一个HTTP url
     */
    public static String extHttp(String s) {
        return extGroup(s, Patterns.getPatternExt(Patterns.HTTP));
    }

    /**
     * 提取HTTP url
     *
     * @param s 字符
     * @return 提取到的所有HTTP url
     */
    public static List<String> extHttpList(String s) {
        return extGroups(s, Patterns.getPatternExt(Patterns.HTTP));
    }

    /**
     * 提取Uri
     *
     * @param s 字符
     * @return 提取到的第一个Uri
     */
    public static String extUri(String s) {
        return extGroup(s, Patterns.getPatternExt(Patterns.URI));
    }

    /**
     * 提取Uri
     *
     * @param s 字符
     * @return 提取到的所有Uri
     */
    public static List<String> extUriList(String s) {
        return extGroups(s, Patterns.getPatternExt(Patterns.URI));
    }

    /**
     * 提取整数
     *
     * @param s 字符
     * @return 提取到的第一个整数
     */
    public static String extInteger(String s) {
        return extGroup(s, Patterns.getPatternExt(Patterns.INTEGER));
    }

    /**
     * 提取整数
     *
     * @param s 字符
     * @return 提取到的所有整数
     */
    public static List<String> extIntegerList(String s) {
        return extGroups(s, Patterns.getPatternExt(Patterns.INTEGER));
    }

    /**
     * 提取浮点数
     *
     * @param s 字符
     * @return 提取到的第一个浮点数
     */
    public static String extDouble(String s) {
        return extGroup(s, Patterns.getPatternExt(Patterns.DOUBLE));
    }

    /**
     * 提取浮点数
     *
     * @param s 字符
     * @return 提取到的所有浮点数
     */
    public static List<String> extDoubleList(String s) {
        return extGroups(s, Patterns.getPatternExt(Patterns.DOUBLE));
    }

    /**
     * 提取数字
     *
     * @param s 字符
     * @return 提取到的第一个数字
     */
    public static String extNumber(String s) {
        return extGroup(s, Patterns.getPatternExt(Patterns.NUMBER_EXT));
    }

    /**
     * 提取数字
     *
     * @param s 字符
     * @return 提取到的所有数字
     */
    public static List<String> extNumberList(String s) {
        return extGroups(s, Patterns.getPatternExt(Patterns.NUMBER_EXT));
    }

    /**
     * 提取ipv4
     *
     * @param s 字符
     * @return 提取到的第一个ip
     */
    public static String extIpv4(String s) {
        return extGroup(s, Patterns.getPatternExt(Patterns.IPV4));
    }

    /**
     * 提取ipv4
     *
     * @param s 字符
     * @return 提取到的所有ip
     */
    public static List<String> extIpv4List(String s) {
        return extGroups(s, Patterns.getPatternExt(Patterns.IPV4));
    }

    /**
     * 提取ipv6
     *
     * @param s 字符
     * @return 提取到的第一个ip
     */
    public static String extIpv6(String s) {
        return extGroup(s, Patterns.getPatternExt(Patterns.IPV6));
    }

    /**
     * 提取ipv6
     *
     * @param s 字符
     * @return 提取到的所有ip
     */
    public static List<String> extIpv6List(String s) {
        return extGroups(s, Patterns.getPatternExt(Patterns.IPV6));
    }

    /**
     * 提取mac
     *
     * @param s 字符
     * @return 提取到的第一个mac
     */
    public static String extMac(String s) {
        return extGroup(s, Patterns.getPatternExt(Patterns.MAC));
    }

    /**
     * 提取mac
     *
     * @param s 字符
     * @return 提取到的所有mac
     */
    public static List<String> extMacList(String s) {
        return extGroups(s, Patterns.getPatternExt(Patterns.MAC));
    }

    /**
     * 提取社会统一信用代码
     *
     * @param s 字符
     * @return 提取到的第一个社会统一信用代码
     */
    public static String extCreditCode(String s) {
        return extGroup(s, Patterns.getPatternExt(Patterns.CREDIT_CODE));
    }

    /**
     * 提取社会统一信用代码
     *
     * @param s 字符
     * @return 提取到的所有社会统一信用代码
     */
    public static List<String> extCreditCodeList(String s) {
        return extGroups(s, Patterns.getPatternExt(Patterns.CREDIT_CODE));
    }

    /**
     * 提取18位身份证号码
     *
     * @param s 字符
     * @return 提取到的第一个18位身份证号码
     */
    public static String extIdCard(String s) {
        return extGroup(s, Patterns.getPatternExt(Patterns.ID_CARD));
    }

    /**
     * 提取18位身份证号码
     *
     * @param s 字符
     * @return 提取到的所有18位身份证号码
     */
    public static List<String> extIdCardList(String s) {
        return extGroups(s, Patterns.getPatternExt(Patterns.ID_CARD));
    }

    /**
     * 提取中国车牌号码
     *
     * @param s 字符
     * @return 提取到的第一个中国车牌号码
     */
    public static String extPlateNumber(String s) {
        return extGroup(s, Patterns.getPatternExt(Patterns.PLATE_NUMBER));
    }

    /**
     * 提取中国车牌号码
     *
     * @param s 字符
     * @return 提取到的所有身份证号码
     */
    public static List<String> extPlateNumberList(String s) {
        return extGroups(s, Patterns.getPatternExt(Patterns.PLATE_NUMBER));
    }

}

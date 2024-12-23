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
package cn.orionsec.kit.lang.utils.regexp;

import cn.orionsec.kit.lang.KitLangConfiguration;
import cn.orionsec.kit.lang.config.KitConfig;
import cn.orionsec.kit.lang.define.cache.SoftCache;

import java.util.regex.Pattern;

/**
 * 常用正则表达式
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/27 11:16
 */
public class Patterns {

    private static final SoftCache<String, Pattern> CACHE = new SoftCache<>();

    /**
     * 空白行
     */
    public static final Pattern SPACE_LINE = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_SPACE_LINE);

    /**
     * 首尾空格
     */
    public static final Pattern SPACE_POINT = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_SPACE_POINT);

    /**
     * 手机号
     */
    public static final Pattern PHONE = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_PHONE);

    /**
     * 邮箱正则
     */
    public static final Pattern EMAIL = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_EMAIL);

    /**
     * http 正则
     */
    public static final Pattern HTTP = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_HTTP);

    /**
     * uri 正则
     */
    public static final Pattern URI = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_URI);

    /**
     * integer 正则
     */
    public static final Pattern INTEGER = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_INTEGER);

    /**
     * double 正则
     */
    public static final Pattern DOUBLE = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_DOUBLE);

    /**
     * number 正则
     */
    public static final Pattern NUMBER = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_NUMBER);

    /**
     * number 正则 提取
     */
    public static final Pattern NUMBER_EXT = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_NUMBER_EXT);

    /**
     * IPV4 正则
     */
    public static final Pattern IPV4 = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_IPV4);

    /**
     * IPV6 正则
     */
    public static final Pattern IPV6 = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_IPV6);

    /**
     * MD5 正则
     */
    public static final Pattern MD5 = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_MD5);

    /**
     * windows 文件路径 正则
     */
    public static final Pattern WINDOWS_PATH = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_WINDOWS_PATH);

    /**
     * linux 文件路径 正则
     */
    public static final Pattern LINUX_PATH = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_LINUX_PATH);

    /**
     * 邮编
     */
    public static final Pattern ZIP_CODE = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_ZIP_CODE);

    /**
     * 中文字、英文字母、数字和下划线
     */
    public static final Pattern UTF = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_UTF);

    /**
     * UUID 包含 -
     */
    public static final Pattern UUID = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_UUID);

    /**
     * MAC 地址
     */
    public static final Pattern MAC = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_MAC);

    /**
     * 16进制字符串
     */
    public static final Pattern HEX = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_HEX);

    /**
     * 社会统一信用代码
     */
    public static final Pattern CREDIT_CODE = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_CREDIT_CODE);

    /**
     * 18位身份证号码
     */
    public static final Pattern ID_CARD = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_ID_CARD);

    /**
     * 中国车牌号码
     */
    public static final Pattern PLATE_NUMBER = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_PLATE_NUMBER);

    /**
     * hex 颜色 #FFF
     */
    public static final Pattern HEX_COLOR = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_HEX_COLOR);

    /**
     * 日期 2021-01-01
     */
    public static final Pattern DATE = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_DATE);

    /**
     * 微信号
     */
    public static final Pattern WE_CHAT = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_WE_CHAT);

    /**
     * qq 号
     */
    public static final Pattern QQ = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_QQ);

    /**
     * 包含中文
     */
    public static final Pattern CHINESE = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_CHINESE);

    /**
     * 用户名称 6到16位 (字母 数字)
     */
    public static final Pattern USERNAME_1 = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_USERNAME_1);

    /**
     * 用户名称 6到16位 (字母 数字 下滑线)
     */
    public static final Pattern USERNAME_2 = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_USERNAME_2);

    /**
     * 密码 8到20位置 (数字 字母) 都要满足
     */
    public static final Pattern PASSWORD_1 = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_PASSWORD_1);

    /**
     * 密码 8到20位置 (数字 字母 符号) 2种或2种以上
     */
    public static final Pattern PASSWORD_2 = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_PASSWORD_2);

    /**
     * 密码 8到20位置 (数字 大写字母 小写字母 符号) 都要满足
     */
    public static final Pattern PASSWORD_3 = KitConfig.get(KitLangConfiguration.CONFIG.PATTERN_PASSWORD_3);

    private Patterns() {
    }

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

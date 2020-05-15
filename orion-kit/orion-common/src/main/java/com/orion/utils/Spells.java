package com.orion.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 拼音工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/1/2 18:14
 */
public class Spells {

    /**
     * 中文正则
     */
    private static final Pattern CHINESE_CHAR = Pattern.compile("[\\u4E00-\\u9FA5]+");

    /**
     * 中文正则 JCK
     */
    private static final Pattern CHINESE_CJK = Pattern.compile("\\p{InCJK Unified Ideographs}&&\\P{Cn}");

    /**
     * 中文正则 REG
     */
    private static final Pattern CHINESE_REG = Pattern.compile("[\\u4E00-\\u9FBF]+");

    /**
     * 乱码正则
     */
    private static final Pattern MESSY_CODE = Pattern.compile("\\s*|\t*|\r*|\n*");

    private Spells() {
    }

    /**
     * 将字符串中的中文转化为拼音, 其他字符不变
     *
     * @param s 中文
     * @return 拼音
     */
    public static String getSpell(String s) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] input = s.trim().toCharArray();
        StringBuilder sb = new StringBuilder();
        try {
            for (char c : input) {
                if (Matches.test(Character.toString(c), CHINESE_CHAR)) {
                    sb.append(PinyinHelper.toHanyuPinyinStringArray(c, format)[0]);
                } else {
                    sb.append(Character.toString(c));
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            // ignore
        }
        return sb.toString();
    }

    /**
     * 获取汉字串拼音首字母, 英文字符不变
     *
     * @param s 汉字
     * @return 汉语拼音首字母
     */
    public static String getFirstSpell(String s) {
        StringBuilder sb = new StringBuilder();
        char[] arr = s.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char anArr : arr) {
            if (anArr > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(anArr, defaultFormat);
                    if (temp != null) {
                        sb.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                sb.append(anArr);
            }
        }
        return sb.toString().replaceAll("\\W", "").trim();
    }

    /**
     * 将字符串中的中文转化为拼音, 其他字符不变 有声调
     *
     * @param s 汉字串
     * @return 汉语拼音
     */
    public static String getFullSpell(String s) {
        StringBuilder sb = new StringBuilder();
        char[] arr = s.toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        for (char anArr : arr) {
            if (anArr > 128) {
                try {
                    sb.append(PinyinHelper.toHanyuPinyinStringArray(anArr, format)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    // ignore
                }
            } else {
                sb.append(anArr);
            }
        }
        return sb.toString();
    }

    /**
     * 通过unicode正则
     * 判断字符串是否是中文
     * 只能判断部分CJK字符
     *
     * @param s ignore
     * @return true 中文
     */
    public static boolean isChineseByReg(String s) {
        if (Strings.isEmpty(s)) {
            return false;
        }
        return CHINESE_REG.matcher(s.trim()).find();
    }

    /**
     * 通过 InCJK 正则
     * 判断字符串是否是中文
     * 只能判断部分CJK字符
     *
     * @param s ignore
     * @return true 中文
     */
    public static boolean isChineseByName(String s) {
        if (Strings.isEmpty(s)) {
            return false;
        }
        // \\p 表示包含大写, \\P 表示不包含大写
        // \\p{Cn} 表示 Unicode 中未被定义字符的编码
        // \\P{Cn} 表示 Unicode中已经被定义字符的编码
        return CHINESE_CJK.matcher(s.trim()).find();
    }

    /**
     * 是否包含中文
     *
     * @param s s
     * @return true 包含中文
     */
    public static boolean containsChinese(String s) {
        char[] ch = s.toCharArray();
        for (char c : ch) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否全为中文 包括空格
     *
     * @param s s
     * @return true 全中文
     */
    public static boolean isAllChinese(String s) {
        char[] ch = s.toCharArray();
        for (char c : ch) {
            if (!isChinese(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否是中文
     *
     * @param c char字符
     * @return true 中文
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    /**
     * 获取一个字符串中中文字符的个数
     *
     * @param s ignore
     * @return 中文的个数
     */
    public static int getChineseLength(String s) {
        Matcher m = CHINESE_CHAR.matcher(s);
        int i = 0;
        while (m.find()) {
            String temp = m.group(0);
            i += temp.length();
        }
        return i;
    }

    /**
     * 判断是否是乱码
     *
     * @param s ignore
     * @return true 乱码
     */
    public static boolean isMessyCode(String s) {
        Matcher m = MESSY_CODE.matcher(s);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = 0;
        float count = 0;
        for (char c : ch) {
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
                chLength++;
            }
        }
        float result = count / chLength;
        return result > 0.4;
    }

}

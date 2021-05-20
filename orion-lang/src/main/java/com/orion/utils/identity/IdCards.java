package com.orion.utils.identity;

import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.regexp.Matches;
import com.orion.utils.regexp.Patterns;
import com.orion.utils.time.DateStream;
import com.orion.utils.time.Dates;

import java.util.*;

/**
 * 身份证工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 23:12
 */
public class IdCards {

    private IdCards() {
    }

    /**
     * 中国公民身份证号码最小长度
     */
    private static final int CHINA_ID_MIN_LENGTH = 15;

    /**
     * 中国公民身份证号码最大长度
     */
    private static final int CHINA_ID_MAX_LENGTH = 18;

    /**
     * 每位加权因子
     */
    private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 省市代码表
     */
    private static final Map<String, String> CITY_CODES = new HashMap<>();

    static {
        CITY_CODES.put("11", "北京");
        CITY_CODES.put("12", "天津");
        CITY_CODES.put("13", "河北");
        CITY_CODES.put("14", "山西");
        CITY_CODES.put("15", "内蒙古");
        CITY_CODES.put("21", "辽宁");
        CITY_CODES.put("22", "吉林");
        CITY_CODES.put("23", "黑龙江");
        CITY_CODES.put("31", "上海");
        CITY_CODES.put("32", "江苏");
        CITY_CODES.put("33", "浙江");
        CITY_CODES.put("34", "安徽");
        CITY_CODES.put("35", "福建");
        CITY_CODES.put("36", "江西");
        CITY_CODES.put("37", "山东");
        CITY_CODES.put("41", "河南");
        CITY_CODES.put("42", "湖北");
        CITY_CODES.put("43", "湖南");
        CITY_CODES.put("44", "广东");
        CITY_CODES.put("45", "广西");
        CITY_CODES.put("46", "海南");
        CITY_CODES.put("50", "重庆");
        CITY_CODES.put("51", "四川");
        CITY_CODES.put("52", "贵州");
        CITY_CODES.put("53", "云南");
        CITY_CODES.put("54", "西藏");
        CITY_CODES.put("61", "陕西");
        CITY_CODES.put("62", "甘肃");
        CITY_CODES.put("63", "青海");
        CITY_CODES.put("64", "宁夏");
        CITY_CODES.put("65", "新疆");
        CITY_CODES.put("71", "台湾");
        CITY_CODES.put("81", "香港");
        CITY_CODES.put("82", "澳门");
        CITY_CODES.put("91", "国外");
    }

    /**
     * 将15位身份证号码转换为18位
     *
     * @param idCard 15位身份编码
     * @return 18位身份编码
     */
    public static String convertCard(String idCard) {
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return null;
        }
        if (Matches.test(idCard, Patterns.INTEGER)) {
            // 获取出生年月日
            Date birthDate = Dates.parse("19" + idCard.substring(6, 12), "yyyyMMdd");
            int year = Dates.stream(birthDate).getYear();
            StringBuilder idCard18 = new StringBuilder().append(idCard, 0, 6).append(year).append(idCard.substring(8));
            // 获取校验位
            char sVal = getCheckCode18(idCard18.toString());
            idCard18.append(sVal);
            return idCard18.toString();
        } else {
            return null;
        }
    }

    /**
     * 是否有效身份证号
     *
     * @param idCard 身份证号，支持18位、15位
     * @return 是否有效
     */
    public static boolean isValidCard(String idCard) {
        idCard = idCard.trim();
        int length = idCard.length();
        switch (length) {
            case 18:
                return isValidCard18(idCard);
            case 15:
                return isValidCard15(idCard);
            default:
                return false;
        }
    }

    /**
     * 判断18位身份证的合法性
     * <p>
     * 第1、2位数字表示: 所在省份的代码
     * 第3、4位数字表示: 所在城市的代码
     * 第5、6位数字表示: 所在区县的代码
     * 第7~14位数字表示: 出生年月日
     * 第15、16位数字表示: 所在地的派出所的代码
     * 第17位数字表示性别: 奇数表示男性, 偶数表示女性
     * 第18位数字是校检码: 用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示
     * <p>
     * 校验码的计算方法为:
     * 将前面的身份证号码17位数分别乘以不同的系数, 从第一位到第十七位的系数分别为: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * 将这17位数字和系数相乘的结果相加除以11取余
     * 余数 0 1 2 3 4 5 6 7 8 9 10
     * 对应 1 0 X 9 8 7 6 5 4 3 2
     *
     * @param idCard 身份编码
     * @return 是否有效
     */
    public static boolean isValidCard18(String idCard) {
        if (CHINA_ID_MAX_LENGTH != idCard.length()) {
            return false;
        }
        // 省份
        String proCode = idCard.substring(0, 2);
        if (null == CITY_CODES.get(proCode)) {
            return false;
        }
        // 生日
        Date birthDay = Dates.parse(idCard.substring(6, 14), "yyyyMMdd");
        if (birthDay == null) {
            return false;
        }
        DateStream bs = Dates.stream(birthDay);
        if (!isBirthday(bs.getYear(), bs.getMonth(), bs.getDay())) {
            return false;
        }
        // 前17位
        String code17 = idCard.substring(0, 17);
        // 第18位
        char code18 = Character.toLowerCase(idCard.charAt(17));
        if (Matches.test(code17, Patterns.INTEGER)) {
            // 校验位
            return getCheckCode18(code17) == code18;
        }
        return false;
    }

    /**
     * 验证15位身份编码是否合法
     *
     * @param idCard 身份编码
     * @return 是否有效
     */
    public static boolean isValidCard15(String idCard) {
        if (CHINA_ID_MIN_LENGTH != idCard.length()) {
            return false;
        }
        if (Matches.test(idCard, Patterns.INTEGER)) {
            // 省份
            String proCode = idCard.substring(0, 2);
            if (null == CITY_CODES.get(proCode)) {
                return false;
            }
            // 校验生日
            Date birthDay = Dates.parse("19" + idCard.substring(6, 12), "yyyyMMdd");
            if (birthDay == null) {
                return false;
            }
            DateStream bs = Dates.stream(birthDay);
            return isBirthday(bs.getYear(), bs.getMonth(), bs.getDay());
        } else {
            return false;
        }
    }

    /**
     * 根据身份编号获取年龄 只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @return 年龄
     */
    public static int getAge(String idCard) {
        return getAge(idCard, new Date());
    }

    /**
     * 根据身份编号获取指定日期当时的年龄年龄 只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @param range  边界
     * @return 年龄
     */
    public static int getAge(String idCard, Date range) {
        String birth = getBirth(idCard);
        return age(Dates.parse(birth, "yyyyMMdd"), range);
    }

    /**
     * 根据身份编号获取生日 只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @return yyyyMMdd
     */
    public static String getBirth(String idCard) {
        int len = idCard.length();
        Valid.isTrue(() -> len == 15 || len == 18, "ID Card length must be 15 or 18");
        if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convertCard(idCard);
        }
        return Objects.requireNonNull(idCard).substring(6, 14);
    }

    /**
     * 根据身份编号获取生日年 只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @return yyyy
     */
    public static Short getYear(String idCard) {
        int len = idCard.length();
        Valid.isTrue(() -> len == 15 || len == 18, "ID Card length must be 15 or 18");
        if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convertCard(idCard);
        }
        return Short.valueOf(Objects.requireNonNull(idCard).substring(6, 10));
    }

    /**
     * 根据身份编号获取生日月 只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @return MM
     */
    public static short getMonth(String idCard) {
        int len = idCard.length();
        Valid.isTrue(() -> len == 15 || len == 18, "ID Card length must be 15 or 18");
        if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convertCard(idCard);
        }
        return Short.parseShort(Objects.requireNonNull(idCard).substring(10, 12));
    }

    /**
     * 根据身份编号获取生日天 只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @return dd
     */
    public static short getDay(String idCard) {
        int len = idCard.length();
        Valid.isTrue(() -> len == 15 || len == 18, "ID Card length must be 15 or 18");
        if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convertCard(idCard);
        }
        return Short.parseShort(Objects.requireNonNull(idCard).substring(12, 14));
    }

    /**
     * 根据身份编号获取性别
     * 只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @return 0女 1男
     */
    public static int getGender(String idCard) {
        int len = idCard.length();
        Valid.isTrue(() -> len == 15 || len == 18, "ID Card length must be 15 or 18");
        if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convertCard(idCard);
        }
        char sCardChar = Objects.requireNonNull(idCard).charAt(16);
        return sCardChar % 2;
    }

    /**
     * 根据身份编号获取户籍省份，只支持15或18位身份证号码
     *
     * @param idCard 身份编码
     * @return 省级编码。
     */
    public static String getProvince(String idCard) {
        int len = idCard.length();
        Valid.isTrue(() -> len == 15 || len == 18, "ID Card length must be 15 or 18");
        return CITY_CODES.get(idCard.substring(0, 2));
    }

    /**
     * 获得18位身份证校验码
     *
     * @param code17 18位身份证号中的前17位
     * @return 第18位
     */
    private static char getCheckCode18(String code17) {
        return getCheckCode18(getPowerSum(code17.toCharArray()));
    }

    /**
     * 将power和值与11取模获得余数进行校验码判断
     *
     * @param iSum 加权和
     * @return 校验位
     */
    private static char getCheckCode18(int iSum) {
        switch (iSum % 11) {
            case 10:
                return '2';
            case 9:
                return '3';
            case 8:
                return '4';
            case 7:
                return '5';
            case 6:
                return '6';
            case 5:
                return '7';
            case 4:
                return '8';
            case 3:
                return '9';
            case 2:
                return 'x';
            case 1:
                return '0';
            case 0:
                return '1';
            default:
                return ' ';
        }
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后, 再得到和值
     *
     * @param iArr 身份证号码的数组
     * @return 身份证编码
     */
    private static int getPowerSum(char[] iArr) {
        int iSum = 0;
        if (POWER.length == iArr.length) {
            for (int i = 0; i < iArr.length; i++) {
                iSum += Integer.parseInt(String.valueOf(iArr[i])) * POWER[i];
            }
        }
        return iSum;
    }

    /**
     * 验证是否为生日
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 是否为生日
     */
    public static boolean isBirthday(int year, int month, int day) {
        int thisYear = Dates.stream().getYear();
        if (year < 1900 || year > thisYear) {
            return false;
        }
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || day > 31) {
            return false;
        }
        if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
            return false;
        }
        if (month == 2) {
            return day < 29 || (day == 29 && Dates.isLeapYear(year));
        }
        return true;
    }

    /**
     * 计算年龄
     *
     * @param birthday 生日
     * @return 年龄
     */
    public static int age(Date birthday) {
        return age(birthday.getTime(), System.currentTimeMillis());
    }

    /**
     * 计算年龄
     *
     * @param birthday 生日
     * @return 年龄
     */
    public static int age(long birthday) {
        return age(birthday, System.currentTimeMillis());
    }

    /**
     * 计算年龄
     *
     * @param birthday 生日
     * @param range    需要对比的日期
     * @return 年龄
     */
    public static int age(Date birthday, Date range) {
        if (null == range) {
            range = new Date();
        }
        return age(birthday.getTime(), range.getTime());
    }

    /**
     * 计算年龄
     *
     * @param birthday 生日
     * @param range    需要对比的日期
     * @return 年龄
     */
    public static int age(long birthday, long range) {
        if (birthday > range) {
            throw Exceptions.argument("birthday is after range!");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(range);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        boolean isLastDayOfMonth = dayOfMonth == cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.setTimeInMillis(birthday);
        int age = year - cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        if (month == monthBirth) {
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
            boolean isLastDayOfMonthBirth = dayOfMonthBirth == cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            if ((!isLastDayOfMonth || !isLastDayOfMonthBirth) && dayOfMonth < dayOfMonthBirth) {
                age--;
            }
        } else if (month < monthBirth) {
            age--;
        }
        return age;
    }

}

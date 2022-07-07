package com.orion.lang.utils.identity;

import com.orion.lang.utils.Valid;
import com.orion.lang.utils.regexp.Matches;
import com.orion.lang.utils.regexp.Patterns;
import com.orion.lang.utils.time.Birthdays;
import com.orion.lang.utils.time.DateStream;
import com.orion.lang.utils.time.Dates;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 身份证工具类
 * <p>
 * 18位身份证
 * 第1、2位数字表示: 所在省份的代码
 * 第3、4位数字表示: 所在城市的代码
 * 第5、6位数字表示: 所在区县的代码
 * 第7~14位数字表示: 出生年月日
 * 第15、16位数字表示: 所在地的派出所的顺序码
 * 第17位数字表示性别: 奇数表示男性, 偶数表示女性
 * 第18位数字是校检码: 用来检验身份证的正确性 校检码可以是0~9的数字, 有时也用x表示
 * <p>
 * 校验码的计算方法为:
 * 将前面的身份证号码17位数分别乘以不同的系数, 从第一位到第十七位的系数分别为: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
 * 将这17位数字和系数相乘的结果相加除以11取余
 * 余数 0 1 2 3 4 5 6 7 8 9 10
 * 对应 1 0 X 9 8 7 6 5 4 3 2
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 23:12
 */
public class IdCards {

    private IdCards() {
    }

    /**
     * 第二代 中国公民身份证号码长度
     */
    private static final int CHINA_ID_LENGTH = 18;

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
     * 判断18位身份证的合法性
     *
     * @param idCard 身份编码
     * @return 是否有效
     */
    public static boolean isValidCard(String idCard) {
        if (CHINA_ID_LENGTH != idCard.length()) {
            return false;
        }
        // 省份
        String proCode = idCard.substring(0, 2);
        if (CITY_CODES.get(proCode) == null) {
            return false;
        }
        // 生日
        Date birthDay = Dates.parse(idCard.substring(6, 14), Dates.YMD2);
        if (birthDay == null) {
            return false;
        }
        DateStream bs = Dates.stream(birthDay);
        if (!Birthdays.isBirthdayNotFuture(bs.getYear(), bs.getMonth(), bs.getDay())) {
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
     * 根据身份编号获取年龄
     *
     * @param idCard 身份编号
     * @return 年龄
     */
    public static int getAge(String idCard) {
        return getAge(idCard, new Date());
    }

    /**
     * 根据身份编号获取指定日期当时的年龄年龄
     *
     * @param idCard 身份编号
     * @param range  边界
     * @return 年龄
     */
    public static int getAge(String idCard, Date range) {
        String birth = getBirth(idCard);
        return Birthdays.getAge(Dates.parse(birth, Dates.YMD2), range);
    }

    /**
     * 根据身份编号获取生日
     *
     * @param idCard 身份编号
     * @return yyyyMMdd
     */
    public static String getBirth(String idCard) {
        Valid.validLength(idCard, CHINA_ID_LENGTH, "ID Card length must be 18");

        return Objects.requireNonNull(idCard).substring(6, 14);
    }

    /**
     * 根据身份编号获取生日年
     *
     * @param idCard 身份编号
     * @return yyyy
     */
    public static int getYear(String idCard) {
        Valid.validLength(idCard, CHINA_ID_LENGTH, "ID Card length must be 18");
        return Integer.parseInt(Objects.requireNonNull(idCard).substring(6, 10));
    }

    /**
     * 根据身份编号获取生日月
     *
     * @param idCard 身份编号
     * @return MM
     */
    public static int getMonth(String idCard) {
        Valid.validLength(idCard, CHINA_ID_LENGTH, "ID Card length must be 18");
        return Integer.parseInt(Objects.requireNonNull(idCard).substring(10, 12));
    }

    /**
     * 根据身份编号获取生日天
     *
     * @param idCard 身份编号
     * @return dd
     */
    public static int getDay(String idCard) {
        Valid.validLength(idCard, CHINA_ID_LENGTH, "ID Card length must be 18");
        return Integer.parseInt(Objects.requireNonNull(idCard).substring(12, 14));
    }

    /**
     * 根据身份编号获取性别
     *
     * @param idCard 身份编号
     * @return true男
     */
    public static boolean getGender(String idCard) {
        Valid.validLength(idCard, CHINA_ID_LENGTH, "ID Card length must be 18");
        return idCard.charAt(16) % 2 == 1;
    }

    /**
     * 根据身份编号获取户籍省份
     *
     * @param idCard 身份编码
     * @return 省级编码
     */
    public static String getProvince(String idCard) {
        Valid.validLength(idCard, CHINA_ID_LENGTH, "ID Card length must be 18");
        return CITY_CODES.get(idCard.substring(0, 2));
    }

    /**
     * 根据身份编号获取户籍省级编码
     *
     * @param idCard 身份编码
     * @return 省级编码
     */
    public static Integer getProvinceCode(String idCard) {
        Valid.validLength(idCard, CHINA_ID_LENGTH, "ID Card length must be 18");
        return Integer.valueOf(idCard.substring(0, 2));
    }

    /**
     * 根据身份编号获取户籍市级编码
     *
     * @param idCard 身份编码
     * @return 市级编码
     */
    public static Integer getCityCode(String idCard) {
        Valid.validLength(idCard, CHINA_ID_LENGTH, "ID Card length must be 18");
        return Integer.valueOf(idCard.substring(0, 4));
    }

    /**
     * 根据身份编号获取户籍县级编码
     *
     * @param idCard 身份编码
     * @return 县级编码
     */
    public static Integer getCountryCode(String idCard) {
        Valid.validLength(idCard, CHINA_ID_LENGTH, "ID Card length must be 18");
        return Integer.valueOf(idCard.substring(0, 6));
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后, 再得到和值
     *
     * @param arr 身份证号码的数组
     * @return 身份证编码
     */
    public static int getPowerSum(char[] arr) {
        int sum = 0;
        if (POWER.length == arr.length) {
            for (int i = 0; i < arr.length; i++) {
                sum += Integer.parseInt(String.valueOf(arr[i])) * POWER[i];
            }
        }
        return sum;
    }

    /**
     * 将power和值与11取模获得余数进行校验码判断
     *
     * @param sum 加权和
     * @return 校验位
     */
    public static char getCheckCode18(int sum) {
        switch (sum % 11) {
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
     * 获得18位身份证校验码
     *
     * @param code17 18位身份证号中的前17位
     * @return 第18位
     */
    public static char getCheckCode18(String code17) {
        return getCheckCode18(getPowerSum(code17.toCharArray()));
    }

}

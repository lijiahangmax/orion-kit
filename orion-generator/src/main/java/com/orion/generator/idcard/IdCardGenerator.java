package com.orion.generator.idcard;

import com.orion.generator.addres.AddressGenerator;
import com.orion.generator.addres.AddressSupport;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.identity.IdCards;
import com.orion.lang.utils.random.Randoms;
import com.orion.lang.utils.time.Birthdays;
import com.orion.lang.utils.time.DateStream;
import com.orion.lang.utils.time.Dates;

import java.util.Date;

/**
 * 身份证生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/11 14:38
 */
public class IdCardGenerator {

    private static final int[] MAN_GENDER_CODE = new int[]{1, 3, 5, 7, 9};

    private static final int[] WOMAN_GENDER_CODE = new int[]{0, 2, 4, 6, 8};

    private static final int AGE_MIN = 18;

    private static final int AGE_MAX = 50;

    private static final String PSB = "公安局";

    private IdCardGenerator() {
    }

    /**
     * 随机生成一个身份证信息 [18 ~ 50] 岁
     *
     * @return 身份证
     */
    public static String generator() {
        return generator(AGE_MIN, AGE_MAX);
    }

    public static String generator(int ageMin, int ageMax) {
        return generator(AddressSupport.randomCountyCode().toString(), ageMin, ageMax, Randoms.randomBoolean());
    }

    public static String generator(String code, int ageMin, int ageMax) {
        return generator(code, ageMin, ageMax, Randoms.randomBoolean());
    }

    public static String generator(int ageMin, int ageMax, boolean gender) {
        return generator(AddressSupport.randomCountyCode().toString(), ageMin, ageMax, gender);
    }

    /**
     * 生成身份证
     *
     * @param code   code 6位
     * @param ageMin ageMin [
     * @param ageMax ageMax ]
     * @param gender true男
     * @return 身份证
     */
    public static String generator(String code, int ageMin, int ageMax, boolean gender) {
        return generator(code, Randoms.randomInt(ageMin, ageMax + 1), gender);
    }

    public static String generator(int age) {
        return generator(AddressSupport.randomCountyCode().toString(), age, Randoms.randomBoolean());
    }

    public static String generator(String code, int age) {
        return generator(code, age, Randoms.randomBoolean());
    }

    public static String generator(int age, boolean gender) {
        return generator(AddressSupport.randomCountyCode().toString(), age, gender);
    }

    /**
     * 生成身份证
     *
     * @param code   code 6位
     * @param age    age
     * @param gender true男
     * @return 身份证
     */
    public static String generator(String code, int age, boolean gender) {
        return generator(code, Birthdays.generatorBirthday(age), gender);
    }

    public static String generator(String birthday) {
        return generator(AddressSupport.randomCountyCode().toString(), birthday, Randoms.randomBoolean());
    }

    public static String generator(String birthday, boolean gender) {
        return generator(AddressSupport.randomCountyCode().toString(), birthday, gender);
    }

    public static String generator(String code, String birthday) {
        return generator(code, birthday, Randoms.randomBoolean());
    }

    /**
     * 生成身份证
     *
     * @param code     code 6位
     * @param birthday 生日 8位
     * @param gender   true男
     * @return 身份证
     */
    public static String generator(String code, String birthday, boolean gender) {
        // 顺序码
        String seq = Strings.leftPad(Randoms.randomInt(100) + Strings.EMPTY, 2, "0");
        // 性别
        int genderCode = Arrays1.random(gender ? MAN_GENDER_CODE : WOMAN_GENDER_CODE);
        String idCard17 = code + birthday + seq + genderCode;
        // 检查码
        char checkCode = IdCards.getCheckCode18(idCard17);
        return idCard17 + checkCode;
    }

    /**
     * 获取签发机关
     *
     * @param idCard idCard
     * @return 签发机关
     */
    public static String getIssueOrg(String idCard) {
        String county = idCard.substring(0, 6);
        return AddressSupport.getSupportCountyName(Integer.parseInt(county)) + PSB;
    }

    /**
     * 获取县地址
     *
     * @param idCard idCard
     * @return 县地址
     */
    public static String getAddress(String idCard) {
        String county = idCard.substring(0, 6);
        return AddressSupport.getCountyAddress(AddressSupport.getSupportCountyCode(Integer.parseInt(county)));
    }

    /**
     * 获取详细地址
     *
     * @param idCard idCard
     * @return 详细地址
     */
    public static String getFullAddress(String idCard) {
        String county = idCard.substring(0, 6);
        Integer countyCode = AddressSupport.getSupportCountyCode(Integer.parseInt(county));
        return AddressSupport.getCountyAddress(countyCode) + AddressGenerator.generatorIdCardAddress();
    }

    /**
     * 获取县编码
     *
     * @param idCard idCard
     * @return 县编码
     */
    public static Integer getAddressCode(String idCard) {
        return Integer.valueOf(idCard.substring(0, 6));
    }

    /**
     * 获取编码
     *
     * @param idCard idCard
     * @return 省, 市, 县
     */
    public static Integer[] getAddressCodeExt(String idCard) {
        String province = idCard.substring(0, 2);
        String city = idCard.substring(0, 4);
        String county = idCard.substring(0, 6);
        return new Integer[]{Integer.valueOf(province), Integer.valueOf(city), Integer.valueOf(county)};
    }

    /**
     * 获取地址
     *
     * @param idCard idCard
     * @return 省, 市, 县
     */
    public static String[] getAddressExt(String idCard) {
        return AddressSupport.getAddressExt(getAddressCode(idCard));
    }

    /**
     * 获取有效期限
     *
     * @param idCard idCard
     * @return yyyyMMdd-yyyyMMdd
     */
    public static String getPeriodString(String idCard) {
        Date[] dates = getPeriod(idCard);
        return Dates.format(dates[0], Dates.YMD2) + "-" + Dates.format(dates[1], Dates.YMD2);
    }

    /**
     * 获取有效期限
     *
     * @param idCard idCard
     * @return yyyyMMdd-yyyyMMdd
     */
    public static Date[] getPeriod(String idCard) {
        int age = IdCards.getAge(idCard);
        int periodYear;
        if (age <= 16) {
            periodYear = 5;
        } else if (age <= 25) {
            periodYear = 10;
        } else {
            periodYear = 20;
        }
        DateStream curr = Dates.stream();
        // 年
        int year = curr.getYear() - (age / 8);
        // 随机月
        int month = Randoms.randomInt(12) + 1;
        // 随机日
        int day = Randoms.randomInt(Dates.getMonthLastDay(year, month)) + 1;
        DateStream period = Dates.stream(Dates.build(year, month, day));
        Date start = period.date();
        Date end = period.addYear(periodYear).date();
        return new Date[]{start, end};
    }

}
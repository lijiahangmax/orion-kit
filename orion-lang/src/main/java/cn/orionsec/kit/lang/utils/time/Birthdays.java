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
package cn.orionsec.kit.lang.utils.time;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.random.Randoms;

import java.util.Calendar;
import java.util.Date;

/**
 * 生日 年龄 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/11 17:15
 */
public class Birthdays {

    private Birthdays() {
    }

    /**
     * 获取身份的年份
     *
     * @param age age
     * @return year
     */
    public static int getBirthdayYear(int age) {
        return Dates.stream().getYear() - age;
    }

    /**
     * 随机生成生日
     *
     * @param age age
     * @return birthday
     */
    public static String generatorBirthday(int age) {
        // 生日年
        int year = getBirthdayYear(age);
        // 随机月
        int month = Randoms.randomInt(12) + 1;
        // 随机日
        int day = Randoms.randomInt(Dates.getMonthLastDay(year, month)) + 1;
        DateStream curr = Dates.stream();
        DateStream birthday = Dates.stream(Dates.build(year, month, day));
        // 当前月
        int currMonth = curr.getMonth();
        if (month > currMonth) {
            // 大于本月
            birthday.subYear(1);
        } else if (month == currMonth && day > curr.getDay()) {
            // 等于本月
            birthday.subMonth(1);
        }
        return birthday.format(Dates.YMD2);
    }

    /**
     * 判断今天是否是生日
     *
     * @param birthday birthday
     * @return true 是生日
     */
    public static boolean todayIsBirthday(Date birthday) {
        DateStream today = Dates.stream();
        DateStream bir = Dates.stream(birthday);
        return today.getMonth() == bir.getMonth() && today.getDay() == bir.getDay();
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
        if (year < 1900) {
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
     * 验证是否为生日 并且不在未来
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 是否为生日
     */
    public static boolean isBirthdayNotFuture(int year, int month, int day) {
        return isBirthday(year, month, day) && !Dates.inFuture(Dates.build(year, month, day));
    }

    /**
     * 计算年龄
     *
     * @param birthday 生日
     * @return 年龄
     */
    public static int getAge(Date birthday) {
        return getAge(birthday.getTime(), System.currentTimeMillis());
    }

    /**
     * 计算年龄
     *
     * @param birthday 生日
     * @return 年龄
     */
    public static int getAge(long birthday) {
        return getAge(birthday, System.currentTimeMillis());
    }

    /**
     * 计算年龄
     *
     * @param birthday 生日
     * @param range    需要对比的日期
     * @return 年龄
     */
    public static int getAge(Date birthday, Date range) {
        if (range == null) {
            range = new Date();
        }
        return getAge(birthday.getTime(), range.getTime());
    }

    /**
     * 计算年龄
     *
     * @param birthday 生日
     * @param range    需要对比的日期
     * @return 年龄
     */
    public static int getAge(long birthday, long range) {
        if (birthday > range) {
            throw Exceptions.argument("birthday is after range");
        }
        Calendar c = Dates.calendar(range);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        boolean isLastDayOfMonth = dayOfMonth == c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.setTimeInMillis(birthday);
        int age = year - c.get(Calendar.YEAR);
        int monthBirth = c.get(Calendar.MONTH);
        if (month == monthBirth) {
            int dayOfMonthBirth = c.get(Calendar.DAY_OF_MONTH);
            boolean isLastDayOfMonthBirth = dayOfMonthBirth == c.getActualMaximum(Calendar.DAY_OF_MONTH);
            if ((!isLastDayOfMonth || !isLastDayOfMonthBirth) && dayOfMonth < dayOfMonthBirth) {
                age--;
            }
        } else if (month < monthBirth) {
            age--;
        }
        return age;
    }

}

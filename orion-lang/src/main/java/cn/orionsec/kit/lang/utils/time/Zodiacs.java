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
package cn.orionsec.kit.lang.utils.time;

import java.util.Calendar;
import java.util.Date;

/**
 * 星座及生肖
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/4 0:30
 */
public class Zodiacs {

    /**
     * 星座分隔时间日
     */
    private static final int[] SPLIT_DAY = new int[]{20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};

    /**
     * 星座
     */
    private static final String[] CONSTELLATION = new String[]{
            "摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
            "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"
    };

    /**
     * 生肖
     */
    private static final String[] CHINESE_ZODIACS = new String[]{
            "鼠", "牛", "虎", "兔", "龙", "蛇",
            "马", "羊", "猴", "鸡", "狗", "猪"
    };

    private Zodiacs() {
    }

    public static String getConstellation() {
        return getConstellation(Dates.calendar());
    }

    public static String getConstellation(Date date) {
        return getConstellation(Dates.calendar(date));
    }

    public static String getConstellation(Calendar c) {
        if (c == null) {
            return null;
        }
        return getConstellation(c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 通过生日计算星座
     *
     * @param month 月 0开始
     * @param day   天
     * @return 星座名
     */
    public static String getConstellation(int month, int day) {
        // 在分隔日前为前一个星座 否则为后一个星座
        return day < SPLIT_DAY[month] ? CONSTELLATION[month] : CONSTELLATION[month + 1];
    }

    public static String getChineseZodiac() {
        return getChineseZodiac(Dates.calendar());
    }

    public static String getChineseZodiac(Date date) {
        return getChineseZodiac(Dates.calendar(date));
    }

    public static String getChineseZodiac(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        return getChineseZodiac(calendar.get(Calendar.YEAR));
    }

    /**
     * 计算生肖
     *
     * @param year 农历年
     * @return 生肖
     */
    public static String getChineseZodiac(int year) {
        if (year < 1900) {
            return null;
        }
        return CHINESE_ZODIACS[(year - 1900) % CHINESE_ZODIACS.length];
    }

}

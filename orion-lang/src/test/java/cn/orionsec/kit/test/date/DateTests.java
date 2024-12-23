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
package cn.orionsec.kit.test.date;

import cn.orionsec.kit.lang.utils.random.Randoms;
import cn.orionsec.kit.lang.utils.time.DateStream;
import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.lang.utils.time.LunarCalendar;
import cn.orionsec.kit.lang.utils.time.ago.DateAgo;
import cn.orionsec.kit.lang.utils.time.ago.DateAgo1;
import org.junit.Test;

import java.util.Date;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/26 15:17
 */
public class DateTests {

    @Test
    public void testFormat() {
        String p = "yyyy-MM-dd HH:mm:ss SSS E W F X k a";
        String format = Dates.current(p);
        Date parse = Dates.parse(format, p);
        System.out.println(format);
        System.out.println(parse);
    }

    @Test
    public void testAgo() {
        Date now = new Date();
        System.out.println(Dates.ago(now, now, false, false));
        System.out.println(Dates.ago(now, now, true, false));
        Date date = Dates.stream().addSecond(2).get();
        System.out.println(Dates.ago(now, date, false, false));
        System.out.println(Dates.ago(now, date, true, true));
        date = Dates.stream().addSecond(-2).get();
        System.out.println(Dates.ago(now, date, false, false));
        System.out.println(Dates.ago(now, date, true, true));
    }

    @Test
    public void testAgo1() {
        DateStream s = new DateStream();
        for (int i = 0; i < 50; i++) {
            Date target = s.get();
            System.out.print(Dates.format(target));
            System.out.print("\t");
            System.out.print(new DateAgo(target).ago());
            System.out.print("\t");
            System.out.print(new DateAgo1(target).strict(true).ago());
            System.out.println();
            s.addMinute(Randoms.randomInt(0, 100000));
        }
    }

    @Test
    public void testLunar() {
        for (int i = 0; i < 50; i++) {
            DateStream d = new DateStream();
            d.addYear(i);
            d.addDay(i);
            Date date = d.get();
            LunarCalendar lunarCalendar = new LunarCalendar(date);
            System.out.println(Dates.format(date, Dates.YMD) + " " + lunarCalendar.toString() + " " + lunarCalendar.toChineseString());
        }
    }

}

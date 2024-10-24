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
package com.orion.test.date;

import com.orion.lang.utils.time.DateStream;
import com.orion.lang.utils.time.Dates;
import com.orion.lang.utils.time.cron.Cron;
import com.orion.lang.utils.time.cron.CronSupport;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/26 15:45
 */
public class CronTests {

    String[] ex = {
            "1 1 1 1 1 ? 2021-2022   ",
            "* 0/5 * 1 * ? 2021-2022 ",
            "0 0 23 L * ?            ",
            "0 0 3 2/3 * ?           ",
            "* 0/5 * * * ?           ",
            "0 0 3 ? * 1/1           ",
            "0 0 3 ? * 7,6           ",
            "0 0 14-6 ? * FRI-MON    ",
            "0 0 14-6 ? * 1-6 2021   ",
            "0 15 10 ? * MON-FRI     ",
            "0 15 10 ? * 6L 2002-2006",
            "0 10,14,16 * * * ?      ",
            "0 0/30 9-17 * * ?       ",
            "0 12 * ? * WED          ",
            "0 12 * * * ?            ",
            "15 10 * ? * 1-6         ",
            "15 10 * * * ?           ",
            "0 15 10 * * ? *         ",
            "0 15 10 * * ? 2005      ",
            "0 * 14 * * ?            ",
            "0 0/5 14 * * ?          ",
            "0 0/5 14,18 * * ?       ",
            "0 0-5 14 * * ?          ",
            "0 10,44 14 ? 3 WED      ",
            "0 15 10 ? * MON-FRI     ",
            "0 15 10 15 * ?          ",
            "0 15 10 L * ?           ",
            "0 15 10 ? * 6L          ",
            "0 15 10 ? * 6L 2002-2005",
            "0 15 10 ? * 6#3         ",
    };

    @Test
    public void test1() {
        for (String e : ex) {
            System.out.println(CronSupport.getCron(e).getExpressionSummary());
        }
    }

    @Test
    public void test2() {
        DateStream stream = DateStream.of(new Date());
        Date date = stream.get();

        Cron cron = CronSupport.getCron(date);
        System.out.println(cron);
        System.out.println();

        // true
        System.out.println(cron.isSatisfiedBy(date));
        // null
        System.out.println(cron.getNextValidTimeAfter(date));
        // not null
        System.out.println(cron.getNextInvalidTimeAfter(date));

        // 之前
        stream.addMinute(-1);
        date = stream.get();
        System.out.println();

        // false
        System.out.println(cron.isSatisfiedBy(date));
        // not null
        System.out.println(cron.getNextValidTimeAfter(date));
        // not null
        System.out.println(cron.getNextInvalidTimeAfter(date));

        // 未来
        stream.addMinute(2);
        date = stream.get();
        System.out.println();

        // false
        System.out.println(cron.isSatisfiedBy(date));
        // null
        System.out.println(cron.getNextValidTimeAfter(date));
        // not null
        System.out.println(cron.getNextInvalidTimeAfter(date));
    }

    @Test
    public void test3() {
        Cron cron = CronSupport.getCron("0 0/5 * * * ? 2020");
        System.out.println(cron);

        List<Date> nextTimes = CronSupport.getNextTime(cron, 5);
        for (Date nextTime : nextTimes) {
            System.out.println(Dates.format(nextTime));
        }
        System.out.println();

        cron = CronSupport.getCron("0 0/5 * * * ?");
        System.out.println(cron);

        nextTimes = CronSupport.getNextTime(cron, 5);
        for (Date nextTime : nextTimes) {
            System.out.println(Dates.format(nextTime));
        }
    }

}

package com.orion.test.date;

import com.orion.lang.utils.random.Randoms;
import com.orion.lang.utils.time.DateStream;
import com.orion.lang.utils.time.Dates;
import com.orion.lang.utils.time.LunarCalendar;
import com.orion.lang.utils.time.ago.DateAgo;
import com.orion.lang.utils.time.ago.DateAgo1;
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

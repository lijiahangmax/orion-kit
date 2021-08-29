package com.orion.test.date;

import com.orion.utils.random.Randoms;
import com.orion.utils.time.DateStream;
import com.orion.utils.time.Dates;
import com.orion.utils.time.ago.DateAgo;
import com.orion.utils.time.ago.DateAgo1;
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
        System.out.println(Dates.current());
    }

    @Test
    public void testParse() {
        System.out.println(Dates.format(Dates.parse(Dates.current())));
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

}

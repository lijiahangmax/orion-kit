package com.orion.test.date;

import com.orion.utils.time.Dates;
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
        Date date = Dates.stream().addSecond(2).get();
        System.out.println(Dates.ago(new Date(), date, false, false));
        System.out.println(Dates.ago(new Date(), date, true, true));
        date = Dates.stream().addSecond(-2).get();
        System.out.println(Dates.ago(new Date(), date, false, false));
        System.out.println(Dates.ago(new Date(), date, true, true));
    }

    @Test
    public void testDateAgo() {
        Date date = Dates.stream().addSecond(2).get();
        System.out.println(Dates.dateAgo(new Date(), date, false));
        System.out.println(Dates.dateAgo(new Date(), date, true));
        date = Dates.stream().addSecond(-2).get();
        System.out.println(Dates.dateAgo(new Date(), date, false));
        System.out.println(Dates.dateAgo(new Date(), date, true));
    }

}

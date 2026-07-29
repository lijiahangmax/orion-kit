package cn.orionsec.kit.lang.utils.time.ago;

import cn.orionsec.kit.lang.utils.time.Dates;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * DateAgo 单元测试
 */
public class DateAgoTest {

    @Test
    public void testNow() {
        Date now = Dates.build(2020, 6, 15, 10, 30, 0);
        DateAgo ago = new DateAgo(now, now);
        assertEquals("现在", ago.ago());
    }

    @Test
    public void testJustNow() {
        Date source = Dates.build(2020, 6, 15, 10, 30, 30);
        Date target = Dates.build(2020, 6, 15, 10, 30, 10);
        DateAgo ago = new DateAgo(source, target);
        ago.vague(true);
        assertEquals("刚刚", ago.ago());
    }

    @Test
    public void testSecondsAgo() {
        Date source = Dates.build(2020, 6, 15, 10, 30, 30, 0);
        Date target = Dates.build(2020, 6, 15, 10, 30, 10, 0);
        DateAgo ago = new DateAgo(source, target);
        ago.vague(false);
        assertEquals("20秒前", ago.ago());
    }

    @Test
    public void testMinutesAgo() {
        Date source = Dates.build(2020, 6, 15, 10, 30, 0, 1);
        Date target = Dates.build(2020, 6, 15, 10, 25, 0, 1);
        DateAgo ago = new DateAgo(source, target);
        assertEquals("5分钟前", ago.ago());
    }

    @Test
    public void testHoursAgo() {
        Date source = Dates.build(2020, 6, 15, 10, 30, 0);
        Date target = Dates.build(2020, 6, 15, 7, 30, 0);
        DateAgo ago = new DateAgo(source, target);
        assertEquals("3小时前", ago.ago());
    }

    @Test
    public void testYesterday() {
        Date source = Dates.build(2020, 6, 15, 10, 0, 0);
        Date target = Dates.build(2020, 6, 14, 10, 0, 0);
        DateAgo ago = new DateAgo(source, target);
        ago.vague(true);
        assertEquals("昨天", ago.ago());
    }

    @Test
    public void testBeforeYesterday() {
        Date source = Dates.build(2020, 6, 15, 10, 0, 0);
        Date target = Dates.build(2020, 6, 13, 10, 0, 0);
        DateAgo ago = new DateAgo(source, target);
        ago.vague(true);
        assertEquals("前天", ago.ago());
    }

    @Test
    public void testDaysAgo() {
        Date source = Dates.build(2020, 6, 15, 10, 0, 0);
        Date target = Dates.build(2020, 6, 10, 10, 0, 0);
        DateAgo ago = new DateAgo(source, target);
        ago.vague(false);
        assertEquals("5天前", ago.ago());
    }

    @Test
    public void testWeeksAgo() {
        Date source = Dates.build(2020, 6, 22, 10, 0, 0);
        Date target = Dates.build(2020, 6, 1, 10, 0, 0);
        DateAgo ago = new DateAgo(source, target);
        ago.useWeek(true);
        String result = ago.ago();
        assertTrue("Expected weeks ago but got: " + result, result.contains("周前"));
    }

    @Test
    public void testMonthsAgo() {
        Date source = Dates.build(2020, 6, 15, 10, 0, 0);
        Date target = Dates.build(2020, 3, 15, 10, 0, 0);
        DateAgo ago = new DateAgo(source, target);
        assertEquals("3月前", ago.ago());
    }

    @Test
    public void testLastYear() {
        Date source = Dates.build(2020, 6, 15, 10, 0, 0);
        Date target = Dates.build(2019, 6, 15, 10, 0, 0);
        DateAgo ago = new DateAgo(source, target);
        ago.vague(true);
        assertEquals("去年", ago.ago());
    }

    @Test
    public void testYearsAgo() {
        Date source = Dates.build(2020, 6, 15);
        Date target = Dates.build(2017, 6, 15);
        DateAgo ago = new DateAgo(source, target);
        assertTrue(ago.ago().contains("年前"));
    }

    @Test
    public void testLongAgo() {
        Date source = Dates.build(2020, 6, 15);
        Date target = Dates.build(2000, 6, 15);
        DateAgo ago = new DateAgo(source, target);
        ago.vague(true);
        assertEquals("很久以前", ago.ago());
    }

    // Future tests
    @Test
    public void testMoment() {
        Date source = Dates.build(2020, 6, 15, 10, 30, 10);
        Date target = Dates.build(2020, 6, 15, 10, 30, 30);
        DateAgo ago = new DateAgo(source, target);
        ago.vague(true);
        assertEquals("片刻之后", ago.ago());
    }

    @Test
    public void testSecondsFuture() {
        Date source = Dates.build(2020, 6, 15, 10, 30, 10, 0);
        Date target = Dates.build(2020, 6, 15, 10, 30, 30, 0);
        DateAgo ago = new DateAgo(source, target);
        ago.vague(false);
        assertEquals("20秒后", ago.ago());
    }

    @Test
    public void testMinutesFuture() {
        Date source = Dates.build(2020, 6, 15, 10, 25, 0);
        Date target = Dates.build(2020, 6, 15, 10, 30, 0);
        DateAgo ago = new DateAgo(source, target);
        assertEquals("5分钟后", ago.ago());
    }

    @Test
    public void testHoursFuture() {
        Date source = Dates.build(2020, 6, 15, 7, 30, 0);
        Date target = Dates.build(2020, 6, 15, 10, 30, 0);
        DateAgo ago = new DateAgo(source, target);
        assertEquals("3小时后", ago.ago());
    }

    @Test
    public void testTomorrow() {
        Date source = Dates.build(2020, 6, 14, 10, 0, 0);
        Date target = Dates.build(2020, 6, 15, 10, 0, 0);
        DateAgo ago = new DateAgo(source, target);
        ago.vague(true);
        assertEquals("明天", ago.ago());
    }

    @Test
    public void testAfterTomorrow() {
        Date source = Dates.build(2020, 6, 13, 10, 0, 0);
        Date target = Dates.build(2020, 6, 15, 10, 0, 0);
        DateAgo ago = new DateAgo(source, target);
        ago.vague(true);
        assertEquals("后天", ago.ago());
    }

    @Test
    public void testNextYear() {
        Date source = Dates.build(2020, 6, 15);
        Date target = Dates.build(2021, 6, 15);
        DateAgo ago = new DateAgo(source, target);
        ago.vague(true);
        assertEquals("明年", ago.ago());
    }

    @Test
    public void testLongFuture() {
        Date source = Dates.build(2020, 6, 15);
        Date target = Dates.build(2040, 6, 15);
        DateAgo ago = new DateAgo(source, target);
        ago.vague(true);
        assertEquals("很久以后", ago.ago());
    }

    @Test
    public void testCustomHint() {
        DateAgoHint hint = new DateAgoHint();
        hint.setJustNow("just now");
        hint.setNow("now");
        Date now = Dates.build(2020, 6, 15, 10, 30, 0);
        DateAgo ago = new DateAgo(now, now, hint);
        assertEquals("now", ago.ago());
    }

    @Test
    public void testDateAgo1Now() {
        Date now = Dates.build(2020, 6, 15, 10, 30, 0);
        DateAgo1 ago1 = new DateAgo1(now, now);
        assertEquals("现在", ago1.ago());
    }

    @Test
    public void testDateAgo1Strict() {
        Date now = Dates.build(2020, 6, 15, 10, 30, 0);
        DateAgo1 ago1 = new DateAgo1(now, now);
        ago1.strict(true);
        assertEquals("0秒前", ago1.ago());
    }

    @Test
    public void testDateAgo1Yesterday() {
        Date source = Dates.build(2020, 6, 15, 10, 0, 0);
        Date target = Dates.build(2020, 6, 14, 10, 0, 0);
        DateAgo1 ago1 = new DateAgo1(source, target);
        assertEquals("昨天", ago1.ago());
    }

    @Test
    public void testDateAgo1HoursAgo() {
        Date source = Dates.build(2020, 6, 15, 10, 0, 0);
        Date target = Dates.build(2020, 6, 15, 7, 0, 0);
        DateAgo1 ago1 = new DateAgo1(source, target);
        assertEquals("3小时前", ago1.ago());
    }

    @Test
    public void testDateAgo1MonthsAgo() {
        Date source = Dates.build(2020, 6, 15, 10, 0, 0);
        Date target = Dates.build(2020, 3, 15, 10, 0, 0);
        DateAgo1 ago1 = new DateAgo1(source, target);
        assertEquals("3月前", ago1.ago());
    }

    @Test
    public void testDateAgo1LastYear() {
        Date source = Dates.build(2020, 6, 15);
        Date target = Dates.build(2019, 6, 15);
        DateAgo1 ago1 = new DateAgo1(source, target);
        assertEquals("去年", ago1.ago());
    }

    @Test
    public void testDateAgo1Tomorrow() {
        Date source = Dates.build(2020, 6, 14, 10, 0, 0);
        Date target = Dates.build(2020, 6, 15, 10, 0, 0);
        DateAgo1 ago1 = new DateAgo1(source, target);
        assertEquals("明天", ago1.ago());
    }

    @Test
    public void testDateAgoHintDefaults() {
        DateAgoHint hint = new DateAgoHint();
        assertEquals("很久以前", hint.getLongAgo());
        assertEquals("很久以后", hint.getLongFuture());
        assertEquals("去年", hint.getLastYear());
        assertEquals("明年", hint.getNextYear());
        assertEquals("昨天", hint.getYesterday());
        assertEquals("明天", hint.getTomorrow());
        assertEquals("前天", hint.getBeforeYesterday());
        assertEquals("后天", hint.getAfterTomorrow());
        assertEquals("刚刚", hint.getJustNow());
        assertEquals("现在", hint.getNow());
        assertEquals("片刻之后", hint.getMoment());
        assertEquals("年前", hint.getYearAgo());
        assertEquals("年后", hint.getYearFuture());
        assertEquals("月前", hint.getMonthAgo());
        assertEquals("月后", hint.getMonthFuture());
        assertEquals("周前", hint.getWeekAgo());
        assertEquals("周后", hint.getWeekFuture());
        assertEquals("天前", hint.getDayAgo());
        assertEquals("天后", hint.getDayFuture());
        assertEquals("小时前", hint.getHourAgo());
        assertEquals("小时后", hint.getHourFuture());
        assertEquals("分钟前", hint.getMinuteAgo());
        assertEquals("分钟后", hint.getMinuteFuture());
        assertEquals("秒前", hint.getSecondAgo());
        assertEquals("秒后", hint.getSecondFuture());
    }
}

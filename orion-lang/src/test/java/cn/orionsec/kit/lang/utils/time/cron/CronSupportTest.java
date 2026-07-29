package cn.orionsec.kit.lang.utils.time.cron;

import cn.orionsec.kit.lang.utils.time.Dates;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * CronSupport 单元测试
 */
public class CronSupportTest {

    @Test
    public void testIsValidExpression() {
        assertTrue(CronSupport.isValidExpression("0 0 12 * * ?"));
        assertTrue(CronSupport.isValidExpression("0 15 10 * * ?"));
        assertTrue(CronSupport.isValidExpression("0 0/5 * * * ?"));
        assertTrue(CronSupport.isValidExpression("0 0 12 ? * MON-FRI"));
        assertFalse(CronSupport.isValidExpression("invalid_cron"));
        assertFalse(CronSupport.isValidExpression(""));
    }

    @Test
    public void testGetCronExpression() {
        Date d = Dates.build(2020, 6, 15, 10, 30, 45);
        String expr = CronSupport.getCronExpression(d);
        assertNotNull(expr);
        assertTrue(expr.contains("45"));
        assertTrue(expr.contains("30"));
        assertTrue(expr.contains("10"));
    }

    @Test
    public void testGetCronFromDate() {
        Date d = Dates.build(2020, 6, 15, 10, 30, 0);
        Cron cron = CronSupport.getCron(d);
        assertNotNull(cron);
        assertTrue(cron.isSatisfiedBy(d));
    }

    @Test
    public void testGetCronFromExpression() {
        Cron cron = CronSupport.getCron("0 0 12 * * ?");
        assertNotNull(cron);
    }

    @Test
    public void testGetNextTime() {
        Cron cron = CronSupport.getCron("0 0/5 * * * ?");
        Date next = CronSupport.getNextTime(cron);
        assertNotNull(next);
        assertTrue(next.after(new Date()));
    }

    @Test
    public void testGetNextTimeWithDate() {
        Cron cron = CronSupport.getCron("0 0/5 * * * ?");
        Date base = Dates.build(2020, 6, 15, 10, 0, 0);
        Date next = CronSupport.getNextTime(cron, base);
        assertNotNull(next);
        assertTrue(next.after(base));
    }

    @Test
    public void testGetNextTimeTimes() {
        Cron cron = CronSupport.getCron("0 0/5 * * * ?");
        Date base = Dates.build(2020, 6, 15, 10, 0, 0);
        List<Date> dates = CronSupport.getNextTime(cron, base, 5);
        assertEquals(5, dates.size());
        // 检查有序
        for (int i = 1; i < dates.size(); i++) {
            assertTrue(dates.get(i).after(dates.get(i - 1)));
        }
    }

    @Test
    public void testGetNextTimeTimesDefault() {
        Cron cron = CronSupport.getCron("0 0/5 * * * ?");
        List<Date> dates = CronSupport.getNextTime(cron, 3);
        assertEquals(3, dates.size());
    }

    @Test
    public void testCronIsSatisfiedBy() {
        // 每天12:00:00
        Cron cron = CronSupport.getCron("0 0 12 * * ?");
        Date satisfied = Dates.build(2020, 6, 15, 12, 0, 0);
        Date notSatisfied = Dates.build(2020, 6, 15, 13, 0, 0);
        assertTrue(cron.isSatisfiedBy(satisfied));
        assertFalse(cron.isSatisfiedBy(notSatisfied));
    }

    @Test
    public void testCronGetNextValidTimeAfter() {
        Cron cron = CronSupport.getCron("0 0 12 * * ?");
        Date base = Dates.build(2020, 6, 15, 10, 0, 0);
        Date next = cron.getNextValidTimeAfter(base);
        assertNotNull(next);
        // 应该是同一天12:00
        assertEquals("2020-06-15 12:00:00", Dates.format(next));
    }

    @Test
    public void testCronGetNextInvalidTimeAfter() {
        Cron cron = CronSupport.getCron("0 0 12 * * ?");
        Date base = Dates.build(2020, 6, 15, 12, 0, 0);
        Date nextInvalid = cron.getNextInvalidTimeAfter(base);
        assertNotNull(nextInvalid);
        // 下一个无效时间应该在12:00:00之后
        assertTrue(nextInvalid.after(base));
    }

    @Test
    public void testCronExpressionSummary() {
        Cron cron = CronSupport.getCron("0 0 12 * * ?");
        String summary = cron.getExpressionSummary();
        assertNotNull(summary);
        assertTrue(summary.length() > 0);
    }

    @Test
    public void testCronEveryMinute() {
        Cron cron = CronSupport.getCron("0 * * * * ?");
        Date base = Dates.build(2020, 6, 15, 10, 30, 0);
        Date next = cron.getNextValidTimeAfter(base);
        assertNotNull(next);
        assertEquals("2020-06-15 10:31:00", Dates.format(next));
    }

    @Test
    public void testCronSpecificDay() {
        // 每月15日 10:00
        Cron cron = CronSupport.getCron("0 0 10 15 * ?");
        Date base = Dates.build(2020, 6, 14, 0, 0, 0);
        Date next = cron.getNextValidTimeAfter(base);
        assertNotNull(next);
        assertEquals("2020-06-15 10:00:00", Dates.format(next));
    }

    @Test
    public void testCronWithYear() {
        Cron cron = CronSupport.getCron("0 0 12 * * ? 2020");
        Date base = Dates.build(2020, 6, 15, 10, 0, 0);
        Date next = cron.getNextValidTimeAfter(base);
        assertNotNull(next);
    }

    @Test
    public void testCronWeekday() {
        // 周一到周五的10:15
        Cron cron = CronSupport.getCron("0 15 10 ? * MON-FRI");
        Date base = Dates.build(2020, 6, 15, 0, 0, 0); // Monday
        Date next = cron.getNextValidTimeAfter(base);
        assertNotNull(next);
        assertEquals("2020-06-15 10:15:00", Dates.format(next));
    }

    @Test
    public void testCronLastDayOfMonth() {
        Cron cron = CronSupport.getCron("0 0 23 L * ?");
        Date base = Dates.build(2020, 6, 15, 0, 0, 0);
        Date next = cron.getNextValidTimeAfter(base);
        assertNotNull(next);
        assertEquals("2020-06-30 23:00:00", Dates.format(next));
    }

    @Test
    public void testCronToString() {
        Cron cron = CronSupport.getCron("0 0 12 * * ?");
        assertNotNull(cron.toString());
    }
}

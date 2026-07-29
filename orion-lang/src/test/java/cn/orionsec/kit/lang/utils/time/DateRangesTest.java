package cn.orionsec.kit.lang.utils.time;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * DateRanges 单元测试
 */
public class DateRangesTest {

    @Test
    public void testInRange() {
        Date start = Dates.build(2020, 1, 1);
        Date end = Dates.build(2020, 12, 31);
        Date mid = Dates.build(2020, 6, 15);
        assertTrue(DateRanges.inRange(mid, start, end));
    }

    @Test
    public void testNotInRange() {
        Date start = Dates.build(2020, 1, 1);
        Date end = Dates.build(2020, 6, 1);
        Date outside = Dates.build(2020, 7, 15);
        assertTrue(DateRanges.notInRange(outside, start, end));
    }

    @Test
    public void testInRangeBoundary() {
        Date start = Dates.build(2020, 1, 1);
        Date end = Dates.build(2020, 12, 31);
        // start boundary
        assertTrue(DateRanges.inRange(start, start, end));
        // end boundary
        assertTrue(DateRanges.inRange(end, start, end));
    }

    @Test
    public void testCross() {
        Date start1 = Dates.build(2020, 1, 1);
        Date end1 = Dates.build(2020, 6, 30);
        Date start2 = Dates.build(2020, 3, 1);
        Date end2 = Dates.build(2020, 9, 30);
        assertTrue(DateRanges.cross(start1, end1, start2, end2));
    }

    @Test
    public void testUncross() {
        Date start1 = Dates.build(2020, 1, 1);
        Date end1 = Dates.build(2020, 3, 31);
        Date start2 = Dates.build(2020, 6, 1);
        Date end2 = Dates.build(2020, 9, 30);
        assertTrue(DateRanges.uncross(start1, end1, start2, end2));
    }

    @Test
    public void testRangeInRange() {
        Date outerStart = Dates.build(2020, 1, 1);
        Date outerEnd = Dates.build(2020, 12, 31);
        Date innerStart = Dates.build(2020, 3, 1);
        Date innerEnd = Dates.build(2020, 6, 30);
        assertTrue(DateRanges.rangeInRange(outerStart, outerEnd, innerStart, innerEnd));
    }

    @Test
    public void testRangeNotInRange() {
        Date outerStart = Dates.build(2020, 3, 1);
        Date outerEnd = Dates.build(2020, 6, 30);
        Date innerStart = Dates.build(2020, 1, 1);
        Date innerEnd = Dates.build(2020, 12, 31);
        assertTrue(DateRanges.rangeNotInRange(outerStart, outerEnd, innerStart, innerEnd));
    }

    @Test
    public void testBefore() {
        Date d1 = Dates.build(2020, 1, 1);
        Date d2 = Dates.build(2020, 6, 15);
        assertTrue(DateRanges.before(d1, d2));
        assertFalse(DateRanges.before(d2, d1));
    }

    @Test
    public void testAfter() {
        Date d1 = Dates.build(2020, 6, 15);
        Date d2 = Dates.build(2020, 1, 1);
        assertTrue(DateRanges.after(d1, d2));
        assertFalse(DateRanges.after(d2, d1));
    }

    @Test
    public void testBeforeSameDate() {
        Date d = Dates.build(2020, 6, 15);
        assertFalse(DateRanges.before(d, d));
        assertFalse(DateRanges.after(d, d));
    }
}

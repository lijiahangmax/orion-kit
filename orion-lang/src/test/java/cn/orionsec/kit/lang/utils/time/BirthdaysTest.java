package cn.orionsec.kit.lang.utils.time;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Birthdays 单元测试
 */
public class BirthdaysTest {

    @Test
    public void testGetBirthdayYear() {
        int currentYear = Dates.stream().getYear();
        assertEquals(currentYear - 25, Birthdays.getBirthdayYear(25));
        assertEquals(currentYear - 0, Birthdays.getBirthdayYear(0));
    }

    @Test
    public void testGetAge() {
        // 2000-01-01 出生, 2020-06-15 时计算年龄
        Date birthday = Dates.build(2000, 1, 1);
        Date range = Dates.build(2020, 6, 15);
        int age = Birthdays.getAge(birthday, range);
        assertEquals(20, age);
    }

    @Test
    public void testGetAgeBeforeBirthday() {
        // 2000-08-15 出生, 2020-06-15 时年龄应该是19
        Date birthday = Dates.build(2000, 8, 15);
        Date range = Dates.build(2020, 6, 15);
        int age = Birthdays.getAge(birthday, range);
        assertEquals(19, age);
    }

    @Test
    public void testGetAgeAfterBirthday() {
        // 2000-03-10 出生, 2020-06-15 时年龄应该是20
        Date birthday = Dates.build(2000, 3, 10);
        Date range = Dates.build(2020, 6, 15);
        int age = Birthdays.getAge(birthday, range);
        assertEquals(20, age);
    }

    @Test
    public void testIsBirthday() {
        assertTrue(Birthdays.isBirthday(1990, 6, 15));
        assertTrue(Birthdays.isBirthday(2000, 2, 29)); // leap year
        assertFalse(Birthdays.isBirthday(2001, 2, 29)); // not leap year
        assertFalse(Birthdays.isBirthday(1899, 6, 15)); // year < 1900
        assertFalse(Birthdays.isBirthday(2000, 0, 15)); // invalid month
        assertFalse(Birthdays.isBirthday(2000, 13, 15)); // invalid month
        assertFalse(Birthdays.isBirthday(2000, 6, 0)); // invalid day
        assertFalse(Birthdays.isBirthday(2000, 6, 32)); // invalid day
    }

    @Test
    public void testIsBirthday31DaysInvalid() {
        // 4,6,9,11月没有31天
        assertFalse(Birthdays.isBirthday(2000, 4, 31));
        assertFalse(Birthdays.isBirthday(2000, 6, 31));
        assertFalse(Birthdays.isBirthday(2000, 9, 31));
        assertFalse(Birthdays.isBirthday(2000, 11, 31));
        // 1,3,5,7,8,10,12月有31天
        assertTrue(Birthdays.isBirthday(2000, 1, 31));
        assertTrue(Birthdays.isBirthday(2000, 3, 31));
        assertTrue(Birthdays.isBirthday(2000, 7, 31));
    }

    @Test
    public void testIsBirthdayNotFuture() {
        // 过去的日期
        assertTrue(Birthdays.isBirthdayNotFuture(2000, 6, 15));
        // 未来的日期
        assertFalse(Birthdays.isBirthdayNotFuture(2099, 6, 15));
    }

    @Test
    public void testGeneratorBirthday() {
        String birthday = Birthdays.generatorBirthday(25);
        assertNotNull(birthday);
        assertEquals(8, birthday.length()); // yyyyMMdd format
    }

    @Test
    public void testTodayIsBirthday() {
        // 今天是生日
        Date today = new Date();
        assertTrue(Birthdays.todayIsBirthday(today));
        // 其他日子不是
        DateStream stream = Dates.stream();
        Date otherDay = stream.addMonth(1).addDay(1).get();
        assertFalse(Birthdays.todayIsBirthday(otherDay));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAgeFutureBirthday() {
        Date birthday = Dates.build(2099, 1, 1);
        Date range = Dates.build(2020, 6, 15);
        Birthdays.getAge(birthday, range);
    }

    @Test
    public void testGetAgeSameDay() {
        Date birthday = Dates.build(2000, 6, 15);
        Date range = Dates.build(2020, 6, 15);
        int age = Birthdays.getAge(birthday, range);
        assertEquals(20, age);
    }
}

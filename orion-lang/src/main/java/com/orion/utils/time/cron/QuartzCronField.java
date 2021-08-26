package com.orion.utils.time.cron;

import com.orion.utils.Exceptions;
import com.orion.utils.Valid;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.temporal.*;

/**
 * copy with spring
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/24 12:33
 */
final class QuartzCronField extends CronField {

    private final Type rollForwardType;

    private final TemporalAdjuster adjuster;

    private final String value;

    private QuartzCronField(Type type, TemporalAdjuster adjuster, String value) {
        this(type, type, adjuster, value);
    }

    /**
     * 需要前滚不同类型的字段的构造函数 与该字段所代表的类型不同
     *
     * @see #parseDaysOfWeek
     */
    private QuartzCronField(Type type, Type rollForwardType, TemporalAdjuster adjuster, String value) {
        super(type);
        this.adjuster = adjuster;
        this.value = value;
        this.rollForwardType = rollForwardType;
    }

    /**
     * 返回给定值是否为 quartz 日期字段
     */
    public static boolean isQuartzDaysOfMonthField(String value) {
        return value.contains("L") || value.contains("W");
    }

    /**
     * 将给定的值解析为一个月的天数 {@code QuartzCronField}
     * cron 表达式的第四个条目
     * 在给定值中需要 L 或 W
     */
    public static QuartzCronField parseDaysOfMonth(String value) {
        int idx = value.lastIndexOf('L');
        if (idx != -1) {
            TemporalAdjuster adjuster;
            if (idx != 0) {
                throw Exceptions.argument("unrecognized characters before 'L' in '" + value + "'");
            } else if (value.length() == 2 && value.charAt(1) == 'W') { // "LW"
                adjuster = lastWeekdayOfMonth();
            } else {
                if (value.length() == 1) { // "L"
                    adjuster = lastDayOfMonth();
                } else { // "L-[0-9]+"
                    int offset = Integer.parseInt(value.substring(idx + 1));
                    if (offset >= 0) {
                        throw Exceptions.argument("offset '" + offset + " should be < 0 '" + value + "'");
                    }
                    adjuster = lastDayWithOffset(offset);
                }
            }
            return new QuartzCronField(Type.DAY_OF_MONTH, adjuster, value);
        }
        idx = value.lastIndexOf('W');
        if (idx != -1) {
            if (idx == 0) {
                throw Exceptions.argument("no day-of-month before 'W' in '" + value + "'");
            } else if (idx != value.length() - 1) {
                throw Exceptions.argument("unrecognized characters after 'W' in '" + value + "'");
            } else {
                // "[0-9]+W"
                int dayOfMonth = Integer.parseInt(value.substring(0, idx));
                dayOfMonth = Type.DAY_OF_MONTH.checkValidValue(dayOfMonth);
                TemporalAdjuster adjuster = weekdayNearestTo(dayOfMonth);
                return new QuartzCronField(Type.DAY_OF_MONTH, adjuster, value);
            }
        }
        throw Exceptions.argument("no 'L' or 'W' found in '" + value + "'");
    }

    /**
     * 返回给定的值是否是 quartz 的星期字段
     */
    public static boolean isQuartzDaysOfWeekField(String value) {
        return value.contains("L") || value.contains("#");
    }

    /**
     * 将给定的值解析为星期几 {@code QuartzCronField}, cron 表达式的第六个条目
     * 在给定值中需要 L 或 #
     */
    public static QuartzCronField parseDaysOfWeek(String value) {
        int idx = value.lastIndexOf('L');
        if (idx != -1) {
            if (idx != value.length() - 1) {
                throw Exceptions.argument("unrecognized characters after 'L' in '" + value + "'");
            } else {
                TemporalAdjuster adjuster;
                if (idx == 0) {
                    throw Exceptions.argument("no day-of-week before 'L' in '" + value + "'");
                } else { // "[0-7]L"
                    DayOfWeek dayOfWeek = parseDayOfWeek(value.substring(0, idx));
                    adjuster = lastInMonth(dayOfWeek);
                }
                return new QuartzCronField(Type.DAY_OF_WEEK, Type.DAY_OF_MONTH, adjuster, value);
            }
        }
        idx = value.lastIndexOf('#');
        if (idx != -1) {
            if (idx == 0) {
                throw Exceptions.argument("no day-of-week before '#' in '" + value + "'");
            } else if (idx == value.length() - 1) {
                throw Exceptions.argument("no ordinal after '#' in '" + value + "'");
            }
            // "[0-7]#[0-9]+"
            DayOfWeek dayOfWeek = parseDayOfWeek(value.substring(0, idx));
            int ordinal = Integer.parseInt(value.substring(idx + 1));
            if (ordinal <= 0) {
                throw Exceptions.argument("ordinal '" + ordinal + "' in '" + value + "' must be positive number ");
            }
            TemporalAdjuster adjuster = dayOfWeekInMonth(ordinal, dayOfWeek);
            return new QuartzCronField(Type.DAY_OF_WEEK, Type.DAY_OF_MONTH, adjuster, value);
        }
        throw Exceptions.argument("no 'L' or '#' found in '" + value + "'");
    }

    private static DayOfWeek parseDayOfWeek(String value) {
        int dayOfWeek = Integer.parseInt(value);
        if (dayOfWeek == 0) {
            // cron is 0 based; java.time 1 based
            dayOfWeek = 7;
        }
        try {
            return DayOfWeek.of(dayOfWeek);
        } catch (DateTimeException ex) {
            throw Exceptions.argument(ex.getMessage() + " '" + value + "'", ex);
        }
    }

    /**
     * 返回一个重置为午夜的调节器
     */
    private static TemporalAdjuster atMidnight() {
        return temporal -> {
            if (temporal.isSupported(ChronoField.NANO_OF_DAY)) {
                return temporal.with(ChronoField.NANO_OF_DAY, 0);
            } else {
                return temporal;
            }
        };
    }

    /**
     * 返回一个调整器, 它返回一个新的时间集到当月的最后一天午夜
     */
    private static TemporalAdjuster lastDayOfMonth() {
        TemporalAdjuster adjuster = TemporalAdjusters.lastDayOfMonth();
        return temporal -> {
            Temporal result = adjuster.adjustInto(temporal);
            return rollbackToMidnight(temporal, result);
        };
    }

    /**
     * 返回一个调整器, 该调整器返回该月的最后一个工作日
     */
    private static TemporalAdjuster lastWeekdayOfMonth() {
        TemporalAdjuster adjuster = TemporalAdjusters.lastDayOfMonth();
        return temporal -> {
            Temporal lastDom = adjuster.adjustInto(temporal);
            Temporal result;
            int dow = lastDom.get(ChronoField.DAY_OF_WEEK);
            if (dow == 6) { // Saturday
                result = lastDom.minus(1, ChronoUnit.DAYS);
            } else if (dow == 7) { // Sunday
                result = lastDom.minus(2, ChronoUnit.DAYS);
            } else {
                result = lastDom;
            }
            return rollbackToMidnight(temporal, result);
        };
    }

    /**
     * 返回一个时间调整器 它可以找到该月的第 n 天到最后一天
     *
     * @param offset 负偏移量, 即 -3 表示倒数第三
     * @return 第 n 到最后一天的调整器
     */
    private static TemporalAdjuster lastDayWithOffset(int offset) {
        Valid.isTrue(offset < 0, "offset should be < 0");
        TemporalAdjuster adjuster = TemporalAdjusters.lastDayOfMonth();
        return temporal -> {
            Temporal result = adjuster.adjustInto(temporal).plus(offset, ChronoUnit.DAYS);
            return rollbackToMidnight(temporal, result);
        };
    }

    /**
     * 返回一个时间调整器
     * 它找到最接近给定月份的工作日
     * 如果 {@code dayOfMonth} 落在星期六, 则日期将移回星期五
     * 如果它落在星期日 (或者如果 {@code dayOfMonth} 是 1 并且它落在星期六) , 它会向前移动到星期一
     *
     * @param dayOfMonth 目标日期
     * @return 工作日最近的调整器
     */
    private static TemporalAdjuster weekdayNearestTo(int dayOfMonth) {
        return temporal -> {
            int current = Type.DAY_OF_MONTH.get(temporal);
            int dayOfWeek = temporal.get(ChronoField.DAY_OF_WEEK);

            if ((current == dayOfMonth && dayOfWeek < 6) || // dayOfMonth is a weekday
                    (dayOfWeek == 5 && current == dayOfMonth - 1) || // dayOfMonth is a Saturday, so Friday before
                    (dayOfWeek == 1 && current == dayOfMonth + 1) || // dayOfMonth is a Sunday, so Monday after
                    (dayOfWeek == 1 && dayOfMonth == 1 && current == 3)) { // dayOfMonth is the 1st, so Monday 3rd
                return temporal;
            }
            int count = 0;
            while (count++ < Cron.MAX_ATTEMPTS) {
                temporal = Type.DAY_OF_MONTH.elapseUntil(cast(temporal), dayOfMonth);
                temporal = atMidnight().adjustInto(temporal);
                current = Type.DAY_OF_MONTH.get(temporal);
                if (current == dayOfMonth) {
                    dayOfWeek = temporal.get(ChronoField.DAY_OF_WEEK);

                    if (dayOfWeek == 6) { // Saturday
                        if (dayOfMonth != 1) {
                            return temporal.minus(1, ChronoUnit.DAYS);
                        } else {
                            // exception for "1W" fields: execute on nearest Monday
                            return temporal.plus(2, ChronoUnit.DAYS);
                        }
                    } else if (dayOfWeek == 7) { // Sunday
                        return temporal.plus(1, ChronoUnit.DAYS);
                    } else {
                        return temporal;
                    }
                }
            }
            return null;
        };
    }

    /**
     * 返回一个时间调整器, 它在一个月内找到给定的星期几中的最后一天
     */
    private static TemporalAdjuster lastInMonth(DayOfWeek dayOfWeek) {
        TemporalAdjuster adjuster = TemporalAdjusters.lastInMonth(dayOfWeek);
        return temporal -> {
            Temporal result = adjuster.adjustInto(temporal);
            return rollbackToMidnight(temporal, result);
        };
    }

    /**
     * 返回一个时间调整器, 它在一个月中找到次出现的给定的星期几
     */
    private static TemporalAdjuster dayOfWeekInMonth(int ordinal, DayOfWeek dayOfWeek) {
        TemporalAdjuster adjuster = TemporalAdjusters.dayOfWeekInMonth(ordinal, dayOfWeek);
        return temporal -> {
            Temporal result = adjuster.adjustInto(temporal);
            return rollbackToMidnight(temporal, result);
        };
    }

    /**
     * 将给定的时间回滚到午夜
     * 如果月份相同时则返回前一个, 以确保我们不会在开始之前结束
     */
    private static Temporal rollbackToMidnight(Temporal current, Temporal result) {
        if (result.get(ChronoField.DAY_OF_MONTH) == current.get(ChronoField.DAY_OF_MONTH)) {
            return current;
        } else {
            return atMidnight().adjustInto(result);
        }
    }

    @Override
    public <T extends Temporal & Comparable<? super T>> T nextOrSame(T temporal) {
        T result = adjust(temporal);
        if (result != null) {
            if (result.compareTo(temporal) < 0) {
                // We ended up before the start, roll forward and try again
                temporal = this.rollForwardType.rollForward(temporal);
                result = adjust(temporal);
                if (result != null) {
                    result = type().reset(result);
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <T extends Temporal & Comparable<? super T>> T adjust(T temporal) {
        return (T) this.adjuster.adjustInto(temporal);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuartzCronField)) {
            return false;
        }
        QuartzCronField other = (QuartzCronField) o;
        return type() == other.type() &&
                this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return type() + " '" + this.value + "'";
    }

}

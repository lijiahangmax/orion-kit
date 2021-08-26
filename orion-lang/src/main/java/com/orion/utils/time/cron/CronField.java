package com.orion.utils.time.cron;

import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Valid;

import java.time.DateTimeException;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.ValueRange;
import java.util.function.BiFunction;

/**
 * copy with spring
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/24 12:27
 */
abstract class CronField {

    private static final String[] MONTHS = new String[]{
            "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
            "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
    };

    private static final String[] DAYS = new String[]{"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    private final Type type;

    protected CronField(Type type) {
        this.type = type;
    }

    /**
     * 返回启用了 0 纳秒的 {@code CronField}
     */
    public static CronField zeroNanos() {
        return BitsCronField.zeroNanos();
    }

    /**
     * 将给定值解析为秒 {@code CronField}, cron 表达式的第一个条目
     */
    public static CronField parseSeconds(String value) {
        return BitsCronField.parseSeconds(value);
    }

    /**
     * 将给定的值解析为分钟 {@code CronField}, cron 表达式的第二个条目
     */
    public static CronField parseMinutes(String value) {
        return BitsCronField.parseMinutes(value);
    }

    /**
     * 将给定值解析为小时 {@code CronField}, cron 表达式的第三个条目
     */
    public static CronField parseHours(String value) {
        return BitsCronField.parseHours(value);
    }

    /**
     * 将给定的值解析为一个月的天数 {@code CronField}, cron 表达式的第四个条目
     */
    public static CronField parseDaysOfMonth(String value) {
        if (!QuartzCronField.isQuartzDaysOfMonthField(value)) {
            return BitsCronField.parseDaysOfMonth(value);
        } else {
            return parseList(value, Type.DAY_OF_MONTH, (field, type) -> {
                if (QuartzCronField.isQuartzDaysOfMonthField(field)) {
                    return QuartzCronField.parseDaysOfMonth(field);
                } else {
                    return BitsCronField.parseDaysOfMonth(field);
                }
            });
        }
    }

    /**
     * 将给定值解析为月份 {@code CronField}, cron 表达式的第五个条目
     */
    public static CronField parseMonth(String value) {
        value = replaceOrdinals(value, MONTHS);
        return BitsCronField.parseMonth(value);
    }

    /**
     * 将给定的值解析为星期几 {@code CronField}, cron 表达式的第六个条目
     */
    public static CronField parseDaysOfWeek(String value) {
        value = replaceOrdinals(value, DAYS);
        if (!QuartzCronField.isQuartzDaysOfWeekField(value)) {
            return BitsCronField.parseDaysOfWeek(value);
        } else {
            return parseList(value, Type.DAY_OF_WEEK, (field, type) -> {
                if (QuartzCronField.isQuartzDaysOfWeekField(field)) {
                    return QuartzCronField.parseDaysOfWeek(field);
                } else {
                    return BitsCronField.parseDaysOfWeek(field);
                }
            });
        }
    }

    private static CronField parseList(String value, Type type, BiFunction<String, Type, CronField> parseFieldFunction) {
        Valid.notBlank(value, "value must not be empty");
        String[] fields = Util.delimitedListToStringArray(value, ",");
        CronField[] cronFields = new CronField[fields.length];
        for (int i = 0; i < fields.length; i++) {
            cronFields[i] = parseFieldFunction.apply(fields[i], type);
        }
        return CompositeCronField.compose(cronFields, type, value);
    }

    private static String replaceOrdinals(String value, String[] list) {
        value = value.toUpperCase();
        for (int i = 0; i < list.length; i++) {
            String replacement = Integer.toString(i + 1);
            value = replace(value, list[i], replacement);
        }
        return value;
    }

    /**
     * 获取与此 cron 字段匹配的序列中的下一个或相同的 {@link Temporal}
     *
     * @param temporal 时间
     * @return 匹配模式的下一个或相同的时间
     */
    public abstract <T extends Temporal & Comparable<? super T>> T nextOrSame(T temporal);

    protected Type type() {
        return this.type;
    }

    @SuppressWarnings("unchecked")
    protected static <T extends Temporal & Comparable<? super T>> T cast(Temporal temporal) {
        return (T) temporal;
    }

    /**
     * 表示cron 字段的类型, 即秒 分 小时 月份 月份 星期几
     */
    protected enum Type {
        NANO(ChronoField.NANO_OF_SECOND),
        SECOND(ChronoField.SECOND_OF_MINUTE, ChronoField.NANO_OF_SECOND),
        MINUTE(ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE, ChronoField.NANO_OF_SECOND),
        HOUR(ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE, ChronoField.NANO_OF_SECOND),
        DAY_OF_MONTH(ChronoField.DAY_OF_MONTH, ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE, ChronoField.NANO_OF_SECOND),
        MONTH(ChronoField.MONTH_OF_YEAR, ChronoField.DAY_OF_MONTH, ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE, ChronoField.NANO_OF_SECOND),
        DAY_OF_WEEK(ChronoField.DAY_OF_WEEK, ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE, ChronoField.NANO_OF_SECOND);

        private final ChronoField field;

        private final ChronoField[] lowerOrders;

        Type(ChronoField field, ChronoField... lowerOrders) {
            this.field = field;
            this.lowerOrders = lowerOrders;
        }

        /**
         * 为给定的时间返回此类型的值
         *
         * @return 该类型的值
         */
        public int get(Temporal date) {
            return date.get(this.field);
        }

        /**
         * 返回此类型的一般范围 例如, 此方法将为 {@link #MONTH} 返回 0-31
         *
         * @return 该字段的范围
         */
        public ValueRange range() {
            return this.field.range();
        }

        /**
         * 检查给定的值是否有效 即是否在 {@linkplain #range() 范围}内
         *
         * @param value 要检查的值
         * @return 传入的值
         * @throws IllegalArgumentException 如果给定值无效
         */
        public int checkValidValue(int value) {
            if (this == DAY_OF_WEEK && value == 0) {
                return value;
            } else {
                try {
                    return this.field.checkValidIntValue(value);
                } catch (DateTimeException ex) {
                    throw Exceptions.argument(ex.getMessage(), ex);
                }
            }
        }

        /**
         * 此字段的当前值与目标值之间的差值经过给定的时间
         * 返回的时间会将给定的目标作为此类型的当前值, 但 {@link #DAY_OF_MONTH} 的情况并非如此
         *
         * @param temporal 要经过的时间
         * @param goal     目标值
         * @param <T>      时间的类型
         * @return 已过去的时间 通常将 {@code goal} 作为值用于此类型
         */
        public <T extends Temporal & Comparable<? super T>> T elapseUntil(T temporal, int goal) {
            int current = get(temporal);
            ValueRange range = temporal.range(this.field);
            if (current < goal) {
                if (range.isValidIntValue(goal)) {
                    return cast(temporal.with(this.field, goal));
                } else {
                    // goal is invalid, eg. 29th Feb, so roll forward
                    long amount = range.getMaximum() - current + 1;
                    return this.field.getBaseUnit().addTo(temporal, amount);
                }
            } else {
                long amount = goal + range.getMaximum() - current + 1 - range.getMinimum();
                return this.field.getBaseUnit().addTo(temporal, amount);
            }
        }

        /**
         * 前滚给定时间直到它到达下一个更高的顺序字段
         * 调用此方法等同于调用 {@link #elapseUntil(Temporal, int)} 并将目标设置为此字段范围的最小值
         *
         * @param temporal 要前滚的时间
         * @param <T>      时间的类型
         * @return 前滚的时间
         */
        public <T extends Temporal & Comparable<? super T>> T rollForward(T temporal) {
            int current = get(temporal);
            ValueRange range = temporal.range(this.field);
            long amount = range.getMaximum() - current + 1;
            return this.field.getBaseUnit().addTo(temporal, amount);
        }

        /**
         * 将此字段和给定时间的所有低阶字段重置为其最小值
         * 例如: 对于 {@link #MINUTE} 此方法将 纳秒 秒 分钟 重置为 0
         *
         * @param temporal 要重置的时间
         * @param <T>      时间的类型
         * @return 重置时间
         */
        public <T extends Temporal> T reset(T temporal) {
            for (ChronoField lowerOrder : this.lowerOrders) {
                if (temporal.isSupported(lowerOrder)) {
                    temporal = lowerOrder.adjustInto(temporal, temporal.range(lowerOrder).getMinimum());
                }
            }
            return temporal;
        }

        @Override
        public String toString() {
            return this.field.toString();
        }
    }

    /**
     * 用另一个字符串替换一个字符串中所有出现的子字符串
     *
     * @param inString   进行检查
     * @param oldPattern 替换
     * @param newPattern 插入
     * @return 替换
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (Strings.isEmpty(inString) || Strings.isEmpty(oldPattern) || newPattern == null) {
            return inString;
        }
        int index = inString.indexOf(oldPattern);
        if (index == -1) {
            // no occurrence -> can return input as-is
            return inString;
        }
        int capacity = inString.length();
        if (newPattern.length() > oldPattern.length()) {
            capacity += 16;
        }
        StringBuilder sb = new StringBuilder(capacity);
        int pos = 0;  // our position in the old string
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString, pos, index);
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        // append any characters to the right of a match
        sb.append(inString, pos, inString.length());
        return sb.toString();
    }

}
